package com.code065.alquilervehiculos.controller.web;

import com.code065.alquilervehiculos.service.RolService;
import com.code065.alquilervehiculos.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioVeiwController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    public AdminUsuarioVeiwController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("roles", rolService.listarRoles());
        model.addAttribute("paginaActiva", "usuarios");
        return "usuarios/lista";
    }

    @PostMapping("/activar/{id}")
    public String activarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.activarUsuario(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Usuario activado correctamente.");
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/desactivar/{id}")
    public String desactivarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.desactivarUsuario(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Usuario desactivado correctamente.");
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/cambiar-rol/{id}")
    public String cambiarRolUsuario(
            @PathVariable Long id,
            @RequestParam Long idRol,
            RedirectAttributes redirectAttributes
    ) {
        usuarioService.cambiarRolUsuario(id, idRol);
        redirectAttributes.addFlashAttribute("mensajeExito", "Rol actualizado correctamente.");
        return "redirect:/admin/usuarios";
    }
}
