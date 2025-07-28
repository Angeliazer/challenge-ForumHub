create table usuarios (
    id bigint not null auto_increment,
    nome varchar(100) not null,
    email varchar(50) not null,
    senha varchar(255) not null,
    perfil_id bigint not null,

    primary key(id),
    constraint fk_perfis_usuario_id foreign key(perfil_id) references perfis(id)
);