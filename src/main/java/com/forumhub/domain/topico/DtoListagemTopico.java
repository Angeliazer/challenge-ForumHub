package com.forumhub.domain.topico;


public record DtoListagemTopico(
        Long id,
        String titulo,
        String mensagem,
        String status,
        String autor,
        String curso
) {}
