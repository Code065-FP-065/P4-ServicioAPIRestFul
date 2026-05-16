package com.code065.alquilervehiculos.dto.api;

public record LoginResponse(
        String tokenType,
        String accessToken,
        long expiresInSeconds
) {}
