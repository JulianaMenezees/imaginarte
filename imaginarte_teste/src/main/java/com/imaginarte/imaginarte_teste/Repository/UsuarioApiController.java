package com.imaginarte.imaginarte_teste.Repository;

import com.imaginarte.imaginarte_teste.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioApiController {

    @Autowired
    private UsuarioRepository repo;

    @GetMapping("/verificar-email")
    public ResponseEntity<Boolean> verificarEmail(@RequestParam String email) {
        boolean existe = repo.existsByEmail(email);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/verificar-cpf")
    public ResponseEntity<Boolean> verificarCpf(@RequestParam String cpf) {
        String cpfLimpo = cpf.replaceAll("\\D", "");
        boolean existe = repo.existsByCpf(cpfLimpo);
        return ResponseEntity.ok(existe);
    }

}
