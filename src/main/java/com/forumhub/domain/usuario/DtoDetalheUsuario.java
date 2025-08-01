package com.forumhub.domain.usuario;

public record DtoDetalheUsuario(Long id, String nome, String email, Long perfil) {

    public DtoDetalheUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfil().getId());

    }
}