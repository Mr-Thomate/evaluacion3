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
