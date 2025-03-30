package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.Repository.ProdutosRepository;
import com.imaginarte.imaginarte_teste.model.DTO.ProdutoAdminDto;
import com.imaginarte.imaginarte_teste.model.Imagem;
import com.imaginarte.imaginarte_teste.model.ProdutoAdmin;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutosRepository repository;

    @GetMapping({"", "/"})
    public String showProdutos(@RequestParam(value = "search", required = false, defaultValue = "") String search, Model model) {
        List<ProdutoAdmin> produtos;

        if (search.isEmpty()) {
            produtos = repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        } else {
            produtos = repository.findByNomeContainingIgnoreCase(search);
        }

        model.addAttribute("produtos", produtos);
        model.addAttribute("search", search);
        return "ProdutosAdmin/AllProdutosAdmin";
    }

    @GetMapping("/cadastroProduto")
    public String showCreateProduto(Model model) {
        ProdutoAdminDto produtoAdminDto = new ProdutoAdminDto();
        model.addAttribute("produtoAdminDto", produtoAdminDto);
        return "ProdutosAdmin/cadastroProdutoAdmin";
    }

    @PostMapping("/cadastroProduto")
    public String criarOuAtualizarProduto(@Valid @ModelAttribute ProdutoAdminDto produtoAdminDto,
                                          BindingResult result,
                                          RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            return "ProdutosAdmin/cadastroProdutoAdmin";
        }

        ProdutoAdmin produtoAdmin;

        if (produtoAdminDto.getId() != 0) {
            produtoAdmin = repository.findById(produtoAdminDto.getId()).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        } else {
            produtoAdmin = new ProdutoAdmin();
            produtoAdmin.setSituacao(true);
        }

        produtoAdmin.setNome(produtoAdminDto.getNome());
        produtoAdmin.setAvaliacao(produtoAdminDto.getAvaliacao());
        produtoAdmin.setDescricao(produtoAdminDto.getDescricao());
        produtoAdmin.setPreco(produtoAdminDto.getPreco());
        produtoAdmin.setQuantidade(produtoAdminDto.getQuantidade());

        if (produtoAdminDto.getImagens() != null && !produtoAdminDto.getImagens().isEmpty()) {
            String uploadDir = "uploads/";
            List<Imagem> imagensSalvas = new ArrayList<>();

            for (MultipartFile imagem : produtoAdminDto.getImagens()) {
                String fileName = UUID.randomUUID() + "_" + imagem.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);

                // Criar diretório se não existir
                if (!Files.exists(Paths.get(uploadDir))) {
                    Files.createDirectories(Paths.get(uploadDir));
                }

                // Salvar arquivo
                Files.copy(imagem.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Criar entidade de imagem
                Imagem novaImagem = new Imagem();
                novaImagem.setUrl(fileName);
                novaImagem.setProduto(produtoAdmin);
                imagensSalvas.add(novaImagem);
            }

            produtoAdmin.getImagens().addAll(imagensSalvas);
        }

        repository.save(produtoAdmin);
        redirectAttributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");

        return "redirect:/produtos";
    }

    @GetMapping("/editar-produto")
    public String showEditarProduto(@RequestParam int id, Model model) {
        try {
            ProdutoAdmin produtoAdmin = repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            ProdutoAdminDto produtoAdminDto = new ProdutoAdminDto();
            produtoAdminDto.setId(produtoAdmin.getId());
            produtoAdminDto.setNome(produtoAdmin.getNome());
            produtoAdminDto.setAvaliacao(produtoAdmin.getAvaliacao());
            produtoAdminDto.setDescricao(produtoAdmin.getDescricao());
            produtoAdminDto.setPreco(produtoAdmin.getPreco());
            produtoAdminDto.setQuantidade(produtoAdmin.getQuantidade());

            model.addAttribute("produtoAdminDto", produtoAdminDto);
            model.addAttribute("imagens", produtoAdmin.getImagens());
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return "redirect:/produtos";
        }
        return "ProdutosAdmin/cadastroProdutoAdmin";
    }

    @GetMapping("/editar-produto-estoquista")
    public String showEditarProdutoEstoquista(@RequestParam int id, Model model) {
        try {
            ProdutoAdmin produtoAdmin = repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            ProdutoAdminDto produtoAdminDto = new ProdutoAdminDto();
            produtoAdminDto.setId(produtoAdminDto.getId());
            produtoAdminDto.setQuantidade(produtoAdminDto.getQuantidade());

            model.addAttribute("produtoAdminDto", produtoAdminDto);
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return "redirect:/produtos";
        }
        return "ProdutosEstoquista/AllProdutosEsto";
    }

    @PutMapping("/alterarSituacao/{id}")
    @ResponseBody
    public Map<String, String> alterarSituacao(@PathVariable int id, @RequestBody Map<String, Boolean> payload) {
        ProdutoAdmin produtoAdmin = repository.findById(id).orElse(null);

        if (produtoAdmin == null) {
            return Map.of("erro", "Produto não encontrado");
        }

        produtoAdmin.setSituacao(payload.get("situacao"));
        repository.save(produtoAdmin);

        return Map.of("mensagem", "Situação do produto alterada com sucesso!");
    }

    @GetMapping("/visualizarProduto/{id}")
    public String visualizarProduto(@PathVariable int id, Model model) {
        ProdutoAdmin produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        model.addAttribute("produto", produto);
        return "ProdutosAdmin/visualizarProduto";
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<ProdutoAdmin> produtos = repository.findAll();
        model.addAttribute("produtos", produtos != null ? produtos : new ArrayList<>());
        return "index";
    }

    @GetMapping("detalhesProduto/{id}")
    public String detalhesProduto(@PathVariable int id, Model model) {
        System.out.println("Acessando detalhes do produto com ID: " + id);
        ProdutoAdmin produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        model.addAttribute("produto", produto);
        return "detalhesProduto";
    }
}
