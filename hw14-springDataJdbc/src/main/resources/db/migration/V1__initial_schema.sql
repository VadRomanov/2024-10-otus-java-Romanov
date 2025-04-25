create table address
(
   id         bigserial not null primary key,
   street     varchar(50) not null
);

create table client
(
    id         bigserial not null primary key,
    address_id bigint,
    name       varchar(50) not null
);

create table phone
(
    id        bigserial not null primary key,
    client_id bigint not null references client (id),
    number    varchar(15) not null
);