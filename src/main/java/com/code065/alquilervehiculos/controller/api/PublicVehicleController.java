package com.code065.alquilervehiculos.controller.api;

import com.code065.alquilervehiculos.model.Alquiler;
import com.code065.alquilervehiculos.model.Vehiculo;
import com.code065.alquilervehiculos.service.AlquilerService;
import com.code065.alquilervehiculos.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PublicVehicleController {

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping("/vehiculos")
    public List<Vehiculo> getAllVehiculos() {
        return vehiculoService.listarVehiculos();
    }

    @GetMapping("/vehiculos/{id}")
    public ResponseEntity<?> getVehiculoById(@PathVariable Long id) {
        return vehiculoService.buscarVehiculoPorId(id)
                .map(vehiculo -> ResponseEntity.ok().body(vehiculo))
                .orElse(ResponseEntity.notFound().build());
    }
}