package MS3.MS3.dto;

import java.util.List;
import lombok.Data;

@Data
public class ComunaDTO {
    private Integer idComuna;
    private String nombreComuna;
    private String region;
    private List<String> nombreBibliotecas; //mostrar el nombre de las bibliotecas que se encuentran en la comuna
}