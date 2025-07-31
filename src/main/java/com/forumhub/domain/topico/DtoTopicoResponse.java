package com.forumhub.domain.topico;

public record DtoTopicoResponse(
        Long id,
        String titulo,
        String mensagem,
        String dataCriacao) {

}
