package com.uade.tpo.marketplace.controllers.image;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponse {
    private Long id;
    private String file;
}
