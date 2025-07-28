create table respostas (
    id bigint not null auto_increment,
    mensagem varchar(255) not null,
    topico_id bigint not null,
    dataCriacao datetime not null,
    status varchar(50) not null,
    usuario_id bigint not null,
    solucao varchar(255) not null,

    primary key(id),
    constraint fk_respostas_topico_id foreign key(topico_id) references respostas(id),
    constraint fk_respostas_usuario_id foreign key(usuario_id) references usuarios(id)
);