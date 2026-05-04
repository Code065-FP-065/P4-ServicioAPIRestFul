package com.code065.alquilervehiculos.dto;

public class ClienteResponseDTO {

    private Long id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String telefono;

    public ClienteResponseDTO(Long id, String nombre, String apellidos, String dni, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
    }

    public Long getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getTelefono() {
        return telefono;
    }
}
