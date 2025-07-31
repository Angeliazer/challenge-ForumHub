package com.forumhub.domain.topico;

import java.time.LocalDateTime;

public record DtoTopicoResponse(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao) {

    public DtoTopicoResponse {
    }
}
