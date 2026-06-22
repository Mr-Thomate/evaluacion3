CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    pnombre VARCHAR(15) NOT NULL,
    snombre VARCHAR(15),
    papellido VARCHAR(15) NOT NULL,
    sapellido VARCHAR(15),
    fecha_nacimiento DATE NOT NULL,
    sexo VARCHAR(20) NOT NULL --por favor
);