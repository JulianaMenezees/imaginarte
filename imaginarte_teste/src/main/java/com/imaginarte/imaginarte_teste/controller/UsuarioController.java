package com.imaginarte.imaginarte_teste.controller;


import com.imaginarte.imaginarte_teste.model.DTO.UsuarioDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @GetMapping("/cadastroUsuario")
    public String showCadastroUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioDto());
        return "Usuario/cadastroUsuario";
    }




}
