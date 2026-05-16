package com.code065.alquilervehiculos.service;

import com.code065.alquilervehiculos.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> listarUsuarios();

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorUsername(String username);

    Optional<Usuario> buscarPorEmail(String email);

    boolean existeUsername(String username);

    boolean existeEmail(String email);

    Usuario guardar(Usuario usuario);

    Usuario asignarRolPorNombre(Usuario usuario, String nombreRol);

    Usuario registrarUsuario(String username, String email, String password);

    void activarUsuario(Long id);

    void desactivarUsuario(Long id);

    void cambiarRolUsuario(Long idUsuario, Long idRol);
}
