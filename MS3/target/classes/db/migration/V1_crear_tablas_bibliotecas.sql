CREATE TABLE Region (
    id_region INT AUTO_INCREMENT PRIMARY KEY,
    nombre_region VARCHAR(255) NOT NULL
);

CREATE TABLE Comuna (
    id_comuna INT AUTO_INCREMENT PRIMARY KEY,
    nombre_comuna VARCHAR(255) NOT NULL,
    id_region INT NOT NULL,

    CONSTRAINT fk_comuna_region
        FOREIGN KEY (id_region)
        REFERENCES region (id_region)
);

CREATE TABLE biblioteca (
    id_biblioteca INT AUTO_INCREMENT PRIMARY KEY,
    nombre_biblioteca VARCHAR(255) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    id_comuna INT NOT NULL,

    CONSTRAINT fk_biblioteca_comuna
        FOREIGN KEY (id_comuna)
        REFERENCES comuna (id_comuna)
);

INSERT INTO region (nombre_region) VALUES
('Region Metropolitana'),
('Region de Valparaiso'),
('Region del Biobio');

INSERT INTO comuna (nombre_comuna, id_region) VALUES
('Santiago', 1),
('Providencia', 1),
('Maipu', 1),
('Valparaiso', 2),
('Vina del Mar', 2),
('Concepcion', 3),
('Talcahuano', 3);

INSERT INTO biblioteca (nombre_biblioteca, direccion, id_comuna) VALUES
('Biblioteca de Santiago', 'Av. Libertador 100', 1),
('Biblioteca de Providencia', 'Calle Providencia 200', 2),
('Biblioteca de Maipu', 'Av. Pajaritos 350', 3),
('Biblioteca de Valparaiso', 'Calle Serrano 45', 4),
('Biblioteca de Concepcion', 'Av. O Higgins 780', 6);