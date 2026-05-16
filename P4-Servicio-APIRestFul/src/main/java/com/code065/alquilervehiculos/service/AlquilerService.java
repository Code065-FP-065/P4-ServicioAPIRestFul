package com.code065.alquilervehiculos.service;

import com.code065.alquilervehiculos.model.Alquiler;

import java.util.List;
import java.util.Optional;

public interface AlquilerService {

    List<Alquiler> listarAlquileres();

    Optional<Alquiler> buscarAlquilerPorId(Long id);

    Alquiler guardarAlquiler(Alquiler alquiler);

    void eliminarAlquiler(Long id);

    List<Alquiler> listarAlquileresPorUsernameCreador(String username);
}
