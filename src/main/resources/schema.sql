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

CREATE TABLE TRADES (

    tradeId int NOT NULL AUTO_INCREMENT,
    price int NOT NULL,
    buyOrderId int NOT NULL,
    sellOrderId int NOT NULL,
    buyAccountUsername varchar(255) NOT NULL,
    sellAccountUsername varchar(255)NOT NULL,
    time TIMESTAMP NOT NULL,
    CONSTRAINT pk_trades_tradeId PRIMARY KEY (tradeId),
    FOREIGN KEY (buyOrderId) REFERENCES ORDERS(orderId),
    FOREIGN KEY (sellOrderId) REFERENCES ORDERS(orderId),
    FOREIGN KEY (buyAccountUsername) REFERENCES ACCOUNTS(username),
    FOREIGN KEY (sellAccountUsername) REFERENCES ACCOUNTS(username)


);