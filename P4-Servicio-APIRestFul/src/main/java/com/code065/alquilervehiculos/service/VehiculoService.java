package com.code065.alquilervehiculos.service;

import com.code065.alquilervehiculos.model.Vehiculo;

import java.util.List;
import java.util.Optional;

public interface VehiculoService {

    List<Vehiculo> listarVehiculos();

    Optional<Vehiculo> buscarVehiculoPorId(Long id);

    Vehiculo guardarVehiculo(Vehiculo vehiculo);

    void eliminarVehiculo(Long id);
}
