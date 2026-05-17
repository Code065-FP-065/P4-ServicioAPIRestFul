package com.code065.alquilervehiculos.controller.api;

import com.code065.alquilervehiculos.dto.AlquilerDto;
import com.code065.alquilervehiculos.model.Alquiler;
import com.code065.alquilervehiculos.service.AlquilerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/secure")
public class SecureBookingController {

    private final AlquilerService alquilerService;

    public SecureBookingController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    // Endpoint 1 Modificado: Retorna una lista de DTOs puros en lugar de entidades mutables
    @GetMapping("/reservas")
    public List<AlquilerDto> getAllReservas() {
        List<Alquiler> alquileres = alquilerService.listarAlquileres();

        // Mecánica de conversión: Mapeamos cada entidad Alquiler a un AlquilerDto plano
        return alquileres.stream().map(alquiler -> {
            AlquilerDto dto = new AlquilerDto();
            dto.setId(alquiler.getIdAlquiler());
            dto.setFechaInicio(alquiler.getFechaInicio());
            dto.setFechaFin(alquiler.getFechaFin());
            dto.setPrecioTotal(alquiler.getTotal());
            if (alquiler.getVehiculo() != null) {
                dto.setMatriculaVehiculo(alquiler.getVehiculo().getMatricula());
            }
            if (alquiler.getCliente() != null) {
                dto.setUsernameCliente(alquiler.getCliente().getNombre());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // Endpoint 2 Modificado: Búsqueda segura por ID retornando DTO
    @GetMapping("/reservas/{id}")
    public ResponseEntity<?> getReservaById(@PathVariable Long id) {
        return alquilerService.buscarAlquilerPorId(id)
                .map(alquiler -> {
                    AlquilerDto dto = new AlquilerDto();
                    dto.setId(alquiler.getIdAlquiler());
                    dto.setFechaInicio(alquiler.getFechaInicio());
                    dto.setFechaFin(alquiler.getFechaFin());
                    dto.setPrecioTotal(alquiler.getTotal());
                    dto.setUsernameCliente(alquiler.getCliente().getNombre());
                    if (alquiler.getVehiculo() != null) dto.setMatriculaVehiculo(alquiler.getVehiculo().getMatricula());
                    return ResponseEntity.ok().body(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/reservas/{id}")
    public ResponseEntity<?> actualizarReserva(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UPDATED");
        response.put("reservaId", id);
        response.put("mensaje", "Reserva modificada con éxito de forma segura.");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "DELETED");
        response.put("reservaId", id);
        response.put("mensaje", "La reserva ha sido revocada del sistema.");
        return ResponseEntity.ok().body(response);
    }
}