package com.forumhub.domain.topico;

public record DtoDetalheTopico(Long id,
                               String titulo,
                               String mensagem,
                               String status,
                               String autor,
                               String curso) {
}
