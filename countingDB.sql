create database if not exists countingDB;

use countingDB;

create table Game ( 
	ID int primary key auto_increment,
    GameDone bool default false,
    FinalAnswer char(4) not null    
);

create table Round (
	ID int primary key auto_increment,
    GameID int,
    foreign key fk_game (GameID)
    references Game(ID),
    Guess char(4) not null,
    result char(7) not null,
    `DateTime` datetime not null
    default now()
);

