package com.code065.alquilervehiculos.dto.api;

import com.code065.alquilervehiculos.model.EstadoAlquiler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AlquilerResponse(
        Long id,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        EstadoAlquiler estado,
        Integer dias,
        BigDecimal precioDiaAplicado,
        BigDecimal total,
        Long clienteId,
        Long vehiculoId,
        String creadoPor,
        String modificadoPor,
        LocalDateTime createdAt
) {}
