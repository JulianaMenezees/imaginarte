package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.Repository.EnderecoEntregaRepository;
import com.imaginarte.imaginarte_teste.Repository.PedidoRepository;
import com.imaginarte.imaginarte_teste.Repository.UsuarioRepository;
import com.imaginarte.imaginarte_teste.model.DTO.UsuarioDto;
import com.imaginarte.imaginarte_teste.model.DTO.UsuarioEdicaoDto;
import com.imaginarte.imaginarte_teste.model.EnderecoEntrega;
import com.imaginarte.imaginarte_teste.model.Pedido;
import com.imaginarte.imaginarte_teste.model.Usuario;
import com.imaginarte.imaginarte_teste.service.PedidoService;
import com.imaginarte.imaginarte_teste.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PedidoService pedidoService;

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

        return "redirect:/produtos/index";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Encerra a sessão
        return "redirect:/produtos/index";
    }

    @GetMapping("/meusdados")
    public String mostrarDadosUsuario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/usuario/login";
        }

        // Garante o carregamento dos endereços (se estiver usando Fetch.LAZY)
        usuario.setEnderecosEntrega(enderecoEntregaRepo.findByUsuarioId(usuario.getId()));

        model.addAttribute("usuario", usuario);
        return "Usuario/dadosUsuario";
    }


    @GetMapping("/editar")
    public String editarDadosUsuario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/usuario/login";

        UsuarioEdicaoDto dto = new UsuarioEdicaoDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setDataNascimento(usuario.getDataNascimento());
        dto.setGenero(usuario.getGenero());

        model.addAttribute("usuario", dto);
        return "Usuario/EditarDados";
    }

    @PostMapping("/editar")
    public String salvarEdicaoUsuario(@Valid @ModelAttribute("usuario") UsuarioEdicaoDto usuarioDto,
                                      BindingResult result,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) return "Usuario/EditarDados";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/usuario/login";

        usuario.setNome(usuarioDto.getNome());
        usuario.setDataNascimento(usuarioDto.getDataNascimento());
        usuario.setGenero(usuarioDto.getGenero());

        if (usuarioDto.getSenha() != null && !usuarioDto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));
        }

        repo.save(usuario);
        session.setAttribute("usuarioLogado", usuario);

        redirectAttributes.addFlashAttribute("mensagem", "Dados atualizados com sucesso!");
        return "redirect:/usuario/meusdados";
    }

    @Autowired
    private EnderecoEntregaRepository enderecoEntregaRepo;

    @GetMapping("/novo-endereco")
    public String novoEnderecoEntrega(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/usuario/login";

        EnderecoEntrega endereco = new EnderecoEntrega();
        model.addAttribute("endereco", endereco);
        return "Usuario/NovoEnderecoEntrega";
    }

    @PostMapping("/novo-endereco")
    public String salvarEnderecoEntrega(@ModelAttribute("endereco") EnderecoEntrega endereco,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/usuario/login";

        if (endereco.isPadrao()) {
            List<EnderecoEntrega> existentes = enderecoEntregaRepo.findByUsuarioId(usuario.getId());
            for (EnderecoEntrega e : existentes) {
                e.setPadrao(false);
            }
            enderecoEntregaRepo.saveAll(existentes);
        }

        endereco.setUsuario(usuario);
        enderecoEntregaRepo.save(endereco);

        redirectAttributes.addFlashAttribute("mensagem", "Endereço adicionado com sucesso!");
        return "redirect:/usuario/meusdados";
    }

    @PostMapping("/endereco/padrao/{id}")
    public String definirEnderecoPadrao(@PathVariable("id") Long id,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/usuario/login";

        List<EnderecoEntrega> enderecos = enderecoEntregaRepo.findByUsuarioId(usuario.getId());
        for (EnderecoEntrega e : enderecos) {
            e.setPadrao(e.getId().equals(id)); // Marca apenas o selecionado como padrão
        }
        enderecoEntregaRepo.saveAll(enderecos);

        redirectAttributes.addFlashAttribute("mensagem", "Endereço padrão atualizado.");
        return "redirect:/usuario/meusdados";
    }

    @GetMapping("/pedidos")
    public String listarPedidos(Model model) {
        // Alterando para tratar o Optional<Usuario> corretamente
        Usuario usuario = usuarioService.getUsuarioLogado()
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Busca os pedidos do usuário
        List<Pedido> pedidos = pedidoService.buscarPedidosPorUsuario(usuario);

        // Adiciona a lista de pedidos no modelo
        model.addAttribute("pedidos", pedidos);

        // Retorna o template "usuario/pedidos"
        return "Usuario/pedidos";
    }
}
