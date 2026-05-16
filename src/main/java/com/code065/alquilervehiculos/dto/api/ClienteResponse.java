package com.code065.alquilervehiculos.dto.api;

import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nombre,
        String apellidos,
        String dni,
        String telefono,
        LocalDateTime createdAt
) {}
