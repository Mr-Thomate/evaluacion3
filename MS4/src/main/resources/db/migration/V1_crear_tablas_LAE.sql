CREATE TABLE Libro (
    isbn INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(30) NOT NULL,
    fecha_publicacion DATE NOT NULL
);

CREATE TABLE Editorial(
    id_editorial INTEGER PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL
);

CREATE TABLE Autor(
    id_autor INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL
);

CREATE TABLE categoria(
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(40) NOT NULL
);

CREATE TABLE libro_autor (
    id_lib_aut INT AUTO_INCREMENT PRIMARY KEY,
    autor_id INT NOT NULL,
    libro_isbn INT NOT NULL,

    CONSTRAINT fk_lib_aut_aut
        FOREIGN KEY (autor_id)
        REFERENCES autor (id),
    CONSTRAINT fk_lib_aut_lib
        FOREIGN KEY (libro_isbn)
        REFERENCES libro (isbn)
);

CREATE TABLE libro_editorial (
    id_lib_edi INT AUTO_INCREMENT PRIMARY KEY,
    editorial_id INT NOT NULL,
    libro_id INT NOT NULL,

    CONSTRAINT fk_lib_edi_edi
        FOREIGN KEY (editorial_id)
        REFERENCES editorial (id),
    CONSTRAINT fk_lib_edi_lib
        FOREIGN KEY (libro_id)
        REFERENCES libro (isbn)
);

CREATE TABLE libro_categoria (
    id_lib_cat INT AUTO_INCREMENT PRIMARY KEY,
    categoria_id INT NOT NULL,
    libro_id INT NOT NULL,

    CONSTRAINT fk_lib_cat_cat
        FOREIGN KEY (categoria_id)
        REFERENCES categoria (id),
    CONSTRAINT fk_lib_cat_lib
        FOREIGN KEY (libro_id)
        REFERENCES libro (isbn)
);