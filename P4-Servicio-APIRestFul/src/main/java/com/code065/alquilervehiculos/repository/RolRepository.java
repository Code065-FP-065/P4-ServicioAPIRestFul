package com.code065.alquilervehiculos.repository;

import com.code065.alquilervehiculos.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(String rolName);
}
