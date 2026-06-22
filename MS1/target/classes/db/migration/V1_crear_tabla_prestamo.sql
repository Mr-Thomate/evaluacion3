CREATE TABLE prestamo (
    id_prestamo INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_libro INT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion DATE NULL,
    CONSTRAINT fk_prestamo_cliente 
        FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);