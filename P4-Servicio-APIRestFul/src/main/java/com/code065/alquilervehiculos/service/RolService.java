package com.code065.alquilervehiculos.service;

import com.code065.alquilervehiculos.model.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {

    List<Rol> listarRoles();

    Optional<Rol> buscarPorId(Long id);

    Optional<Rol> buscarPorNombre(String nombre);
}
