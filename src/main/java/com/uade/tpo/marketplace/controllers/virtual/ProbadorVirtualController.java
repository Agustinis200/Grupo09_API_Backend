package com.uade.tpo.marketplace.controllers.virtual;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.marketplace.service.probador.ProbadorVirtualService;

@RestController
@RequestMapping("/probador")
public class ProbadorVirtualController {

    @Autowired
    private ProbadorVirtualService probadorVirtualService;

    @PostMapping("/{productoId}")
    public ResponseEntity<?> probarPrenda(
            @PathVariable Long productoId,
            @RequestPart("user") MultipartFile imagenUsuario) {
        
        try {
            Map<String, Object> resultado = probadorVirtualService.probarPrendaVirtual(productoId, imagenUsuario);
            return ResponseEntity.ok(resultado);
            
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(Map.of("error", e.getReason()));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al procesar la solicitud: " + e.getMessage()));
        }
    }
}
