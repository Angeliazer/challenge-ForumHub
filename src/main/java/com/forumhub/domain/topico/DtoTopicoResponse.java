package com.forumhub.domain.topico;

public record DtoTopicoResponse(
        Long id,
        String titulo,
        String mensagem,
        String dataCriacao) {

    public DtoTopicoResponse(Topico topico){
        this(topico.getId(), topico.getTitulo(), topico.getMensagem(), topico.formataData()
        );
    }
}
