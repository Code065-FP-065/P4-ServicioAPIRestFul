package com.code065.alquilervehiculos.dto.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 150) String apellidos,
        @NotBlank @Size(max = 20) String dni,
        @Size(max = 20) String telefono
) {}
