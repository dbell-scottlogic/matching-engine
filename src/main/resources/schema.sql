CREATE TABLE ACCOUNTS (
    accountId int NOT NULL AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    CONSTRAINT pk_accounts_accountId PRIMARY KEY (accountId)
);