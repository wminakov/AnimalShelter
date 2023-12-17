DROP DATABASE IF EXISTS friends_of_man;
CREATE DATABASE friends_of_man;
USE friends_of_man;

CREATE TABLE animals (
	id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	name VARCHAR(50)
);
INSERT INTO animals (name) VALUES
('Home'),
('Pack');

CREATE TABLE home_animals (
	id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	name VARCHAR(50),
	animals_id INT,
	FOREIGN KEY (animals_id) REFERENCES animals(id) ON DELETE CASCADE ON UPDATE CASCADE
);
INSERT INTO home_animals (name, animals_id) VALUES
('Dog', 1),
('Cat', 1),
('Hamster', 1);

CREATE TABLE pack_animals (
	id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	name VARCHAR(50),
	animals_id INT,
	FOREIGN KEY (animals_id) REFERENCES animals(id) ON DELETE CASCADE ON UPDATE CASCADE
);
INSERT INTO pack_animals (name, animals_id) VALUES
('Horse', 2),
('Donkey', 2),
('Camel', 2);

CREATE TABLE dogs (
	id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	name VARCHAR(100),
	birthday DATE,
	skills TEXT,
	type INT,
	FOREIGN KEY (type) REFERENCES home_animals(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE cats (
	id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	name VARCHAR(100),
	birthday DATE,
	skills TEXT,
	type INT,
	FOREIGN KEY (type) REFERENCES home_animals(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE hamsters (
	id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	name VARCHAR(100),
	birthday DATE,
	skills TEXT,
	type INT,
	FOREIGN KEY (type) REFERENCES home_animals(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE horses (
	id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	name VARCHAR(100),
	birthday DATE,
	skills TEXT,
	type INT,
	FOREIGN KEY (type) REFERENCES pack_animals(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE donkeys (
	id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	name VARCHAR(100),
	birthday DATE,
	skills TEXT,
	type INT,
	FOREIGN KEY (type) REFERENCES pack_animals(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE camels (
	id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	name VARCHAR(100),
	birthday DATE,
	skills TEXT,
	type INT,
	FOREIGN KEY (type) REFERENCES pack_animals(id) ON DELETE CASCADE ON UPDATE CASCADE
);

# dogs
INSERT INTO dogs (name, birthday, skills, type) VALUES
('Бейли','2023-01-01','Сидеть', 1),
('Макс','2023-02-01','Лежать', 1),
('Луна','2023-03-01','Фас', 1);

# cats
INSERT INTO cats (name, birthday, skills, type) VALUES
('Вася','2023-01-01','Мурлыкать', 2),
('Мурка','2023-01-01','Ловить мышей', 2),
('Симба','2023-01-01','Играть', 2);

# hamsters
INSERT INTO hamsters (name, birthday, skills, type) VALUES
('Карамелька','2022-02-01','Подъем на задние лапы', 3),
('Шнюфель','2023-03-01','Бег в колесе', 3),
('Пушистик','2023-04-01','', 3);

# horses
INSERT INTO horses (name, birthday, skills, type) VALUES
('Торнадо','2020-01-01','Шаг', 1),
('Рыжик','2020-01-01','Галоп', 1),
('Пегас','2020-01-01','', 1);

# donkeys
INSERT INTO donkeys (name, birthday, skills, type) VALUES
('Булат','2020-05-15','', 2),
('Потап','2020-06-16','', 2),
('Граф','2020-06-17','', 2);

# camels
INSERT INTO camels (name, birthday, skills, type) VALUES
('Хаджар','2019-07-20','Кусается', 3),
('Касим','2019-08-21','Плюется', 3),
('Зара','2019-09-22','У верблюда два горба, потому что жизнь - борьба.', 3);

# delete camels
DELETE FROM camels;

SELECT * FROM animals;
SELECT * FROM home_animals;
SELECT * FROM pack_animals;
SELECT * FROM dogs;
SELECT * FROM cats;
SELECT * FROM hamsters;
SELECT * FROM donkeys;
SELECT * FROM horses;
SELECT * FROM camels;

# to combine horses and donkeys
SELECT name, birthday, skills FROM horses
UNION
SELECT name, birthday, skills FROM donkeys;

# young_animals
CREATE TABLE young_animals AS
WITH
all_animals AS
(SELECT id, name, birthday, skills, 'Dog' as class FROM dogs
UNION
SELECT id, name, birthday, skills, 'Cat' AS class FROM cats
UNION
SELECT id, name, birthday, skills, 'Hamster' AS class FROM hamsters
UNION
SELECT id, name, birthday, skills, 'Horse' AS class FROM horses
UNION
SELECT id, name, birthday, skills, 'Donkey' AS class FROM donkeys
UNION
SELECT id, name, birthday, skills, 'Camel' AS class FROM camels)
SELECT
	id,
  class,
  name,
  birthday,
  skills,
  TIMESTAMPDIFF(MONTH, birthday, CURDATE()) AS age_month
FROM all_animals
WHERE birthday BETWEEN DATE_SUB(CURDATE(), INTERVAL 3 YEAR) AND DATE_SUB(CURDATE(), INTERVAL 1 YEAR);

SELECT * FROM young_animals;

# megre tables
WITH
h_t AS
(SELECT h_a.id, h_a.name AS class, animals.name AS type FROM home_animals AS h_a LEFT JOIN animals ON h_a.animals_id=animals.id ),
p_t AS
(SELECT h_a.id, h_a.name AS class, animals.name AS type FROM pack_animals AS h_a LEFT JOIN animals ON h_a.animals_id=animals.id)
SELECT dogs.name, dogs.birthday, dogs.skills, h_t.class, h_t.type
FROM dogs
LEFT JOIN h_t ON dogs.type=h_t.id
UNION
SELECT cats.name, cats.birthday, cats.skills, h_t.class, h_t.type
FROM cats
LEFT JOIN h_t ON cats.type=h_t.id
UNION
SELECT hamsters.name, hamsters.birthday, hamsters.skills, h_t.class, h_t.type
FROM hamsters
LEFT JOIN h_t ON hamsters.type=h_t.id
UNION
SELECT horses.name, horses.birthday, horses.skills, p_t.class, p_t.type
FROM horses
LEFT JOIN p_t ON horses.type=p_t.id
UNION
SELECT donkeys.name, donkeys.birthday, donkeys.skills, p_t.class, p_t.type
FROM donkeys
LEFT JOIN p_t ON donkeys.type=p_t.id
UNION
SELECT camels.name, camels.birthday, camels.skills, p_t.class, p_t.type
FROM camels
LEFT JOIN p_t ON camels.type=p_t.id;

