package com.code065.alquilervehiculos.controller.api;

import com.code065.alquilervehiculos.dto.api.ClienteRequest;
import com.code065.alquilervehiculos.dto.api.ClienteResponse;
import com.code065.alquilervehiculos.model.Cliente;
import com.code065.alquilervehiculos.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Clientes API")
public class ClienteApiController {

    private final ClienteService clienteService;

    public ClienteApiController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/api/clientes")
    @Operation(summary = "Endpoint público: lista todos los clientes")
    public List<ClienteResponse> listarClientes() {
        return clienteService.listaClientes().stream().map(ClienteResponse::from).toList();
    }

    @GetMapping("/api/secure/clientes/{id}")
    @Operation(summary = "Endpoint protegido: obtiene un cliente por id")
    public ClienteResponse obtenerCliente(@PathVariable Long id) {
        return clienteService.buscarClientePorId(id).map(ClienteResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + id));
    }

    @PostMapping("/api/secure/clientes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Endpoint protegido: crea un cliente")
    public ClienteResponse crearCliente(@Valid @RequestBody ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.nombre());
        cliente.setApellidos(request.apellidos());
        cliente.setDni(request.dni());
        cliente.setTelefono(request.telefono());
        return ClienteResponse.from(clienteService.guardarCliente(cliente));
    }
}
