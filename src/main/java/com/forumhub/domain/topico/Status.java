package com.forumhub.domain.topico;

public enum Status {
    NAO_RESPONDIDO(0),
    RESPONDIDO(1);

    private int valor;

    Status(int valor){
        this.valor = valor;
    }
}
