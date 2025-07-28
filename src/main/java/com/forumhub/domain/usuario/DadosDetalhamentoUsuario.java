package com.forumhub.domain.usuario;

public record DadosDetalhamentoUsuario(Long id, String nome, String email, String senha, Long perfil) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil().getId());

    }
}