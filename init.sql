
# CREATE DATABASE main;
use main;
select * from repository;
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
    created_date varchar(255),
    content     text,
    image       longtext           not null,
    primary key (num),
    foreign key (id) references user (id) on delete cascade
);


DELIMITER $$

CREATE TRIGGER set_created_date
    BEFORE INSERT ON board
    FOR EACH ROW
BEGIN
    SET NEW.created_date = DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s');
END $$

DELIMITER ;

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
    image longtext not null,
    unique (organization,code)
);

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

