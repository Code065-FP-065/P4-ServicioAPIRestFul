package com.code065.alquilervehiculos.service.imp;

import com.code065.alquilervehiculos.model.Rol;
import com.code065.alquilervehiculos.repository.RolRepository;
import com.code065.alquilervehiculos.service.RolService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImp implements RolService {

    private final RolRepository rolRepository;

    public RolServiceImp(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }
    @Override
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> buscarPorId(Long id) {
        return rolRepository.findById(id);
    }

    @Override
    public Optional<Rol> buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
}
