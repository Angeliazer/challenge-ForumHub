package com.forumhub.domain.topico;

import com.forumhub.domain.resposta.DtoResposta;

import java.util.*;
import java.util.stream.Collectors;

public record DtoDetalheTopico(
        Long id,
        String titulo,
        String mensagem,
        String dataCriacao,
        String status,
        String autor,
        String curso,
        List<DtoResposta> respostas) {

    public DtoDetalheTopico(Topico topico) {
        this(topico.getId(), topico.getTitulo(), topico.getMensagem(), topico.formataData(),
                topico.getStatus().name(), topico.getAutor().getNome(), topico.getCurso().getNome(), topico.getRespostas().stream()
                        .map(resposta -> new DtoResposta(
                                resposta.getId(),
                                resposta.getMensagem(),
                                resposta.getDataCriacao().toString(),
                                resposta.getAutor().getNome(),
                                resposta.getSolucao()
                        ))
                        .collect(Collectors.toList()));
    }
}