DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS flights;
DROP TABLE IF EXISTS persons;

CREATE TABLE persons(
 person_id INT NOT NULL,
 first_name VARCHAR(32) NOT NULL,
 last_name VARCHAR(32) NOT NULL,
 PRIMARY KEY( person_id )
) TYPE=INNODB;

CREATE TABLE flights(
 flight_id INT NOT NULL,
 name VARCHAR(32) NOT NULL,
 departure_utc DATETIME NOT NULL,
 arrival_utc DATETIME NOT NULL,
 PRIMARY KEY( flight_id )
) TYPE=INNODB;

CREATE TABLE reservations(
 reservation_id INT NOT NULL,
 person_id_fk INT NOT NULL,
 flight_id_fk INT NOT NULL,
 registration_utc DATETIME NOT NULL,
 comment TEXT,
 PRIMARY KEY(reservation_id,person_id_fk,flight_id_fk),
 INDEX person_ind(person_id_fk),
 INDEX flight_ind(flight_id_fk),
 FOREIGN KEY (person_id_fk) REFERENCES persons(person_id),
 FOREIGN KEY (flight_id_fk) REFERENCES flights(flight_id)
) TYPE=INNODB;

INSERT INTO persons VALUES(1,'Aslak','Hellesøy');
INSERT INTO persons VALUES(2,'Eivind','Waaler');
INSERT INTO persons VALUES(3,'Ludovic','Claude');
