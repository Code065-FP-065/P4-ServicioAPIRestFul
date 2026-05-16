package com.code065.alquilervehiculos.service.imp;

import com.code065.alquilervehiculos.model.Rol;
import com.code065.alquilervehiculos.model.Usuario;
import com.code065.alquilervehiculos.repository.RolRepository;
import com.code065.alquilervehiculos.repository.UsuarioRepository;
import com.code065.alquilervehiculos.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImp implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImp(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario asignarRolPorNombre(Usuario usuario, String nombreRol) {
        Rol rol = rolRepository.findByNombre(nombreRol).orElseThrow(() -> new IllegalArgumentException("No existe el rol:" + nombreRol));
        usuario.setRol(rol);
        return usuario;
    }

    @Override

    public Usuario registrarUsuario(String username, String email, String password) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese nombre.");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email.");
        }

        Rol rolUser = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new IllegalStateException("No existe el rol USER en la base de datos."));

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordEncoder.encode(password));
        usuario.setEnabled(true);
        usuario.setRol(rolUser);

        return usuarioRepository.save(usuario);
    }

    @Override
    public void activarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));
        usuario.setEnabled(true);
        usuarioRepository.save(usuario);
    }

    @Override
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));
        usuario.setEnabled(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public void cambiarRolUsuario(Long idUsuario, Long idRol) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + idUsuario));

        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con id: " + idRol));

        usuario.setRol(rol);
        usuarioRepository.save(usuario);
    }
}
