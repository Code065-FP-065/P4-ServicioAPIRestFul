package com.code065.alquilervehiculos.dto.api;

import com.code065.alquilervehiculos.model.EstadoVehiculo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VehiculoResponse(
        Long id,
        String matricula,
        String marca,
        String modelo,
        String tipo,
        BigDecimal precioDia,
        EstadoVehiculo estado,
        LocalDateTime createdAt
) {}
