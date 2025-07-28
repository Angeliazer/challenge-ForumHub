package com.forumhub.domain.usuario;

import com.forumhub.domain.perfil.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroUsuario(
        @NotNull
        String nome,

        @NotNull
        @Email
        String email,

        @NotNull
        String senha,

        @NotNull
        Long perfil
) {

    public DadosCadastroUsuario(String nome, String email, String senha,  Long perfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
    }
}
