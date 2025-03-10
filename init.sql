
# CREATE DATABASE main;
use main;
# show tables;
# drop table inventory;
#
# select * from user;
# select * from organization;
# select * from invite;
# select * from post;
# select * from organization;
# select * from user_organization;
# select * from item;
# select * from location;
# select * from inventory;
#테스트용
# update user set profile = 'url(\"https://cdn.pixabay.com/photo/2024/07/19/08/16/waves-8905720_1280.png\")' where id = 'user1';
# insert into user values('mnl005','mnl005@naver.com','이현건','01057712038','url(\"https://cdn.pixabay.com/photo/2024/07/19/08/16/waves-8905720_1280.png\")');
# insert into user values('user1','u1mail@naver.com','김광식','01000000000','url(\"https://cdn.pixabay.com/photo/2024/07/19/08/16/waves-8905720_1280.png\")');
# insert into user values('user2','u2mail@naver.com','곽두팔','01000000000','url(\"https://cdn.pixabay.com/photo/2023/07/30/12/07/bird-8158766_1280.png\")');
# insert into user values('user3','u3mail@naver.com','김철홍','01000000000','url(\"https://cdn.pixabay.com/photo/2024/09/30/15/37/halloween-9086123_1280.jpg\")');
# insert into organization(organization, master) values('group2','mnl005');
# insert into user_organization(id, organization) values('user1','mygroup');
# insert into user_organization(id, organization) values('user2','my group');
# insert into user_organization(id, organization) values('user3','mygroup');
#

# drop table item;
# delete from user where id = 'mnl005';
# delete from organization where master = 'mnl006';


create table user
(
    id           varchar(255) NOT NULL,
    email        varchar(255) NOT NULL unique,
    name         varchar(255) NOT NULL,
    phone        varchar(255) NOT NULL,
    profile      longtext,
    primary KEY (id)
);
create table organization
(
    organization varchar(255) NOT NULL,
    master varchar(255) NOT NULL,
    primary key (organization),
    foreign key (master) references user(id) on delete cascade
);
create table user_organization
(
    id varchar(255) not null,
    organization varchar(255) not null,
    primary key (id, organization),
    foreign key (id) references user(id) on delete cascade,
    foreign key (organization) references  organization(organization) on delete cascade
);
create table invite
(
    num     int auto_increment primary key,
    master varchar(255) not null,
    target  varchar(255) not null,
    organization  varchar(255) not null,
    unique (master,target,organization),
    foreign KEY (master, organization) references organization (master, organization) ON delete cascade
);

DELIMITER $$

CREATE TRIGGER after_organization_insert
    AFTER INSERT ON organization
    FOR EACH ROW
BEGIN
    INSERT INTO user_organization (id, organization)
    VALUES (NEW.master, NEW.organization);
END $$

DELIMITER ;

create table post
(
    num         int auto_increment not null,
    organization          varchar(255)       not null,
    id          varchar(255)       not null,
    title       varchar(255)       not null,
    created_date varchar(255),
    content     text not null,
    image       longtext           not null,
    primary key (num),
    foreign key (organization) references organization(organization) on delete cascade,
    foreign key (id) references user(id) on delete cascade
);


DELIMITER $$

CREATE TRIGGER set_created_date
    BEFORE INSERT ON post
    FOR EACH ROW
BEGIN
    SET NEW.created_date = DATE_FORMAT(NOW(), '%Y-%m-%d');
END $$

DELIMITER ;



create table item
(
    organization varchar(255) not null,
    item_code varchar(255) not null,
    name varchar(255) not null,
    other varchar(255),
    image longtext not null,
    primary key (organization, item_code),
    foreign key (organization) references organization(organization) on delete cascade
);
create table location
(
    organization varchar(255) not null,
    location_code varchar(255) not null,
    primary key (organization, location_code),
    foreign key (organization) references organization(organization) on delete cascade
);
CREATE TABLE inventory
(
    num     int auto_increment primary key,
    organization varchar(255) not null,
    location_code varchar(255) not null,
    item_code varchar(255) not null,
    quantity int not null default 0,
    updated_date varchar(255) not null,
    status ENUM('NORMAL', 'DISCREPANCY', 'DAMAGED') NOT NULL DEFAULT 'Normal',
    unique (organization, location_code, item_code),
    foreign key (organization,location_code) references location(organization,location_code) on delete cascade,
    foreign key (organization,item_code) references item(organization,item_code) on delete cascade
);

DELIMITER $$

CREATE TRIGGER set_update_inventory_date
    BEFORE INSERT ON inventory
    FOR EACH ROW
BEGIN
    SET NEW.updated_date = DATE_FORMAT(NOW(), '%Y-%m-%d');
END $$

DELIMITER ;




