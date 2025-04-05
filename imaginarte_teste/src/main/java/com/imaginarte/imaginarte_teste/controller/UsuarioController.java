package com.imaginarte.imaginarte_teste.controller;


import com.imaginarte.imaginarte_teste.Repository.UsuarioRepository;
import com.imaginarte.imaginarte_teste.model.DTO.UsuarioDto;
import com.imaginarte.imaginarte_teste.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/cadastroUsuario")
    public String showCadastroUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioDto());
        return "Usuario/cadastroUsuario";
    }

    @PostMapping("/cadastroUsuario")
    public String cadastrarUsuario(@Valid @ModelAttribute UsuarioDto usuarioDto,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            return "Usuario/cadastroUsuario";
        }

        //Verificação de CPF
        Usuario usuarioExistente = repo.findByCpf(usuarioDto.getCpf().replaceAll("\\D", ""));
        if(usuarioExistente != null && (usuarioDto.getId() == 0 || usuarioExistente.getId() != usuarioDto.getId())) {
            result.rejectValue("cpf", "cpf.existente", "Este CPF já está cadastrado");
            return "Usuario/cadastroUsuario";
        }

        Usuario usuario = new Usuario();

        usuario.setNome(usuarioDto.getNome());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioDto.getSenha())); // Recomendado criptografar a senha
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

        // Salvar no banco
        repo.save(usuario);

        // Mensagem de sucesso
        redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso!");

        return "redirect:/login"; // Ou outra rota, como página de sucesso
    }

    }




