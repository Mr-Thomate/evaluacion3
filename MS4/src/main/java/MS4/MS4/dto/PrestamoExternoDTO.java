package MS4.MS4.dto;

import lombok.Data;

@Data
public class PrestamoExternoDTO {
    private Integer idPrestamo;
    private String fechaIncio;
    private String fechaFin;
    private String estado;
    private String nombreCliente;
    private String tituloLibro;
    private String nombreBiblioteca;
}