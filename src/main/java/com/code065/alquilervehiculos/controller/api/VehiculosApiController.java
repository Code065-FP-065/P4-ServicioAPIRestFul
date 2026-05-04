package com.code065.alquilervehiculos.controller.api;

import com.code065.alquilervehiculos.model.Vehiculo;
import com.code065.alquilervehiculos.service.VehiculoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alquileres")
public class VehiculosApiController {

    private final VehiculoService vehiculoService;


    public VehiculosApiController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<Vehiculo> listarVehiculos() {
        return vehiculoService.listarVehiculos();
    }
}
