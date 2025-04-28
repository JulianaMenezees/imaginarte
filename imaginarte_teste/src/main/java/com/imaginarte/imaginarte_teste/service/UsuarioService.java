package com.imaginarte.imaginarte_teste.service;

import com.imaginarte.imaginarte_teste.Repository.UsuarioRepository;
import com.imaginarte.imaginarte_teste.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private HttpSession session;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> getUsuarioLogado() {
        // Tenta pegar o usuário logado da sessão
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        return Optional.ofNullable(usuarioLogado);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

}
