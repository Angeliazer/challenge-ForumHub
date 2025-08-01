package com.forumhub.domain.usuario;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DtoCadastroUsuario(
        @NotNull
        String nome,

        @NotNull
        @Email
        @Column(unique = true)
        String email,

        @NotNull
        String senha,

        @NotNull
        Long idPerfil
) {

    public DtoCadastroUsuario(@Valid String nome, String email, String senha, Long idPerfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.idPerfil = idPerfil;
    }
}
