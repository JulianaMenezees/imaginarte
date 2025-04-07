package com.imaginarte.imaginarte_teste.model.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class UsuarioEdicaoDto {

    private long id;

    @NotBlank
    private String nome;

    @NotNull
    private LocalDate dataNascimento;

    @NotBlank
    private String genero;

    private String senha;

}
