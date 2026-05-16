package com.code065.alquilervehiculos.controller.web;

import com.code065.alquilervehiculos.dto.RegistroUsuarioDto;
import com.code065.alquilervehiculos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthViewController {

    private final UsuarioService usuarioService;

    public AuthViewController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("paginaActiva", "login");
        return "auth/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("registroUsuarioDto", new RegistroUsuarioDto());
        model.addAttribute("paginaActiva", "registro");
        return "auth/registro";

    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @Valid @ModelAttribute("registroUsuarioDto") RegistroUsuarioDto registroUsuarioDto,
            BindingResult bindingResult,
            Model model
    ) {
        model.addAttribute("paginaActiva", "registro");

        if (!registroUsuarioDto.getPassword().equals(registroUsuarioDto.getConfirmPassword())) {
            bindingResult.rejectValue(
                    "confirmPassword",
                    "error.confirmPassword",
                    "Las contraseñas no coinciden."
            );
        }

        if (bindingResult.hasErrors()) {
            return "auth/registro";
        }

        try {
            usuarioService.registrarUsuario(
                    registroUsuarioDto.getUsername(),
                    registroUsuarioDto.getEmail(),
                    registroUsuarioDto.getPassword()
            );

        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("mensajeError", e.getMessage());
            return "auth/registro";

        }

        return "redirect:/login?registroOk";

    }
}
