package com.code065.alquilervehiculos.dto.api;

import com.code065.alquilervehiculos.model.Cliente;

public record ClienteResponse(Long idCliente, String nombre, String apellidos, String dni, String telefono) {
    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(cliente.getIdCliente(), cliente.getNombre(), cliente.getApellidos(), cliente.getDni(), cliente.getTelefono());
    }
}
