CREATE TABLE ACCOUNTS (
    accountId int NOT NULL AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    password binary(512) NOT NULL,
    CONSTRAINT pk_accounts_username PRIMARY KEY (username)

);

CREATE TABLE ORDERS (

    orderId int NOT NULL AUTO_INCREMENT,
    size int NOT NULL,
    price int NOT NULL,
    action varchar NOT NULL,
    username varchar(255) NOT NULL,
    time TIMESTAMP NOT NULL,
    CONSTRAINT pk_orders_orderId PRIMARY KEY (orderId),
    FOREIGN KEY (username) REFERENCES ACCOUNTS(username)

);