package com.forumhub.domain.topico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoTopicoRequest(

        @NotBlank
        String titulo,

        @NotBlank
        String mensagem,

        @NotNull
        Long idCurso,

        @NotNull
        Long idAutor)
{

}
