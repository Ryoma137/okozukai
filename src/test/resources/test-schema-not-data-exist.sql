DROP TABLE ACCOUNT;
CREATE TABLE ACCOUNT (id bigint generated BY DEFAULT AS IDENTITY, expense INTEGER NOT NULL,
income INTEGER NOT NULL, item VARCHAR(255), itemDate DATE, note VARCHAR(255),PRIMARY KEY (id));