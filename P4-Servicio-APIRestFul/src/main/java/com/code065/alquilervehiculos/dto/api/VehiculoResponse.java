package com.code065.alquilervehiculos.dto.api;

import com.code065.alquilervehiculos.model.EstadoVehiculo;
import com.code065.alquilervehiculos.model.Vehiculo;

import java.math.BigDecimal;

public record VehiculoResponse(Long idVehiculo, String matricula, String marca, String modelo, String tipo, BigDecimal precioDia, EstadoVehiculo estado) {
    public static VehiculoResponse from(Vehiculo vehiculo) {
        return new VehiculoResponse(vehiculo.getIdVehiculo(), vehiculo.getMatricula(), vehiculo.getMarca(), vehiculo.getModelo(), vehiculo.getTipo(), vehiculo.getPrecioDia(), vehiculo.getEstado());
    }
}
