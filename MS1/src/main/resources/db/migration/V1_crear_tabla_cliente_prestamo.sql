CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    pnombre VARCHAR(15) NOT NULL,
    snombre VARCHAR(15),
    papellido VARCHAR(15) NOT NULL,
    sapellido VARCHAR(15),
    fecha_nacimiento DATE NOT NULL,
    sexo VARCHAR(20) NOT NULL --por favor
);

CREATE TABLE prestamo (
    id_prestamo INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_libro INT NOT NULL,
    id_biblioteca INT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion DATE NULL,
    CONSTRAINT fk_prestamo_cliente 
        FOREIGN KEY (id_cliente) 
        REFERENCES cliente (id_cliente)
);
