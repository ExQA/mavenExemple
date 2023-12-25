CREATE TABLE IF NOT EXISTS users
(
    id       integer AUTO_INCREMENT primary key,
    name     varchar(255),
    nickname varchar(255),
    birthday datetime,
    password varchar(255),
    amount   numeric(10, 2)
);


CREATE TABLE IF NOT EXISTS games
(
    id           integer AUTO_INCREMENT primary key ,
    name         varchar(255),
    release_date date,
    rating       numeric(10, 2),
    cost         numeric(10, 2),
    description  varchar(1024)
);

CREATE TABLE IF NOT EXISTS usergames
(
    user_id integer NOT NULL,
    game_id integer NOT NULL,
    PRIMARY KEY (user_id, game_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (game_id) REFERENCES games(id)
);