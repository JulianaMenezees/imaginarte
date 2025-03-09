package com.imaginarte.imaginarte_teste.Services;

import com.imaginarte.imaginarte_teste.model.UsuarioAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

//chama a classe UsuarioAdmin e sua chave primária do banco de dados
public interface UsuariosRepository extends JpaRepository<UsuarioAdmin,Integer> {
}
