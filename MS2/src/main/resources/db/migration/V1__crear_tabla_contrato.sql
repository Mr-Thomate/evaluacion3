CREATE TABLE contrato (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_contrato VARCHAR(15) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    sueldo INT NOT NULL,
    empleado_id INT NOT NULL
);

CREATE TABLE empleado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    p_nombre VARCHAR(15) NOT NULL,
    s_nombre VARCHAR(15),
    p_apellido VARCHAR(15) NOT NULL,
    s_apellido VARCHAR(15),
    fecha_nacimiento DATE NOT NULL,
    CONSTRAINT fk_contrato_id
        FOREIGN KEY (contrato_id) REFERENCES contrato(id),
    FOREIGN KEY (biblioteca_id) REFERENCES biblioteca(id)
);
