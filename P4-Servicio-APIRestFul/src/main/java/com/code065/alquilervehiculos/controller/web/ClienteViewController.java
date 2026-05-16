package com.code065.alquilervehiculos.controller.web;

import com.code065.alquilervehiculos.model.Cliente;
import com.code065.alquilervehiculos.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/clientes")
public class ClienteViewController {

    private final ClienteService clienteService;

    public ClienteViewController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listaClientes());
        model.addAttribute("paginaActiva", "clientes");
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("paginaActiva", "clientes");
        return "clientes/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarClientePorId(id).orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + id));
        model.addAttribute("cliente", cliente);
        model.addAttribute("paginaActiva", "clientes");
        return "clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCliente(Cliente cliente) {
        clienteService.guardarCliente(cliente);
        return "redirect:/admin/clientes";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminarCliente(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Cliente eliminado correctamente.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/admin/clientes";
    }
}
