create table usuario (
    id bigint not null auto_increment,
    nome varchar(100) not null,
    email varchar(50) not null unique,
    senha varchar(255) not null,
    perfil_id bigint not null,

    primary key(id),
    constraint fk_perfil_usuario foreign key(perfil_id) references perfil(id)
);