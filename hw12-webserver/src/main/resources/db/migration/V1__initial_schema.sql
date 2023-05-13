-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence admin_SEQ start with 1 increment by 1;
create sequence client_SEQ start with 1 increment by 1;
create sequence address_SEQ start with 1 increment by 1;
create sequence phone_SEQ start with 1 increment by 1;

create table admin (
    id bigint not null primary key,
    email varchar(50) unique,
    password varchar(50)
);

insert into admin values (nextval('admin_SEQ'), 'test@mail.com', 'password');

create table address
(
    id   bigint not null primary key,
    street varchar(50)
);


create table client
(
    id   bigint not null primary key,
    name varchar(50),
    address_id bigint,
    foreign key (address_id) references address(id)
);


create table phone
(
    id   bigint not null primary key,
    number varchar(50),
    client_id bigint,
    foreign key (client_id) references client(id)
);
insert into address values (nextval('address_SEQ'), 'some adress');
insert into client values (nextval('client_SEQ'), 'Name', 1);
insert into phone values (nextval('phone_SEQ'), 'Number', 1);
insert into address values (nextval('address_SEQ'), 'some adress2');
insert into client values (nextval('client_SEQ'), 'Name2', 2);
insert into phone values (nextval('phone_SEQ'), 'Number2', 2);