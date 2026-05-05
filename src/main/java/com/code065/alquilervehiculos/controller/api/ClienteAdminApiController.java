package com.code065.alquilervehiculos.controller.api;

import com.code065.alquilervehiculos.dto.ClienteRequestDTO;
import com.code065.alquilervehiculos.dto.ClienteResponseDTO;
import com.code065.alquilervehiculos.model.Cliente;
import com.code065.alquilervehiculos.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/clientes")
@Tag(name = "Clientes Admin API", description = "Endpoints protegidos para gestionar clientes")
public class ClienteAdminApiController {

    private final ClienteService clienteService;

    public ClienteAdminApiController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @Operation(summary = "Listar clientes admin", description = "Devuelve todos los clientes. Requiere rol ADMIN.")
    public List<ClienteResponseDTO> listarClientes() {
        return clienteService.listaClientes()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear cliente", description = "Crea un cliente desde la API. Requiere rol ADMIN.")
    public ClienteResponseDTO crearCliente(@RequestBody ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteRequestDTO.getNombre());
        cliente.setApellidos(clienteRequestDTO.getApellidos());
        cliente.setDni(clienteRequestDTO.getDni());
        cliente.setTelefono(clienteRequestDTO.getTelefono());

        Cliente clienteGuardado = clienteService.guardarCliente(cliente);

        return convertirADTO(clienteGuardado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente por ID. Requiere rol ADMIN.")
    public void eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }

    private ClienteResponseDTO convertirADTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getApellidos(),
                cliente.getDni(),
                cliente.getTelefono()
        );

    }
}
