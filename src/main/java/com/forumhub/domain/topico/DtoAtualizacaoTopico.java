package com.forumhub.domain.topico;

import jakarta.validation.constraints.NotBlank;

public record DtoAtualizacaoTopico(
        @NotBlank
        String titulo,

        @NotBlank
        String mensagem

        ) {
}
