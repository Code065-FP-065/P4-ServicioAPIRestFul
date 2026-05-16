package com.code065.alquilervehiculos.service;

import com.code065.alquilervehiculos.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    List<Cliente> listaClientes();

    Optional<Cliente> buscarClientePorId(Long id);

    Cliente guardarCliente(Cliente cliente);

    void eliminarCliente(Long id);
}
