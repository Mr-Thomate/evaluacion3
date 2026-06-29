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
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion DATE NULL,
    estado VARCHAR(15) NOT NULL,
    id_cliente INT NOT NULL,
    id_libro INT NOT NULL,
    id_biblioteca INT NOT NULL,
    CONSTRAINT fk_prestamo_cliente 
        FOREIGN KEY (id_cliente) 
        REFERENCES cliente (id_cliente)
);

INSERT INTO cliente (pnombre, snombre, papellido, sapellido, fecha_nacimiento, sexo) VALUES
('Maria', 'Jose', 'Gonzalez', 'Perez', '1990-03-15', 'Femenino'),
('Carlos', 'Andres', 'Ramirez', 'Lopez', '1985-07-22', 'Masculino'),
('Valentina', NULL, 'Torres', 'Soto', '1998-11-05', 'Femenino'),
('Diego', 'Felipe', 'Morales', NULL, '1993-01-30', 'Masculino'),
('Camila', NULL, 'Fuentes', 'Nunez', '2000-06-18', 'Femenino');

-- id_biblioteca referencia MS3 (id 1-3), id_libro referencia MS4 (isbn 1-5)
INSERT INTO prestamo (fecha_prestamo, fecha_devolucion, estado, id_cliente, id_libro, id_biblioteca) VALUES
('2025-01-10', '2025-01-24', 'Devuelto', 1, 1, 1),
('2025-02-05', NULL, 'Activo', 2, 3, 2),
('2025-03-12', '2025-03-26', 'Devuelto', 3, 2, 1),
('2025-04-01', NULL, 'Activo', 4, 5, 3),
('2025-04-20', NULL, 'Atrasado', 1, 4, 2),
('2025-05-08', '2025-05-22', 'Devuelto', 5, 1, 3);