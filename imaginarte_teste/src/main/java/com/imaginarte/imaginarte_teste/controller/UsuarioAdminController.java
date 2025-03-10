package com.imaginarte.imaginarte_teste.controller;


import com.imaginarte.imaginarte_teste.Services.UsuariosRepository;
import com.imaginarte.imaginarte_teste.model.DTO.UsuarioAdminDto;
import com.imaginarte.imaginarte_teste.model.UsuarioAdmin;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioAdminController {

    @Autowired
    private UsuariosRepository repo;

    @GetMapping({"", "/"})
    public String showUsuarios(Model model) {
        List<UsuarioAdmin> usuarioAdmin = repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("usuarios", usuarioAdmin);
        return "UsuariosAdmin/AllUsuariosAdmin";
    }

    @GetMapping("/cadastro")
    public String showCreateUsuario(Model model) {
        UsuarioAdminDto usuarioAdminDto = new UsuarioAdminDto();
        model.addAttribute("usuarioAdminDto", usuarioAdminDto);
        return "UsuariosAdmin/cadastroUsuarioAdmin";
    }

    @PostMapping("/cadastro")
    public String criarUsuario(@Valid @ModelAttribute UsuarioAdminDto usuarioAdminDto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "UsuariosAdmin/cadastroUsuarioAdmin";
        }

        UsuarioAdmin usuarioAdmin;

        if (usuarioAdminDto.getId() != 0) {
            // Busca usuário no banco para atualização
            usuarioAdmin = repo.findById(usuarioAdminDto.getId()).orElse(new UsuarioAdmin());
        } else {
            // Criando um novo usuário
            usuarioAdmin = new UsuarioAdmin();
        }

        // Atualiza os dados do usuário
        usuarioAdmin.setNome(usuarioAdminDto.getNome());
        usuarioAdmin.setEmail(usuarioAdminDto.getEmail());
        usuarioAdmin.setSenha(usuarioAdminDto.getSenha());
        usuarioAdmin.setCpf(usuarioAdminDto.getCpf());
        usuarioAdmin.setGrupo(usuarioAdminDto.getGrupo());

        // Se for uma criação, define situação como ativa
        if (usuarioAdmin.getId() == 0) {
            usuarioAdmin.setSituacao(true);
        }

        repo.save(usuarioAdmin);
        redirectAttributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");

        return "redirect:/usuarios";
    }


    @GetMapping("/editar-usuario")
    public String showEditarUsuario(@RequestParam int id, Model model) {
        try {
            UsuarioAdmin usuarioAdmin = repo.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            UsuarioAdminDto usuarioAdminDto = new UsuarioAdminDto();
            usuarioAdminDto.setId(usuarioAdmin.getId()); // Preenche o ID para edição
            usuarioAdminDto.setNome(usuarioAdmin.getNome());
            usuarioAdminDto.setEmail(usuarioAdmin.getEmail());
            usuarioAdminDto.setSenha(usuarioAdmin.getSenha());
            usuarioAdminDto.setCpf(usuarioAdmin.getCpf());
            usuarioAdminDto.setGrupo(usuarioAdmin.getGrupo());

            model.addAttribute("usuarioAdminDto", usuarioAdminDto);
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return "redirect:/usuarios";
        }
        return "UsuariosAdmin/cadastroUsuarioAdmin";
    }

}


