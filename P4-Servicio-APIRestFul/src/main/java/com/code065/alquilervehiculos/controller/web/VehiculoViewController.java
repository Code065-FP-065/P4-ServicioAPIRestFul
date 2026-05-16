package com.code065.alquilervehiculos.controller.web;

import com.code065.alquilervehiculos.model.EstadoVehiculo;
import com.code065.alquilervehiculos.model.Vehiculo;
import com.code065.alquilervehiculos.service.VehiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/vehiculos")
public class VehiculoViewController {

    private final VehiculoService vehiculoService;

    public VehiculoViewController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoService.listarVehiculos());
        model.addAttribute("paginaActiva", "vehiculos");
        return "vehiculos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoVehiculo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("estadosVehiculo", EstadoVehiculo.values());
        model.addAttribute("paginaActiva", "vehiculos");
        return "vehiculos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarFormularioEditarVehiculo(@PathVariable Long id, Model model) {
        Vehiculo vehiculo = vehiculoService.buscarVehiculoPorId(id).orElseThrow(() -> new IllegalArgumentException("Vehiculo no encontrado con id: " + id));
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("estadosVehiculo", EstadoVehiculo.values());
        model.addAttribute("paginaActiva", "vehiculos");
        return "vehiculos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarVehiculo(Vehiculo vehiculo) {
        vehiculoService.guardarVehiculo(vehiculo);
        return "redirect:/admin/vehiculos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vehiculoService.eliminarVehiculo(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Vehículo eliminado correctamente.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/admin/vehiculos";
    }
}
