package com.forumhub.domain.resposta;


public record DtoResposta(Long id,
                          String mensagem,
                          String dataCriacao,
                          String autor,
                          String solucao
) {}

