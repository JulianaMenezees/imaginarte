package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.Repository.ProdutosRepository;
import com.imaginarte.imaginarte_teste.model.DTO.ProdutoAdminDto;
import com.imaginarte.imaginarte_teste.model.DTO.UsuarioAdminDto;
import com.imaginarte.imaginarte_teste.model.ProdutoAdmin;
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
@RequestMapping("/produtos")
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
                                          RedirectAttributes redirectAttributes) {
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
        produtoAdmin.setPreco(produtoAdminDto.getPreco());
        produtoAdmin.setQuantidade(produtoAdminDto.getQuantidade());

        repository.save(produtoAdmin);
        redirectAttributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");

        return "redirect:/produtos";
    }

    @GetMapping("/editar-produto")
    public String showEditarProduto(@RequestParam int id, Model model) {
        try {
            ProdutoAdmin produtoAdmin = repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            ProdutoAdminDto produtoAdminDto = new ProdutoAdminDto();
            produtoAdminDto.setId(produtoAdminDto.getId());
            produtoAdminDto.setNome(produtoAdminDto.getNome());
            produtoAdminDto.setAvaliacao(produtoAdminDto.getAvaliacao());
            produtoAdminDto.setDescricao(produtoAdminDto.getDescricao());
            produtoAdminDto.setPreco(produtoAdminDto.getPreco());
            produtoAdminDto.setQuantidade(produtoAdminDto.getQuantidade());

            model.addAttribute("produtoAdminDto", produtoAdminDto);
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return "redirect:/produtos";
        }
        return "ProdutosAdmin/cadastroProdutoAdmin";
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

}
