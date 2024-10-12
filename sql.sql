show databases;
CREATE DATABASE main;
use main;
select * from user;
desc user;

delete from user where id = "mnl005";

create table user
(
    id           varchar(255) NOT NULL,
    organization varchar(255),
    name         varchar(255) NOT NULL,
    email        varchar(255) NOT NULL,
    phone        varchar(255) NOT NULL,
    profile      longtext,
    primary KEY (id)
);

create table board
(
    num         int auto_increment not null,
    id          varchar(255)       not null,
    title       varchar(255)       not null,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    content     text,
    image       longtext           not null,
    primary key (num),
    foreign key (id) references user (id) on delete cascade
);

create table invite
(
    num     int auto_increment primary key,
    inviter varchar(255) not null,
    target  varchar(255) not null,
    foreign key (inviter) references user (id) on delete cascade,
    foreign key (target) references user (id) on delete cascade
);
