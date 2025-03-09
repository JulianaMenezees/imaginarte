package com.imaginarte.imaginarte_teste.controller;


import com.imaginarte.imaginarte_teste.Services.UsuariosRepository;
import com.imaginarte.imaginarte_teste.model.DTO.UsuarioAdminDto;
import com.imaginarte.imaginarte_teste.model.UsuarioAdmin;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioAdminController {

    @Autowired
    private UsuariosRepository repo;

    @GetMapping({"", "/"})
    public String showUsuarios(Model model) {
        List<UsuarioAdmin> usuarioAdmin = repo.findAll();
        model.addAttribute("usuarios", usuarioAdmin);
        return "usuario/index";
    }

    @GetMapping("cadastro")
    public String showCreateUsuario(Model model) {
        model.addAttribute("usuarioAdminDto", new UsuarioAdminDto());
        return "usuario/cadastro";
    }

    @PostMapping("cadastro")
    public String criarUsuario(@Valid @ModelAttribute UsuarioAdminDto usuarioAdminDto, BindingResult result) {

        if (result.hasErrors()) {
            return "usuario/cadastro";
        }

        UsuarioAdmin usuarioAdmin = convertDtoToEntity(usuarioAdminDto);
        repo.save(usuarioAdmin);
        return "redirect:/usuarios";
    }

    private UsuarioAdmin convertDtoToEntity(UsuarioAdminDto usuarioAdminDto) {
        UsuarioAdmin usuarioAdmin = new UsuarioAdmin();
        usuarioAdmin.setNome(usuarioAdminDto.getNome());
        usuarioAdmin.setEmail(usuarioAdminDto.getEmail());
        usuarioAdmin.setSenha(usuarioAdminDto.getSenha());
        usuarioAdmin.setCpf(usuarioAdminDto.getCpf());
        usuarioAdmin.setGrupo(usuarioAdminDto.getGrupo());
        return usuarioAdmin;
    }
}
