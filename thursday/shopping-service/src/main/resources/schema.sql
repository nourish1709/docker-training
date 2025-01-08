create table if not exists item
(
    id          serial primary key,
    name        varchar(255)   not null unique,
    description text,
    price       decimal(10, 2) not null,
    quantity    integer        not null
);
