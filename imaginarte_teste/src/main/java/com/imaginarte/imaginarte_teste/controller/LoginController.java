package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.imaginarte.imaginarte_teste.Repository.UsuariosRepository;
import com.imaginarte.imaginarte_teste.model.UsuarioAdmin;

import java.io.UnsupportedEncodingException;

@Controller
public class LoginController {

    @Autowired
    private UsuariosRepository ur;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("nome", CookieService.getCookie(request, "nomeUsuario"));
        return "dashboard";
    }

    @GetMapping("/dashboardEstoquista")
    public String mostrarDashboardEstoquista(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("nome", CookieService.getCookie(request, "nomeUsuario"));
        return "dashboardEstoquista";
    }

    @PostMapping("/logar")
    public String loginUsuarioAdmin(@ModelAttribute UsuarioAdmin usuarioAdmin,
                                    Model model,
                                    HttpServletResponse response) throws UnsupportedEncodingException {

        UsuarioAdmin usuarioLogado = ur.findByEmail(usuarioAdmin.getEmail());

        if (usuarioLogado != null && passwordEncoder.matches(usuarioAdmin.getSenha(), usuarioLogado.getSenha())) {
            if (!usuarioLogado.isSituacao()) {
                model.addAttribute("erro", "Usuário inativo!");
                return "login";
            }

            CookieService.setCookie(response, "usuarioId", String.valueOf(usuarioLogado.getId()), 10000);
            CookieService.setCookie(response, "nomeUsuario", String.valueOf(usuarioLogado.getNome()), 10000);
            CookieService.setCookie(response, "roleUsuario", usuarioLogado.getGrupo(), 10000);

            if ("Administrador".equals(usuarioLogado.getGrupo())) {
                return "redirect:/dashboard";
            } else if ("Estoquista".equals(usuarioLogado.getGrupo())) {
                return "redirect:/dashboardEstoquista";
            }
        }

        model.addAttribute("erro", "Usuário inválido!");
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastroUsuarioAdmin() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastroUsuario(@Valid @ModelAttribute UsuarioAdmin usuarioAdmin, BindingResult result) {
        if (result.hasErrors()) {
            return "cadastro";
        }

        // Criptografa a senha antes de salvar
        usuarioAdmin.setSenha(passwordEncoder.encode(usuarioAdmin.getSenha()));
        ur.save(usuarioAdmin);

        return "redirect:/login";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();

        Cookie logoutCookie = new Cookie("JSESSIONID", null);
        logoutCookie.setMaxAge(0);
        logoutCookie.setPath("/");
        response.addCookie(logoutCookie);

        return "redirect:/login";
    }
}
