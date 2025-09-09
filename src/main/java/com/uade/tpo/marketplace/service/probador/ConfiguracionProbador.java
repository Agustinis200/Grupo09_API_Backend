package com.uade.tpo.marketplace.service.probador;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.genai.Client;
import com.google.genai.types.Blob;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import jakarta.annotation.PostConstruct;

@Component
public class ConfiguracionProbador {
    @Value("${gemini.api.key}")
    private String apiKey;

    private Client client;

    @PostConstruct
    public void init() {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public List<byte[]> realizarPruebaVirtual(MultipartFile prendaFile, MultipartFile usuarioFile) throws IOException {
        Blob prendaBlob = blobFromMultipartFile(prendaFile);
        Blob usuarioBlob = blobFromMultipartFile(usuarioFile);

        String prompt = buildPrompt();

        return generarEdicion(prompt, prendaBlob, usuarioBlob);
    }
    

    public List<byte[]> realizarPruebaVirtualConBlobDB(byte[] prendaBytes, String prendaMimeType, MultipartFile usuarioFile) throws IOException {
        Blob prendaBlob = blobFromBytes(prendaBytes, prendaMimeType);
        Blob usuarioBlob = blobFromMultipartFile(usuarioFile);

        String prompt = buildPrompt();

        return generarEdicion(prompt, prendaBlob, usuarioBlob);
    }

    public List<byte[]> realizarPruebaVirtualCompleta(byte[] prendaBytes, String prendaMimeType, 
                                                     byte[] usuarioBytes, String usuarioMimeType) {
        Blob prendaBlob = blobFromBytes(prendaBytes, prendaMimeType);
        Blob usuarioBlob = blobFromBytes(usuarioBytes, usuarioMimeType);

        String prompt = buildPrompt();

        return generarEdicion(prompt, prendaBlob, usuarioBlob);
    }

    private List<byte[]> generarEdicion(String prompt, Blob prendaBlob, Blob usuarioBlob) {
        List<byte[]> imagenesResultantes = new ArrayList<>();
        try {            
            GenerateContentConfig cfg = GenerateContentConfig.builder()
                    .responseModalities(List.of("IMAGE", "TEXT"))
                    .build();

            Content editReq = Content.fromParts(
                    Part.fromText("IMAGE_1 (garment reference):"),
                    Part.builder().inlineData(prendaBlob).build(),
                    Part.fromText("IMAGE_2 (base/final target):"),
                    Part.builder().inlineData(usuarioBlob).build(),
                    Part.fromText(prompt)
            );

            GenerateContentResponse resp = client.models.generateContent("gemini-2.5-flash-image-preview", editReq, cfg);
            
            for (Part p : resp.parts()) {
                p.inlineData().ifPresent(blob -> {
                    if (blob.mimeType().orElse("").startsWith("image/")) {
                        blob.data().ifPresent(imagenesResultantes::add);
                    }
                });
            }

        } catch (Exception e) {
            System.err.println("Error en tiempo de ejecución detallado: " + e.toString());
            e.printStackTrace();
            
            if (e instanceof java.util.UnknownFormatConversionException) {
                return intentarEnfoqueAlternativo(prendaBlob, usuarioBlob);
            }
            
            throw new RuntimeException("Error al generar la edición: " + e.getMessage(), e);
        }

        if (imagenesResultantes.isEmpty()) {
            System.out.println("El modelo no devolvió imágenes, intentando enfoque alternativo...");
            return intentarEnfoqueAlternativo(prendaBlob, usuarioBlob);
        }
        
        return imagenesResultantes;
    }
    
    private List<byte[]> intentarEnfoqueAlternativo(Blob prendaBlob, Blob usuarioBlob) {
        List<byte[]> imagenesResultantes = new ArrayList<>();
        try {
            System.out.println("Ejecutando enfoque alternativo simplificado...");
            
            GenerateContentConfig cfg = GenerateContentConfig.builder()
                    .responseModalities(List.of("IMAGE", "TEXT"))
                    .build();

            String promptSimplificado = """
            Create a virtual try-on effect: Take the garment from IMAGE_1 and make it appear as if the person in IMAGE_2 is wearing it.
            Maintain the identity, face, and background from IMAGE_2 unchanged. Return only the edited result as an image.
            """;
            
            Content editReq = Content.fromParts(
                    Part.fromText("IMAGE_1:"),
                    Part.builder().inlineData(prendaBlob).build(),
                    Part.fromText("IMAGE_2:"),
                    Part.builder().inlineData(usuarioBlob).build(),
                    Part.fromText(promptSimplificado)
            );

            GenerateContentResponse resp = client.models.generateContent("gemini-2.5-flash-image-preview", editReq, cfg);
            
            for (Part p : resp.parts()) {
                p.inlineData().ifPresent(blob -> {
                    if (blob.mimeType().orElse("").startsWith("image/")) {
                        blob.data().ifPresent(imagenesResultantes::add);
                    }
                });
            }
            
        } catch (Exception e) {
            System.err.println("Error en enfoque alternativo: " + e.toString());
            e.printStackTrace();
            throw new RuntimeException("Error al generar la edición (enfoque alternativo): " + e.getMessage(), e);
        }
        
        if (imagenesResultantes.isEmpty()) {
            throw new RuntimeException("El modelo no devolvió imágenes en ninguno de los enfoques.");
        }
        
        return imagenesResultantes;
    }


    private Blob blobFromMultipartFile(MultipartFile file) throws IOException {
        return Blob.builder()
                .data(file.getBytes())
                .mimeType(file.getContentType())
                .build();
    }
    

    private Blob blobFromBytes(byte[] bytes, String mimeType) {
        return Blob.builder()
                .data(bytes)
                .mimeType(mimeType)
                .build();
    }


    private String buildPrompt() {
        
        return """
        **ROLE:** You are a professional photo editor specializing in hyper-realistic apparel compositing for e-commerce. Your task is a virtual try-on.

        **CONTEXT:**
        *   **IMAGE_1:** Contains the source garment to be extracted. This is our target texture and shape.
        *   **IMAGE_2:** Contains the target subject and scene. The garment from IMAGE_1 will be placed here.
        *   **OBJECT OF INTEREST:** The garment.

        **WORKFLOW (Follow these steps precisely):**

        **1. Semantic Segmentation (IMAGE_1):**
        *   Create a perfect, pixel-accurate mask of the **OBJECT OF INTEREST** in IMAGE_1.
        *   Isolate every detail: seams, zippers, logos, texture, and original colors.
        *   Exclude all background and any part of the original mannequin or model. The output of this step is only the garment with a transparent background.

        **2. Geometric Transformation & Warping:**
        *   Analyze the pose, posture, and body contours of the subject in IMAGE_2.
        *   Apply non-rigid transformations (warp, perspective, scale) to the segmented garment so it drapes realistically over the subject's body.
        *   Simulate natural folds, creases, and stretching consistent with the fabric type and the subject's pose. The garment must look like it is being worn, not just pasted on.

        **3. Photorealistic Compositing (Integration into IMAGE_2):**
        *   Replace the subject's original clothing in IMAGE_2 completely with your transformed garment from the previous step.
        *   Analyze the lighting environment of IMAGE_2 (light source direction, softness, color temperature).
        *   Render physically accurate lighting and shadows on the new garment to match the scene. This includes ambient occlusion, contact shadows where the garment touches the body, and specular highlights.

        **CRITICAL RULES (Non-negotiable):**
        *   **IDENTITY PRESERVATION:** The subject's face, facial expression, skin tone, hair, and any visible accessories in IMAGE_2 must remain **100% UNCHANGED**.
        *   **SCENE INTEGRITY:** The background and all other elements of IMAGE_2 that are not the subject's main garment **MUST NOT BE ALTERED**.
        *   **NO ARTIFACTS:** The final output must be clean, with no blending artifacts, halos, or mismatched pixels. The integration must be seamless.
        *   **NO FUSION:** Do not blend or merge any part of the original garment from IMAGE_2 with the new garment. It must be a complete replacement.

        **FINAL OUTPUT:** Return only the modified IMAGE_2, appearing as a single, authentic photograph.
        """;
    }
}