package com.code065.alquilervehiculos.dto.api;

import com.code065.alquilervehiculos.model.Alquiler;
import com.code065.alquilervehiculos.model.EstadoAlquiler;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AlquilerResponse(Long idAlquiler, LocalDate fechaInicio, LocalDate fechaFin, EstadoAlquiler estado, Integer dias, BigDecimal precioDiaAplicado, BigDecimal total, Long idCliente, Long idVehiculo, String creadoPor) {
    public static AlquilerResponse from(Alquiler alquiler) {
        return new AlquilerResponse(
                alquiler.getIdAlquiler(),
                alquiler.getFechaInicio(),
                alquiler.getFechaFin(),
                alquiler.getEstado(),
                alquiler.getDias(),
                alquiler.getPrecioDiaAplicado(),
                alquiler.getTotal(),
                alquiler.getCliente() != null ? alquiler.getCliente().getIdCliente() : null,
                alquiler.getVehiculo() != null ? alquiler.getVehiculo().getIdVehiculo() : null,
                alquiler.getCreadoPor() != null ? alquiler.getCreadoPor().getUsername() : null
        );
    }
}
