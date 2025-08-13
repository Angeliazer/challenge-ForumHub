package com.forumhub.domain.resposta;

public record DtoRespostaRequest(Long topico_id,
                                 Long usuario_id,
                                 String mensagem,
                                 String solucao) {
}
