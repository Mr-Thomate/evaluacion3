package Evaluacion3.MS1.dto;

import lombok.Data;

@Data
public class LibroExternoDTO {
    private Integer isbn;
    private String titulo;
    private String fechaPublicacion;
    private String categoria;
    private String nombreEditorial;
    private String nombreAutor;
}