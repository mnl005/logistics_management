show databases;
CREATE DATABASE main;
use main;
select * from user;
select * from board;
update user set profile = 'https://pixabay.com/ko/illustrations/image-9079096/' where id = 'user1';
select * from invite;
desc user;
insert into user(id,name,email,phone,profile) value ('user3','김한솔','email4@naver.com','0101231234','pro1234');

delete from board where num = 46;
update user set organization = 'group1' where id = 'mnl005';
select * from board;
desc board;

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

create table item
(
    organization varchar(255) not null,
    code varchar(255) not null,
    name varchar(255) not null,
    unique (organization,code)
);
alter table item add column image longtext not null;

create table repository
(
    organization varchar(255) not null,
    location varchar(255) not null,
    code varchar(255),
    quantity int not null default 0,
    unique (organization,location),
    foreign key (organization, code)
references item(organization, code) on delete cascade
);
desc item;
select * from item;
select * from repository;
