package com.code065.alquilervehiculos.controller.api;

import com.code065.alquilervehiculos.dto.api.ClienteResponse;
import com.code065.alquilervehiculos.model.Cliente;
import com.code065.alquilervehiculos.service.ClienteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Clientes")
@SecurityRequirement(name = "bearerAuth")
public class ClienteApiController {

    private final ClienteService clienteService;

    public ClienteApiController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes")
    public List<ClienteResponse> listarClientes() {
        return clienteService.listaClientes().stream()
                .map(this::toResponse)
                .toList();
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getApellidos(),
                cliente.getDni(),
                cliente.getTelefono(),
                cliente.getCreatedAt()
        );
    }
}
