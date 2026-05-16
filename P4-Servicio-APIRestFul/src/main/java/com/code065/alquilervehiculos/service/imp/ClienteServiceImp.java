package com.code065.alquilervehiculos.service.imp;

import com.code065.alquilervehiculos.model.Cliente;
import com.code065.alquilervehiculos.repository.AlquilerRepository;
import com.code065.alquilervehiculos.repository.ClienteRepository;
import com.code065.alquilervehiculos.service.ClienteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImp implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final AlquilerRepository alquilerRepository;

    public ClienteServiceImp(ClienteRepository clienteRepository, AlquilerRepository alquilerRepository) {
        this.clienteRepository = clienteRepository;
        this.alquilerRepository = alquilerRepository;
    }

    @Override
    public List<Cliente> listaClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminarCliente(Long id) {
        if (alquilerRepository.existsByCliente_IdCliente(id)) {
            throw new IllegalStateException("No se puede eliminar el cliente porque tiene alquileres asociados.");
        }
        clienteRepository.deleteById(id);
    }
}
