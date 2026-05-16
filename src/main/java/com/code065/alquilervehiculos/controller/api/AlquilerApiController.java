package com.code065.alquilervehiculos.controller.api;

import com.code065.alquilervehiculos.dto.api.AlquilerResponse;
import com.code065.alquilervehiculos.model.Alquiler;
import com.code065.alquilervehiculos.service.AlquilerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Alquileres")
@SecurityRequirement(name = "bearerAuth")
public class AlquilerApiController {

    private final AlquilerService alquilerService;

    public AlquilerApiController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    @GetMapping("/alquileres")
    public List<AlquilerResponse> listarAlquileres() {
        return alquilerService.listarAlquileres().stream()
                .map(this::toResponse)
                .toList();
    }

    private AlquilerResponse toResponse(Alquiler alquiler) {
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
                alquiler.getCreadoPor() != null ? alquiler.getCreadoPor().getUsername() : null,
                alquiler.getModificadoPor() != null ? alquiler.getModificadoPor().getUsername() : null,
                alquiler.getCreatedAt()
        );
    }
}
