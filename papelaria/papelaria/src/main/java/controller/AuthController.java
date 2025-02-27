package controller;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Exibe a página de registro
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    // Processa o registro
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDto userDto, Model model) {
        try {
            userService.registerUser(
                    userDto.getEmail(),
                    userDto.getCpf(),
                    userDto.getPassword(),
                    userDto.getConfirmPassword()
            );
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        return "redirect:/login";
    }

    // Exibe a página de login
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}

