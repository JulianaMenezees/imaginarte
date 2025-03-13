package com.imaginarte.imaginarte_teste.controller;

import org.springframework.ui.Model;
import com.imaginarte.imaginarte_teste.Repository.UsuariosRepository;
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

@Controller
public class LoginController {

    @Autowired
    private UsuariosRepository ur;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(){
        return "dashboard";
    }

   @PostMapping("/logar")
   public String loginUsuarioAdmin(UsuarioAdmin usuarioAdmin, Model model, HttpServletResponse response){
        UsuarioAdmin usuarioLogado = this.ur.login(usuarioAdmin.getEmail(), usuarioAdmin.getSenha());
        if(usuarioLogado != null){
            return "redirect:/dashboard";
        }
        model.addAttribute("erro", "Usuario Invalido!");
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
