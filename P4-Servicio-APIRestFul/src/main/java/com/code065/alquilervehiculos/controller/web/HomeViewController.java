package com.code065.alquilervehiculos.controller.web;

import com.code065.alquilervehiculos.service.AlquilerService;
import com.code065.alquilervehiculos.service.ClienteService;
import com.code065.alquilervehiculos.service.VehiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeViewController {
    private final ClienteService clienteService;
    private final VehiculoService vehiculoService;
    private final AlquilerService alquilerService;

    public HomeViewController(ClienteService clienteService,
                              VehiculoService vehiculoService,
                              AlquilerService alquilerService) {
        this.clienteService = clienteService;
        this.vehiculoService = vehiculoService;
        this.alquilerService = alquilerService;
    }

    @GetMapping("/")
    public String mostrarDashboard(Model model) {
        model.addAttribute("totalClientes", clienteService.listaClientes().size());
        model.addAttribute("totalVehiculos", vehiculoService.listarVehiculos().size());
        model.addAttribute("totalAlquileres", alquilerService.listarAlquileres().size());
        model.addAttribute("paginaActiva", "dashboard");
        return "dashboard/index";
    }

    @GetMapping("/dashboard")
    public String mostrarDashboardAlternativo(Model model) {
        model.addAttribute("totalClientes", clienteService.listaClientes().size());
        model.addAttribute("totalVehiculos", vehiculoService.listarVehiculos().size());
        model.addAttribute("totalAlquileres", alquilerService.listarAlquileres().size());
        model.addAttribute("paginaActiva", "dashboard");
        return "dashboard/index";
    }
}
