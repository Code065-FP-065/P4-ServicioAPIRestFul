package com.code065.alquilervehiculos.dto.api;

import com.code065.alquilervehiculos.model.EstadoVehiculo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record VehiculoRequest(
        @NotBlank @Size(max = 20) String matricula,
        @NotBlank @Size(max = 100) String marca,
        @NotBlank @Size(max = 100) String modelo,
        @NotBlank @Size(max = 100) String tipo,
        @NotNull @DecimalMin("0.0") BigDecimal precioDia,
        @NotNull EstadoVehiculo estado
) {}
