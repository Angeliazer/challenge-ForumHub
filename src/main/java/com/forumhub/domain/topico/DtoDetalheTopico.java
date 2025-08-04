package com.forumhub.domain.topico;

import com.forumhub.domain.resposta.DtoResposta;

import java.util.ArrayList;
import java.util.List;

public record DtoDetalheTopico(
        Long id,
        String titulo,
        String mensagem,
        String dataCriacao,
        String status,
        String autor,
        String curso,
        List<DtoResposta> respostas) {
}
