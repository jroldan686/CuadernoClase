DROP DATABASE IF EXISTS cuadernoDBjesusr;
CREATE DATABASE cuadernoDBjesusr CHARACTER SET utf8 COLLATE utf8_general_ci;

USE cuadernoDBjesusr;

CREATE TABLE IF NOT EXISTS `alumno` (
  `id` int AUTO_INCREMENT NOT NULL,
  `nombre` varchar(50) NOT NULL UNIQUE,
  `direccion` varchar(250) NOT NULL,
  `ciudad` varchar(30) NOT NULL,
  `codpostal` varchar(5) NOT NULL,
  `telefono` varchar(9) NOT NULL,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `falta` (
  `id` int AUTO_INCREMENT NOT NULL,
  `fecha` date NOT NULL,
  `alumno` varchar(50) NOT NULL,
  `tipo` int,
  `trabajo` int,
  `actitud` int,
  `observaciones` varchar(250),
  PRIMARY KEY (`id`),
  KEY (`alumno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `falta`
  ADD CONSTRAINT `falta_ibfk_1` FOREIGN KEY (`alumno`) REFERENCES `alumno` (`nombre`) ON DELETE CASCADE ON UPDATE CASCADE;

INSERT INTO `alumno` (`nombre`, `direccion`, `ciudad`, `codpostal`, `telefono`, `email`) VALUES
('Julio Domingo', 'Avenida Iceferon, 2', 'Málaga', 29000, 662000000, 'juliodomingo@gmail.com'),
('Juana D´Accord', 'Calle Seikoma, 1', 'Málaga', 29000, 662999999, 'juanadaccord@gmail.com');

INSERT INTO `falta` (`fecha`, `alumno`, `tipo`, `trabajo`, `actitud`, `observaciones`) VALUES
('2017-03-02', 'Julio Domingo', 2, 2, 1, null),
('2017-03-02', 'Juana D´Accord', 1, 3, 2, 'No trabaja y falta mucho'),
('2017-03-03', 'Julio Domingo', 3, 1, 1, 'Se quedó dormido en casa'),
('2017-03-03', 'Juana D´Accord', 3, 2, 1, 'Va mejorando');

DELIMITER @
DROP TRIGGER IF EXISTS ai_alumno @
CREATE TRIGGER ai_alumno AFTER INSERT ON alumno
FOR EACH ROW
BEGIN
	INSERT INTO falta (alumno, fecha) VALUES (NEW.nombre, CURDATE());
END @

DROP TRIGGER IF EXISTS au_alumno @
CREATE TRIGGER au_alumno AFTER UPDATE ON alumno
FOR EACH ROW
BEGIN
	UPDATE falta SET alumno = NEW.nombre WHERE falta.alumno = OLD.nombre;
END @

DROP TRIGGER IF EXISTS ad_alumno @
CREATE TRIGGER ad_alumno AFTER DELETE ON alumno
FOR EACH ROW
BEGIN
	DELETE FROM falta WHERE falta.alumno = OLD.nombre;
END @
DELIMITER ;
