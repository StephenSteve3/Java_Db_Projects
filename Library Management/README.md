## Table Creation

`Credentials`
CREATE TABLE CREDENTIAL(USERNAME VARCHAR(30), PASSWORD VARCHAR(30));

`Author`
CREATE TABLE AUTHOR(ID SERIAL PRIMARY KEY, NAME VARCHAR(50) UNIQUE NOT NULL);

`Category`
CREATE TABLE CATEGORY(ID SERIAL PRIMARY KEY, NAME VARCHAR(50) UNIQUE NOT NULL);

`Publication`
CREATE TABLE PUBLICATION(ID SERIAL PRIMARY KEY, NAME VARCHAR(50) UNIQUE NOT NULL);

`Book`
CREATE TABLE BOOK(ID SERIAL PRIMARY KEY, NAME VARCHAR(100) NOT NULL, AUTHOR INT REFERENCES AUTHOR(ID) NOT NULL, CATEGORY INT REFERENCES CATEGORY(ID) NOT NULL, PUBLICATION INT REFERENCES PUBLICATION(ID) NOT NULL, PRICE FLOAT NOT NULL CHECK(PRICE > 0));

`User`
CREATE TABLE USERS(ID SERIAL PRIMARY KEY, NAME VARCHAR(50) NOT NULL, GENDER CHAR(1), EMAIL VARCHAR(50) UNIQUE NOT NULL, DOB DATE, PHONE BIGINT, PASSWORD VARCHAR(50) NOT NULL);

`BookCopy`
CREATE TABLE BOOKCOPY(ID SERIAL PRIMARY KEY, BOOK INT NOT NULL REFERENCES BOOK, IS_AVAILABLE BOOLEAN NOT NULL DEFAULT TRUE);

`Borrows`
CREATE TABLE BORROWS(ID SERIAL PRIMARY KEY, USERS INT NOT NULL REFERENCES USERS, BOOKCOPY INT NOT NULL REFERENCES BOOKCOPY, DATE DATE NOT NULL, RETURN DATE);

## Select Queries
SELECT * FROM CREDENTIAL; SELECT * FROM USERS; SELECT * FROM AUTHOR; SELECT * FROM CATEGORY; SELECT * FROM PUBLICATION; SELECT * FROM BOOK; SELECT * FROM BOOKCOPY; SELECT * FROM BORROWS;