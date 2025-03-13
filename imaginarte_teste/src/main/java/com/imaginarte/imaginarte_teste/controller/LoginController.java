package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.Services.CookieService;
import org.springframework.ui.Model;
import com.imaginarte.imaginarte_teste.Services.UsuariosRepository;
import com.imaginarte.imaginarte_teste.model.UsuarioAdmin;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.UnsupportedEncodingException;

@Controller
public class LoginController {

    @Autowired
    private UsuariosRepository ur;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/dashboard")
    public String mostrarDashboard() {
        return "dashboard";
    }

    @PostMapping("/logar")
    public String loginUsuarioAdmin(UsuarioAdmin usuarioAdmin, Model model, HttpServletResponse response) throws UnsupportedEncodingException {
        UsuarioAdmin usuarioLogado =  this.ur.login(usuarioAdmin.getEmail(), usuarioAdmin.getSenha());
        if(usuarioLogado != null){
           // CookieService.setCookie(response, "usuarioId", String.ValueOf(usuarioLogado.getId()), 10000);
           // CookieService.setCookie(response, "nomeUsuario", String.ValueOf(usuarioLogado.getNome()), 10000);
            return "redirect:/dashboard";
        }
        model.addAttribute("erro", "Usuario Invalido");
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastroUsuarioAdmin() {
        return "cadastro";
    }

    @RequestMapping(value = "/cadastro", method = RequestMethod.POST)
    public String cadastroUsuario(@Valid UsuarioAdmin usuarioAdmin, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/cadastro";
        }

        ur.save(usuarioAdmin);

        return "redirect:/login";
    }
}
