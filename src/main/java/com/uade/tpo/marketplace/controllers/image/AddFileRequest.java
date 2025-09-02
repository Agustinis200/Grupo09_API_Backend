package com.uade.tpo.marketplace.controllers.image;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddFileRequest {
    private String name;
    private MultipartFile file;
    private Long productId;
}
