package com.code065.alquilervehiculos.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "alquileres")
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alquiler")
    private Long idAlquiler;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoAlquiler estado;

    @Column (name = "dias", nullable = false)
    private Integer dias;

    @Column (name = "precio_dia_aplicado", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioDiaAplicado;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modificado_por")
    private Usuario modificadoPor;

    public Alquiler() {}

    public Alquiler(LocalDate fechaInicio, LocalDate fechaFin, EstadoAlquiler estado, Integer dias, BigDecimal precioDiaAplicado, BigDecimal total, Cliente cliente, Vehiculo vehiculo) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.dias = dias;
        this.precioDiaAplicado = precioDiaAplicado;
        this.total = total;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
    }

    public Long getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(Long idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoAlquiler getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlquiler estado) {
        this.estado = estado;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public BigDecimal getPrecioDiaAplicado() {
        return precioDiaAplicado;
    }

    public void  setPrecioDiaAplicado(BigDecimal precioDiaAplicado) {
        this.precioDiaAplicado = precioDiaAplicado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Usuario getCreadoPor() {return creadoPor;}

    public void setCreadoPor(Usuario creadoPor) {this.creadoPor = creadoPor;}

    public Usuario getModificadoPor() {return modificadoPor;}

    public void setModificadoPor(Usuario modificadoPor) {this.modificadoPor = modificadoPor;}
}
