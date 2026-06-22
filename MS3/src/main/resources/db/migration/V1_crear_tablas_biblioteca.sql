CREATE TABLE biblioteca (
    idBiblioteca Integer PRIMARY KEY,
    nombreBiblioteca VARCHAR(255) NOT NULL,
    direccion VARCHAR(255) NOT NULL
);

CREATE TABLE Comuna (
    idComuna Integer PRIMARY KEY,
    nombreComuna VARCHAR(255) NOT NULL
);

CREATE TABLE Region (
    idRegion Integer PRIMARY KEY,
    nombreRegion VARCHAR(255) NOT NULL
);