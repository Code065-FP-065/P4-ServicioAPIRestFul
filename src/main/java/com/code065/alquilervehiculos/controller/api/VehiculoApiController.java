package com.code065.alquilervehiculos.controller.api;

import com.code065.alquilervehiculos.dto.api.VehiculoResponse;
import com.code065.alquilervehiculos.model.Vehiculo;
import com.code065.alquilervehiculos.service.VehiculoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Vehiculos")
@SecurityRequirement(name = "bearerAuth")
public class VehiculoApiController {

    private final VehiculoService vehiculoService;

    public VehiculoApiController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping("/vehiculos")
    public List<VehiculoResponse> listarVehiculos() {
        return vehiculoService.listarVehiculos().stream()
                .map(this::toResponse)
                .toList();
    }

    private VehiculoResponse toResponse(Vehiculo vehiculo) {
        return new VehiculoResponse(
                vehiculo.getIdVehiculo(),
                vehiculo.getMatricula(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getTipo(),
                vehiculo.getPrecioDia(),
                vehiculo.getEstado(),
                vehiculo.getCreatedAt()
        );
    }
}
