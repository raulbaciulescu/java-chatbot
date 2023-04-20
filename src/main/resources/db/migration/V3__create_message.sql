CREATE TABLE IF NOT EXISTS messages(
    id SERIAL PRIMARY KEY,
    type varchar(60),
    text varchar(100000)
    );

ALTER TABLE messages
    ADD chat_fk bigint,
    ADD FOREIGN KEY (chat_fk) REFERENCES chats(id);
