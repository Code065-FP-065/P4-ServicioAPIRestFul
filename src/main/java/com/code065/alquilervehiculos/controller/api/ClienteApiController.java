package com.code065.alquilervehiculos.controller.api;

import com.code065.alquilervehiculos.dto.ClienteResponseDTO;
import com.code065.alquilervehiculos.service.ClienteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteApiController {

    private final ClienteService clienteService;

    public ClienteApiController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteResponseDTO> listarClientes() {

        return clienteService.listaClientes()
                .stream()
                .map(cliente -> new ClienteResponseDTO(
                        cliente.getIdCliente(),
                        cliente.getNombre(),
                        cliente.getApellidos(),
                        cliente.getDni(),
                        cliente.getTelefono()
                ))
                .toList();
    }
}
