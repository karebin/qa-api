CREATE SCHEMA IF NOT EXISTS public;


CREATE TABLE public.client (
id SERIAL UNIQUE,
first_name VARCHAR(45) NOT NULL,
last_name VARCHAR(45),
mobile_phone VARCHAR(45) PRIMARY KEY NOT NULL,
email VARCHAR(45)
);


INSERT INTO client (first_name, last_name, mobile_phone, email) VALUES
('Илья','Каребин','79991232233', NULL),
('Сергей','Петров','79991232232', 'serega@mail.com'),
('Григорий',NULL,'79991232231', 'grisha@mail.com'),
('Андрей','Самойлов','79991232234', 'grom@mail.com'),
('Василий','Смирнов','79991232235', 'blak@mail.com'),
('Анна',NULL,'79991232236', 'anka@mail.com'),
('Александр','Лисицын','79991232237', 'loga@mail.com'),
('Самвел','Иванов','79991232238', 'ivaniv@mail.com'),
('Иван','БезПродуктов','79991232293', 'ivaniv@mail.com');

CREATE TABLE public.product (
id SERIAL PRIMARY KEY,
clientId INTEGER REFERENCES CLIENT (Id),
type VARCHAR(45) NOT NULL,
product_type VARCHAR(45) NOT NULL,
balance SERIAL NOT NULL
);

INSERT INTO product (clientId, type, product_type, balance) VALUES
(1,'CARD','DEBIT',10),
(2,'CARD','CREDIT',20),
(3,'LOAN','AUTO',130),
(1,'ACCOUNT','ML',10),
(1,'ACCOUNT','DEBIT',430),
(4,'LOAN','AUTO',340),
(5,'LOAN','ML',50),
(1,'CARD','CREDIT',1210);

