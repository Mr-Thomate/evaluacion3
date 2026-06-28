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

INSERT INTO libro (titulo, fecha_publicacion) VALUES
('Don Quijote', '16-01-1605'),
('Cien Anos Soledad', '05-06-1967'),
('La Odisea', '01-01-0800'),
('El Principito', '06-04-1943'),
('1984', '08-06-1949');

INSERT INTO autor (nombre) VALUES
('Miguel de Cervantes'),
('Gabriel Garcia Marquez'),
('Homero'),
('Antoine de Saint Exupery'),
('George Orwell');

INSERT INTO editorial (nombre) VALUES
('Planeta'),
('Sudamericana'),
('Alianza Editorial'),
('Salamandra'),
('Debolsillo');

INSERT INTO categoria (nombre) VALUES
('Novela Clasica'),
('Realismo Magico'),
('Epica Griega'),
('Literatura Infantil'),
('Distopia');

-- Tabla intermedia: libro_autor
INSERT INTO libro_autor (autor_id, libro_isbn) VALUES
(1, 1),  -- Cervantes       Don Quijote
(2, 2),  -- Garcia Marquez  Cien Anos Soledad
(3, 3),  -- Homero          La Odisea
(4, 4),  -- Saint Exupery   El Principito
(5, 5);  -- Orwell          1984

-- Tabla intermedia: libro_editorial
INSERT INTO libro_editorial (editorial_id, libro_id) VALUES
(3, 1),  -- Alianza         Don Quijote
(2, 2),  -- Sudameric.      Cien Anos Soledad
(3, 3),  -- Alianza         La Odisea
(4, 4),  -- Salamandra      El Principito
(5, 5);  -- Debolsillo      1984

-- Tabla intermedia: libro_categoria
INSERT INTO libro_categoria (categoria_id, libro_id) VALUES
(1, 1),  -- Novela Clasica      Don Quijote
(2, 2),  -- Realismo Magico     Cien Anos Soledad
(3, 3),  -- Epica Griega        La Odisea
(4, 4),  -- Lit. Infantil       El Principito
(5, 5);  -- Distopia            1984