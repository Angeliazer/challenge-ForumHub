create table resposta (
    id bigint not null auto_increment,
    mensagem TEXT not null,
    data_criacao datetime not null DEFAULT CURRENT_TIMESTAMP,
    topico_id bigint not null,
    usuario_id bigint not null,
    solucao TEXT,

    primary key(id),
    constraint fk_resposta_topico foreign key(topico_id) references topico(id),
    constraint fk_resposta_autor foreign key(usuario_id) references usuario(id)
);