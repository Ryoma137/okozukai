DROP TABLE ACCOUNT;
CREATE TABLE ACCOUNT (id bigint generated BY DEFAULT AS IDENTITY, expense INTEGER NOT NULL,
income INTEGER NOT NULL, item VARCHAR(255), itemDate DATE, note VARCHAR(255));

INSERT INTO ACCOUNT(itemDate, item, income, expense, note)
VALUES(2022-02-01,'Food',200,100, "Sandwich"),
(2022-02-02,'Clothes',1000,1500,'Uniqlo T-Shirt'),
(2023-02-03,"iPhone",130000,140000,'iPhone 13 Pro');
