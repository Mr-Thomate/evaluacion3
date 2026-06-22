package MS3.MS3.dto;

import java.util.List;
import lombok.Data;

@Data
public class BibliotecaDTO {
    private Integer id;
    private String nombreBiblioteca;
    private String direccion;
    private String nombreComuna;
    private List<String> clienteConPrestamo; //mostrar el nombre de los clientes que tienen un prestamo activo en la biblioteca
    private List<String> nombreEmpleados;
}