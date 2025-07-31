create table resposta (
    id bigint not null auto_increment,
    mensagem varchar(255) not null,
    data_criacao datetime not null DEFAULT CURRENT_TIMESTAMP,
    status varchar(50) not null,
    topico_id bigint not null,
    autor_id bigint not null,
    solucao varchar(255) not null,

    primary key(id),
    constraint fk_resposta_topico foreign key(topico_id) references resposta(id),
    constraint fk_resposta_usuario foreign key(autor_id) references usuario(id)
);