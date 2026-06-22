package MS4.MS4.dto;

import java.util.List;
import lombok.Data;

@Data
public class LibroDTO {
    private Integer isbn;
    private String titulo;
    private String fechaPublicacion;
    private String categoria;
    private String nombreEditorial;
    private String nombreAutor;
    private PrestamoExternoDTO prestamo;
    private List<PrestamoExternoDTO> nombreClientesPrestamo; //mostrar el nombre de los clientes que tienen en prestamo el libro
}