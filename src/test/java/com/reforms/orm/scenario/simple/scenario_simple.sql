-- Тестовый сценарий работы ORM мэппинга по SELECT выражению: вложенные данные

-- Таблица клиентов
CREATE TABLE client(
  id bigint NOT NULL,
  name character varying(127) NOT NULL,
  address_id bigint NOT NULL,
  act_time timestamp NOT NULL,
  CONSTRAINT pk_goods PRIMARY KEY (id)
);

-- Таблица адресов
CREATE TABLE address(
  id bigint NOT NULL,
  city character varying(127) NOT NULL,
  street character varying(127) NOT NULL,
  CONSTRAINT pk_address PRIMARY KEY (id)
);

-- Добавляем адреса
INSERT INTO address (id, city, street) VALUES (1, 'Москва', 'Лужники');
INSERT INTO address (id, city, street) VALUES (2, 'Москва', 'Конова');

-- Добавляем клиентов
INSERT INTO client (id, name, address_id, act_time) VALUES (1, 'Пупкин Иван Иванович', 1, {ts '2017-01-01 19:12:01.69'});
INSERT INTO client (id, name, address_id, act_time) VALUES (2, 'Остапов Никалай Сергеевич', 2, {ts '2017-01-01 19:12:01.69'});