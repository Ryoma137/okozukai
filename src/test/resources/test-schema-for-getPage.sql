DROP TABLE ACCOUNT;
CREATE TABLE ACCOUNT (id bigint, item_date DATE,  item VARCHAR(255),
income INTEGER NOT NULL,expense INTEGER NOT NULL,  note VARCHAR(255),PRIMARY KEY (id));

INSERT INTO ACCOUNT(id, item_date, item, income, expense, note)
VALUES(1,'2022-01-20','T-Shirts',1000,0,'Uniqlo T-Shirt'),
(2,'2022-05-03','iPhone',0,140000,'iPhone 13 Pro'),
(3,'2022-07-20','Green Curry',500,0,'Thai Cuisine'),
(4, '2022-10-08','Sushi',0,700,'Tuna');

