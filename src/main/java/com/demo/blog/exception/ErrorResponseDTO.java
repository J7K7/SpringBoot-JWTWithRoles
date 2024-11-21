package com.demo.blog.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDTO {
    private String message;
}
