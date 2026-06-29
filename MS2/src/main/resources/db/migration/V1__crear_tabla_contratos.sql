CREATE TABLE contrato (
    id_contrato INT AUTO_INCREMENT PRIMARY KEY,
    tipo_contrato VARCHAR(15) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    sueldo INT NOT NULL
);

CREATE TABLE empleado (
    id_empleado INT AUTO_INCREMENT PRIMARY KEY,
    p_nombre VARCHAR(15) NOT NULL,
    s_nombre VARCHAR(15),
    p_apellido VARCHAR(15) NOT NULL,
    s_apellido VARCHAR(15),
    fecha_nacimiento DATE NOT NULL,
    id_contrato INT UNIQUE,
    id_biblioteca INT NOT NULL

    CONSTRAINT fk_empleado_contrato
        FOREIGN KEY (id_contrato) 
        REFERENCES contrato (id_contrato)
);

INSERT INTO contrato (tipo_contrato, fecha_inicio, fecha_fin, sueldo) VALUES
('Indefinido', '2022-03-01', NULL, 850000),
('Plazo Fijo', '2023-06-15', '2025-06-15', 650000),
('Indefinido', '2021-11-01', NULL, 920000),
('Plazo Fijo', '2024-01-10', '2025-01-10', 700000),
('Indefinido', '2020-08-20', NULL, 780000);

-- id_contrato en MS2 (id 1-5), id_biblioteca referencia MS3 (id 1-3)
INSERT INTO empleado (pnombre, snombre, papellido, sapellido, fecha_nacimiento, id_contrato, id_biblioteca) VALUES
('Ana', 'Belen', 'Vargas', 'Mena', '1988-04-10', 1, 1),
('Luis', NULL, 'Castillo', 'Bravo', '1991-09-23', 2, 2),
('Patricia', 'Isabel', 'Reyes', NULL, '1979-12-01', 3, 1),
('Rodrigo', 'Alonso', 'Pinto', 'Jimenez', '1995-02-14', 4, 3),
('Daniela', NULL, 'Salinas', 'Vera', '1987-07-07', 5, 2);