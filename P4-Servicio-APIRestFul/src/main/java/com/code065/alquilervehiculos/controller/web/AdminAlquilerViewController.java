package com.code065.alquilervehiculos.controller.web;

import com.code065.alquilervehiculos.model.*;
import com.code065.alquilervehiculos.security.UsuarioAutenticadoService;
import com.code065.alquilervehiculos.service.AlquilerService;
import com.code065.alquilervehiculos.service.ClienteService;
import com.code065.alquilervehiculos.service.VehiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/admin/alquileres")
public class AdminAlquilerViewController {

    private final AlquilerService alquilerService;
    private final ClienteService clienteService;
    private final VehiculoService vehiculoService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public AdminAlquilerViewController(AlquilerService alquilerService, ClienteService clienteService, VehiculoService vehiculoService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.alquilerService = alquilerService;
        this.clienteService = clienteService;
        this.vehiculoService = vehiculoService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @GetMapping
    public String listarAlquileres(Model model) {
        model.addAttribute("alquileres", alquilerService.listarAlquileres());
        model.addAttribute("paginaActiva", "alquileres");
        model.addAttribute("rutaNuevoAlquiler", "/admin/alquileres/nuevo");
        model.addAttribute("modoAdmin", true);
        return "alquileres/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoAlquiler(Model model) {
        Alquiler alquiler = new Alquiler();
        alquiler.setCliente(new Cliente());
        alquiler.setVehiculo(new Vehiculo());
        cargarDatosFormulario(model);
        model.addAttribute("alquiler", alquiler);
        model.addAttribute("formAction", "/admin/alquileres/guardar");
        model.addAttribute("cancelUrl", "/admin/alquileres");
        model.addAttribute("modoAdmin", true);
        return "alquileres/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarAlquiler(@PathVariable Long id, Model model) {
        Alquiler alquiler = alquilerService.buscarAlquilerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Alquiler no encontrado con id: " + id));

        if (alquiler.getCliente() == null) {
            alquiler.setCliente(new Cliente());
        }

        if (alquiler.getVehiculo() == null) {
            alquiler.setVehiculo(new Vehiculo());
        }

        cargarDatosFormulario(model);
        model.addAttribute("alquiler", alquiler);
        model.addAttribute("formAction", "/admin/alquileres/guardar");
        model.addAttribute("cancelUrl", "/admin/alquileres");
        model.addAttribute("modoAdmin", true);

        return "alquileres/formulario";
    }

    @PostMapping("/guardar")
    public String guardarAlquiler(Alquiler alquiler, Model model) {

        if (alquiler.getFechaInicio() == null || alquiler.getFechaFin() == null) {
            cargarDatosFormulario(model);
            model.addAttribute("alquiler", alquiler);
            model.addAttribute("formAction", "/admin/alquileres/guardar");
            model.addAttribute("cancelUrl", "/admin/alquileres");
            model.addAttribute("modoAdmin", true);
            model.addAttribute("mensajeError", "Debes indicar la fecha de inicio y la fecha de fin.");
            return "alquileres/formulario";
        }

        long diasCalculados = ChronoUnit.DAYS.between(alquiler.getFechaInicio(), alquiler.getFechaFin());

        if (diasCalculados <= 0) {
            cargarDatosFormulario(model);
            model.addAttribute("alquiler", alquiler);
            model.addAttribute("formAction", "/admin/alquileres/guardar");
            model.addAttribute("cancelUrl", "/admin/alquileres");
            model.addAttribute("modoAdmin", true);
            model.addAttribute("mensajeError", "La fecha de fin debe ser posterior a la fecha de inicio.");
            return "alquileres/formulario";
        }

        Cliente cliente = clienteService.buscarClientePorId(alquiler.getCliente().getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        Vehiculo vehiculo = vehiculoService.buscarVehiculoPorId(alquiler.getVehiculo().getIdVehiculo())
                .orElseThrow(() -> new IllegalArgumentException("Vehiculo no encontrado"));

        int dias = (int) diasCalculados;
        BigDecimal precioDiaAplicado = vehiculo.getPrecioDia();
        BigDecimal total = precioDiaAplicado.multiply(BigDecimal.valueOf(dias));

        alquiler.setCliente(cliente);
        alquiler.setVehiculo(vehiculo);
        alquiler.setDias(dias);
        alquiler.setPrecioDiaAplicado(precioDiaAplicado);
        alquiler.setTotal(total);

        Usuario usuarioActual = usuarioAutenticadoService.obtenerUsuarioActual();

        if (alquiler.getIdAlquiler() == null) {
            alquiler.setCreadoPor(usuarioActual);
        } else {
            Alquiler alquilerExistente = alquilerService.buscarAlquilerPorId(alquiler.getIdAlquiler())
                    .orElseThrow(() -> new IllegalArgumentException("Alquiler no encontrado con id: " + alquiler.getIdAlquiler()));

            alquiler.setCreadoPor(alquilerExistente.getCreadoPor());
        }

        alquiler.setModificadoPor(usuarioActual);

        alquilerService.guardarAlquiler(alquiler);
        return "redirect:/admin/alquileres";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarAlquiler(@PathVariable Long id) {
        alquilerService.eliminarAlquiler(id);
        return "redirect:/admin/alquileres";
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("clientes", clienteService.listaClientes());
        model.addAttribute("vehiculos", vehiculoService.listarVehiculos());
        model.addAttribute("estadosAlquiler", EstadoAlquiler.values());
        model.addAttribute("paginaActiva", "alquileres");
    }
}
