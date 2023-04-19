CREATE TABLE IF NOT EXISTS users(
     id SERIAL PRIMARY KEY,
     first_name varchar(60),
    last_name varchar(60),
    email varchar(60)  ,
    password varchar(100),
    role varchar(10),
    UNIQUE(email)
    );
