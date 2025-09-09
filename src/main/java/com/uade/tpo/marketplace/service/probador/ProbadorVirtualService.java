package com.uade.tpo.marketplace.service.probador;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.marketplace.entity.Image;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.repository.ImageRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;

@Service
public class ProbadorVirtualService {

    @Autowired
    private ConfiguracionProbador virtualTryOn;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ImageRepository imageRepository;
    
    public Map<String, Object> probarPrendaVirtual(Long productoId, MultipartFile imagenUsuario) throws Exception {
        Product producto = productRepository.findById(productoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        
        Image imagenProducto = imageRepository.findByProductId(producto.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no tiene imágenes"));
        
        Blob imagenBlob = imagenProducto.getImage();
        
        try {            
            byte[] imageBytes = getBytesFromBlob(imagenBlob);
            
            String mimeType = detectMimeType(imageBytes);

            if (imagenUsuario == null || imagenUsuario.isEmpty()) {
                throw new IllegalArgumentException("La imagen del usuario está vacía");
            }
            List<byte[]> resultados = virtualTryOn.realizarPruebaVirtualConBlobDB(
                imageBytes, 
                mimeType, 
                imagenUsuario
            );
            
            if (resultados.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, 
                    "No se pudieron generar imágenes. Intente con otra foto."
                );
            }
            
            String base64Image = Base64.getEncoder().encodeToString(resultados.get(0));
            String dataUrl = "data:" + mimeType + ";base64," + base64Image; 
            Map<String, Object> response = new HashMap<>();
            response.put("image", dataUrl);
            response.put("productName", producto.getName());
            
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al procesar la imagen del producto: " + e.getMessage()
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al procesar las imágenes: " + e.getMessage()
            );
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error en el procesamiento: " + e.getMessage()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error inesperado: " + e.getMessage()
            );
        }
    }
    
    private byte[] getBytesFromBlob(Blob blob) throws SQLException, IOException {
        if (blob == null) {
            throw new SQLException("El blob es nulo");
        }
        
        try {
            int blobLength = (int) blob.length();
            if (blobLength <= 0) {
                throw new SQLException("El blob está vacío (longitud <= 0)");
            }
                        
            byte[] bytes = blob.getBytes(1, blobLength);
            if (bytes == null || bytes.length == 0) {
                throw new SQLException("No se pudieron obtener bytes del blob");
            }
            
            return bytes;
        } catch (SQLException e) {
            throw e;
        }
    }
    

    private String detectMimeType(byte[] bytes) {
        if (bytes == null || bytes.length < 2) {
            return "image/jpeg";
        }
        
        StringBuilder firstBytesHex = new StringBuilder("Primeros bytes (hex): ");
        for (int i = 0; i < Math.min(bytes.length, 16); i++) {
            firstBytesHex.append(String.format("%02X ", bytes[i]));
        }
        
        // JPEG: FF D8
        if (bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8) {
            return "image/jpeg";
        }
        
        // PNG: 89 50 4E 47
        if (bytes.length >= 4 && 
            bytes[0] == (byte) 0x89 && 
            bytes[1] == 'P' && 
            bytes[2] == 'N' && 
            bytes[3] == 'G') {
            return "image/png";
        }
        
        // GIF: 'GIF87a' o 'GIF89a'
        if (bytes.length >= 6 &&
            bytes[0] == 'G' && 
            bytes[1] == 'I' && 
            bytes[2] == 'F' && 
            bytes[3] == '8' &&
            (bytes[4] == '7' || bytes[4] == '9') && 
            bytes[5] == 'a') {
            return "image/gif";
        }
        
        if (bytes.length >= 2 && 
            bytes[0] == 'B' && 
            bytes[1] == 'M') {
            return "image/bmp";
        }
        
        if (bytes.length >= 12 && 
            bytes[0] == 'R' && 
            bytes[1] == 'I' && 
            bytes[2] == 'F' && 
            bytes[3] == 'F' && 
            bytes[8] == 'W' && 
            bytes[9] == 'E' && 
            bytes[10] == 'B' && 
            bytes[11] == 'P') {
            return "image/webp";
        }
        
        return "image/jpeg";
    }
}
