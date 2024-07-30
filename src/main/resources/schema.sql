drop table if exists users, items, bookings, requests, comments;

create table if not exists users
(
    id    bigint generated always as identity primary key,
    name  varchar(100) not null,
    email varchar(255) not null unique
);

create table if not exists items
(
    id          bigint generated always as identity primary key,
    name        varchar(255)  not null,
    description varchar(1000) not null,
    owner_id    bigint,
    available   boolean,
    request_id  bigint
);

create table if not exists bookings
(
    id         bigint generated always as identity primary key,
    start_date timestamp without time zone not null,
    end_date   timestamp without time zone not null,
    item_id    bigint,
    booker_id  bigint,
    status     varchar(50)
);

create table if not exists requests
(
    id           bigint generated always as identity primary key,
    description  varchar(1000) not null,
    requestor_id bigint,
    created      timestamp without time zone
);

create table if not exists comments
(
    id        bigint generated always as identity primary key,
    text      varchar(1000) not null,
    item_id   bigint        not null,
    author_id bigint        not null,
    created   timestamp without time zone
);
alter table items
    add foreign key (owner_id) references users (id) on delete restrict;
alter table items
    add foreign key (request_id) references requests (id) on delete restrict;
alter table bookings
    add foreign key (item_id) references items (id) on delete restrict;
alter table bookings
    add foreign key (booker_id) references users (id) on delete restrict;
alter table requests
    add foreign key (requestor_id) references users (id) on delete restrict;
alter table comments
    add foreign key (item_id) references items (id) on delete restrict;
alter table comments
    add foreign key (author_id) references users (id) on delete restrict;