package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.Repository.UsuarioRepository;
import com.imaginarte.imaginarte_teste.model.DTO.UsuarioDto;
import com.imaginarte.imaginarte_teste.model.Usuario;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Exibe o formulário de cadastro
    @GetMapping("/cadastroUsuario")
    public String showCadastroUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioDto());
        return "Usuario/cadastroUsuario";
    }

    // Processa o cadastro
    @PostMapping("/cadastroUsuario")
    public String cadastrarUsuario(@Valid @ModelAttribute UsuarioDto usuarioDto,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "Usuario/cadastroUsuario";
        }

        // Verifica se CPF já existe
        Usuario usuarioExistenteCpf = repo.findByCpf(usuarioDto.getCpf().replaceAll("\\D", ""));
        if (usuarioExistenteCpf != null &&
                (usuarioDto.getId() == 0 || usuarioExistenteCpf.getId() != usuarioDto.getId())) {
            result.rejectValue("cpf", "cpf.existente", "Este CPF já está cadastrado");
            return "Usuario/cadastroUsuario";
        }

        // Verifica se email já existe
        Usuario usuarioExistenteEmail = repo.findByEmail(usuarioDto.getEmail());
        if (usuarioExistenteEmail != null &&
                (usuarioDto.getId() == 0 || usuarioExistenteEmail.getId() != usuarioDto.getId())) {
            result.rejectValue("email", "email.existente", "Este email já está cadastrado");
            return "Usuario/cadastroUsuario";
        }

        // Cria novo usuário
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDto.getNome());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));
        usuario.setCpf(usuarioDto.getCpf().replaceAll("\\D", ""));
        usuario.setDataNascimento(usuarioDto.getDataNascimento());
        usuario.setGenero(usuarioDto.getGenero());

        // Endereço
        usuario.setCep(usuarioDto.getCep());
        usuario.setLogradouro(usuarioDto.getLogradouro());
        usuario.setNumero(usuarioDto.getNumero());
        usuario.setComplemento(usuarioDto.getComplemento());
        usuario.setBairro(usuarioDto.getBairro());
        usuario.setCidade(usuarioDto.getCidade());
        usuario.setUf(usuarioDto.getUf());

        // Salva no banco
        repo.save(usuario);

        // Redireciona para login com mensagem de sucesso
        redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso!");
        return "redirect:/usuario/login";
    }

    // Exibe a tela de login do usuário comum
    @GetMapping("/login")
    public String exibirLoginUsuario() {
        return "Usuario/Login"; // Caminho correto com base na pasta
    }

    @PostMapping("/login")
    public String loginUsuario(@RequestParam("email") String email,
                               @RequestParam("senha") String senha,
                               Model model,
                               HttpSession session) {

        Usuario usuario = repo.findByEmail(email);

        if (usuario == null || !passwordEncoder.matches(senha, usuario.getSenha())) {
            model.addAttribute("erro", "Email ou senha inválidos");
            return "Usuario/Login";
        }

        // Armazena o usuário logado na sessão
        session.setAttribute("usuarioLogado", usuario);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Encerra a sessão
        return "redirect:/";
    }

    @GetMapping("/meusdados")
    public String mostrarDadosUsuario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/usuario/login"; // redireciona se não estiver logado
        }

        model.addAttribute("usuario", usuario);
        return "Usuario/DadosUsuario";
    }
}
