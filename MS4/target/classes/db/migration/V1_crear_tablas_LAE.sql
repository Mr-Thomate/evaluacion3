CREATE TABLE Libro (
    isbn INTEGER PRIMARY KEY,
    titulo VARCHAR(30) NOT NULL,
    fechaPublicacion DATE NOT NULL
);

CREATE TABLE Editorial(
    id INTEGER PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL
);

CREATE TABLE Autor(
    id INTEGER PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL
);