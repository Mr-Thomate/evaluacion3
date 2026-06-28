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
