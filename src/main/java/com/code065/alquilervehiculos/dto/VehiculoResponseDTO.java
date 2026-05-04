package com.code065.alquilervehiculos.dto;

import com.code065.alquilervehiculos.model.EstadoVehiculo;

import java.math.BigDecimal;

public class VehiculoResponseDTO {

    private Long id;
    private String matricula;
    private String marca;
    private String modelo;
    private String tipo;
    private BigDecimal precioDia;
    private EstadoVehiculo estado;

    public VehiculoResponseDTO(Long id, String matricula, String marca, String modelo, String tipo, BigDecimal precioDia, EstadoVehiculo estado) {
        this.id = id;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
        this.precioDia = precioDia;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public BigDecimal getPrecioDia() {
        return precioDia;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }
}
