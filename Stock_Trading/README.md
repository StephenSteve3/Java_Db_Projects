## Table Creation

`Stock`
CREATE TABLE STOCK (ID SERIAL PRIMARY KEY, NAME VARCHAR(100) NOT NULL, PRICE FLOAT NOT NULL);

`Trader`
CREATE TABLE TRADER (ID SERIAL PRIMARY KEY, NAME VARCHAR(50) NOT NULL, GENDER CHAR(1) NOT NULL, EMAIL VARCHAR(50) UNIQUE NOT NULL, AADHAR BIGINT UNIQUE NOT NULL, PHONE BIGINT NOT NULL, PASSWORD VARCHAR(50) NOT NULL , AMOUNT FLOAT NOT NULL);

`Holding`
CREATE TABLE HOLDING (ID SERIAL PRIMARY KEY, TRADER INT REFERENCES TRADER(ID) NOT NULL, STOCK INT REFERENCES STOCK(ID) NOT NULL, PRICE FLOAT NOT NULL, QUANTITY INT DEFAULT 1);

## Select queries       
SELECT * FROM STOCK; SELECT * FROM TRADER; SELECT * FROM HOLDING;# Stock_Trading
# Java_Db_Projects