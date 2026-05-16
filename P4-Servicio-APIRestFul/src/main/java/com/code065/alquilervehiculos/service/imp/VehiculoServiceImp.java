package com.code065.alquilervehiculos.service.imp;

import com.code065.alquilervehiculos.model.Vehiculo;
import com.code065.alquilervehiculos.repository.AlquilerRepository;
import com.code065.alquilervehiculos.repository.VehiculoRepository;
import com.code065.alquilervehiculos.service.VehiculoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoServiceImp implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final AlquilerRepository alquilerRepository;

    public VehiculoServiceImp(VehiculoRepository vehiculoRepository, AlquilerRepository alquilerRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.alquilerRepository = alquilerRepository;
    }

    @Override
    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepository.findAll();
    }

    @Override
    public Optional<Vehiculo> buscarVehiculoPorId(Long id) {
        return vehiculoRepository.findById(id);
    }

    @Override
    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    @Override
    public void eliminarVehiculo(Long id) {
        if (alquilerRepository.existsByVehiculo_IdVehiculo(id)) {
            throw new IllegalStateException("No se puede eliminar el vehículo porque tiene alquileres asociados.");
        }
        vehiculoRepository.deleteById(id);
    }
}
