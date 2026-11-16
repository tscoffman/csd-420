USE databasedb;

CREATE TABLE fans (
	ID int PRIMARY KEY,
	firstname varchar(25),
	lastname varchar(25),
	favoriteteam varchar(25)
);

INSERT INTO fans VALUES (1, 'Lou', 'Woods', 'Packers');
INSERT INTO fans VALUES (2, 'Maria', 'Lopez', 'Cowboys');
INSERT INTO fans VALUES (3, 'James', 'Carter', 'Chiefs');
INSERT INTO fans VALUES (4, 'Priya', 'Patel', 'Steelers');
INSERT INTO fans VALUES (5, 'Sam', 'Nguyen', 'Bears');