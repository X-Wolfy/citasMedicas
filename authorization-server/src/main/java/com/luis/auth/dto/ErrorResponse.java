package com.luis.auth.dto;

public record ErrorResponse(
        int codigo,
        String mensaje
) { }
