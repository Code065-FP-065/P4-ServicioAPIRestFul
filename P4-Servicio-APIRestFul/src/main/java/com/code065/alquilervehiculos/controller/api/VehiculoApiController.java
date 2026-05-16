package com.code065.alquilervehiculos.controller.api;

import com.code065.alquilervehiculos.dto.api.VehiculoRequest;
import com.code065.alquilervehiculos.dto.api.VehiculoResponse;
import com.code065.alquilervehiculos.model.Vehiculo;
import com.code065.alquilervehiculos.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Vehículos API")
public class VehiculoApiController {

    private final VehiculoService vehiculoService;

    public VehiculoApiController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping("/api/vehiculos")
    @Operation(summary = "Endpoint público: lista todos los vehículos")
    public List<VehiculoResponse> listarVehiculos() {
        return vehiculoService.listarVehiculos().stream().map(VehiculoResponse::from).toList();
    }

    @GetMapping("/api/vehiculos/{id}")
    @Operation(summary = "Endpoint protegido: obtiene un vehículo por id")
    public VehiculoResponse obtenerVehiculo(@PathVariable Long id) {
        return vehiculoService.buscarVehiculoPorId(id).map(VehiculoResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con id: " + id));
    }

    @PostMapping("/api/secure/vehiculos")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Endpoint protegido: crea un vehículo")
    public VehiculoResponse crearVehiculo(@Valid @RequestBody VehiculoRequest request) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula(request.matricula());
        vehiculo.setMarca(request.marca());
        vehiculo.setModelo(request.modelo());
        vehiculo.setTipo(request.tipo());
        vehiculo.setPrecioDia(request.precioDia());
        vehiculo.setEstado(request.estado());
        return VehiculoResponse.from(vehiculoService.guardarVehiculo(vehiculo));
    }
}
