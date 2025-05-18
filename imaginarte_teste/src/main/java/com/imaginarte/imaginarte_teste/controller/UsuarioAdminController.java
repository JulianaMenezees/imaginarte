package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.Repository.UsuariosRepository;
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
import java.util.Map;

@Controller
@RequestMapping("/usuarios")
public class UsuarioAdminController {

    @Autowired
    private UsuariosRepository repo;

    @GetMapping({"", "/"})
    public String showUsuarios(@RequestParam(value = "search", required = false, defaultValue = "") String search, Model model) {
        List<UsuarioAdmin> usuarios;

        if (search.isEmpty()) {
            usuarios = repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        } else {
            usuarios = repo.findByNomeContainingIgnoreCase(search);
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("search", search);
        return "UsuariosAdmin/AllUsuariosAdmin";
    }

    @GetMapping("/cadastro")
    public String showCreateUsuario(Model model) {
        UsuarioAdminDto usuarioAdminDto = new UsuarioAdminDto();
        model.addAttribute("usuarioAdminDto", usuarioAdminDto);
        return "UsuariosAdmin/cadastroUsuarioAdmin";
    }

    @PostMapping("/cadastro")
    public String criarOuAtualizarUsuario(@Valid @ModelAttribute UsuarioAdminDto usuarioAdminDto,
                                          BindingResult result,
                                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "UsuariosAdmin/cadastroUsuarioAdmin";
        }

        // Verifica se o CPF já está sendo utilizado por outro usuário
        UsuarioAdmin usuarioExistente = repo.findByCpf(usuarioAdminDto.getCpf().replaceAll("\\D", ""));
        if (usuarioExistente != null && (usuarioAdminDto.getId() == 0 || usuarioExistente.getId() != usuarioAdminDto.getId())) {
            result.rejectValue("cpf", "cpf.existente", "Este CPF já está cadastrado.");
            return "UsuariosAdmin/cadastroUsuarioAdmin";
        }

        UsuarioAdmin usuarioAdmin;

        if (usuarioAdminDto.getId() != 0) {
            // Editando um usuário existente
            usuarioAdmin = repo.findById(usuarioAdminDto.getId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // ⚠️ Não altera o e-mail ao editar
        } else {
            // Criando um novo usuário
            usuarioAdmin = new UsuarioAdmin();
            usuarioAdmin.setSituacao(true); // Ativo por padrão
            usuarioAdmin.setEmail(usuarioAdminDto.getEmail()); // Só define o email se for novo
        }


        // Atualiza os dados
        usuarioAdmin.setNome(usuarioAdminDto.getNome());
        usuarioAdmin.setEmail(usuarioAdminDto.getEmail());

        // Atualiza a senha se houver uma nova
        if (!usuarioAdminDto.getSenha().isEmpty()) {
            usuarioAdmin.setSenha(usuarioAdminDto.getSenha());
        }

        usuarioAdmin.setCpf(usuarioAdminDto.getCpf().replaceAll("\\D", ""));
        usuarioAdmin.setGrupo(usuarioAdminDto.getGrupo());

        repo.save(usuarioAdmin);
        redirectAttributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");

        return "redirect:/usuarios";
    }

    @GetMapping("/editar-usuario")
    public String showEditarUsuario(@RequestParam int id, Model model) {
        try {
            UsuarioAdmin usuarioAdmin = repo.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            UsuarioAdminDto usuarioAdminDto = new UsuarioAdminDto();
            usuarioAdminDto.setId(usuarioAdmin.getId());
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

    @PutMapping("/alterarSituacao/{id}")
    @ResponseBody
    public Map<String, String> alterarSituacao(@PathVariable int id, @RequestBody Map<String, Boolean> payload) {
        UsuarioAdmin usuario = repo.findById(id).orElse(null);

        if (usuario == null) {
            return Map.of("erro", "Usuário não encontrado");
        }

        usuario.setSituacao(payload.get("situacao"));
        repo.save(usuario);

        return Map.of("mensagem", "Situação do usuário alterada com sucesso!");
    }
}
