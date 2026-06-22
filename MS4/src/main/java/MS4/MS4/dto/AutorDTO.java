package MS4.MS4.dto;

import java.util.List;
import lombok.Data;

@Data
public class AutorDTO {
    private Integer id;
    private String nombre;
    private List<String> tituloLibros;
}