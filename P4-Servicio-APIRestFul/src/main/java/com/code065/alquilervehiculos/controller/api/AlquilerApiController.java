package com.code065.alquilervehiculos.controller.api;

import com.code065.alquilervehiculos.dto.api.AlquilerResponse;
import com.code065.alquilervehiculos.service.AlquilerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Alquileres API")
public class AlquilerApiController {

    private final AlquilerService alquilerService;

    public AlquilerApiController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    @GetMapping("/api/secure/alquileres")
    @Operation(summary = "Endpoint protegido: lista todos los alquileres")
    public List<AlquilerResponse> listarAlquileres() {
        return alquilerService.listarAlquileres().stream().map(AlquilerResponse::from).toList();
    }
}
