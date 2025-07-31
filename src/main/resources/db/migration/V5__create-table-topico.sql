create table topico (
    id bigint not null auto_increment,
    titulo varchar(255) not null,
    mensagem TEXT not null,
    data_criacao datetime not null DEFAULT CURRENT_TIMESTAMP,
    status varchar(20) not null DEFAULT 'NAO_RESPONDIDO',
    autor_id bigint not null,
    curso_id bigint not null,
    primary key(id),
    constraint fk_topico_autor foreign key(autor_id) references usuario(id),
    constraint fk_topico_curso foreign key(curso_id) references curso(id)
);