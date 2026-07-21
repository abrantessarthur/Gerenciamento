
create table usuarios(
    id bigint not null auto_increment,
    nome varchar(100) not null,
    email varchar(100) not null,
    senha_hash varchar(30) not null
);

create table campo(
    id bigint not null,
    nome_do_campo varchar(50),
    status varchar(50),
    preco_hora decimal(10,2)
);

create table reservas(
    id bigint not null auto_increment,
    usuario_id bigint not null,
    campo_id big int not null,
    hora_inicio DATETIME NOT NULL,
    hora_fim DATETIME NOT NULL,
    valor decimal(10,2),
    status varchar(20),

    primary key(id),

    constraint fk_reserva_usuario
                     foreign key(usuario_id) references usuarios(id),
    constraint fk_reserva_campo
        foreign key(campo_id) references campo(id)

);

CREATE TABLE usuario_roles (
                               usuario_id BIGINT NOT NULL,
                               role_id BIGINT NOT NULL,

                               PRIMARY KEY(usuario_id, role_id),

                               FOREIGN KEY(usuario_id) REFERENCES usuario(id),
                               FOREIGN KEY(role_id) REFERENCES roles(id)
);