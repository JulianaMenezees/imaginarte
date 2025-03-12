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
    public String showUsuarios(@RequestParam(value = "search", required = false, defaultValue = "") String search, Model model) {
        List<UsuarioAdmin> usuarios;

        if (search.isEmpty()) {
            // Se o campo de pesquisa estiver vazio, mostra todos os usuários
            usuarios = repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        } else {
            // Caso contrário, faz a busca por nome
            usuarios = repo.findByNomeContainingIgnoreCase(search);
        }

        // Adiciona os dados ao modelo
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("search", search);  // Adiciona o valor da pesquisa ao modelo
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

        // Verifica se já existe um usuário com o mesmo CPF
        if (repo.existsByCpf(usuarioAdminDto.getCpf())) {
            result.rejectValue("cpf", "cpf.existente", "Este CPF já está cadastrado.");
            return "UsuariosAdmin/cadastroUsuarioAdmin";
        }

        // Verifica se já existe um usuário com o mesmo e-mail
        if (repo.existsByEmail(usuarioAdminDto.getEmail())) {
            result.rejectValue("email", "email.existente", "Este e-mail já está cadastrado.");
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

        // Limpa a formatação do CPF (remove pontos e hífen)
        String cpfSemFormatacao = usuarioAdminDto.getCpf().replaceAll("\\D", "");
        usuarioAdmin.setCpf(cpfSemFormatacao);

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
