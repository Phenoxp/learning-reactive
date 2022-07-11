create table users (
    id bigint auto_increment,
    name varchar(50),
    balance int,

    primary key(id)
);

create table user_transaction(
    id bigint auto_increment,
    user_id bigint,
    amount int,
    transaction_date timestamp,

    primary key(id),
    foreign key(user_id) references users(id) on delete cascade
);

insert into users(name, balance) values ('Sam', 1000),  ('Jake', 1200), ('Marshall', 2000), ('Rayan', 800);