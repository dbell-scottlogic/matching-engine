CREATE TABLE ACCOUNTS (
    accountId int NOT NULL AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    password binary(512) NOT NULL,
    token varchar(255),
    CONSTRAINT pk_accounts_accountId PRIMARY KEY (accountId)
);