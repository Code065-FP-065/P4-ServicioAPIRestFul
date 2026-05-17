package com.code065.alquilervehiculos.controller.api;

import com.code065.alquilervehiculos.dto.VehiculoResponseDTO;
import com.code065.alquilervehiculos.service.VehiculoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculosApiController {

    private final VehiculoService vehiculoService;


    public VehiculosApiController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<VehiculoResponseDTO> listarVehiculos() {
        return vehiculoService.listarVehiculos()
                .stream()
                .map( vehiculo -> new VehiculoResponseDTO(
                        vehiculo.getIdVehiculo(),
                        vehiculo.getMatricula(),
                        vehiculo.getMarca(),
                        vehiculo.getModelo(),
                        vehiculo.getTipo(),
                        vehiculo.getPrecioDia(),
                        vehiculo.getEstado()
                ))
                .toList();
    }
}
