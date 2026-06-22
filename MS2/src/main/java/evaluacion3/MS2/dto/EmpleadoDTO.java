package evaluacion3.MS2.dto;

import lombok.Data;

@Data
public class EmpleadoDTO {
    private Integer idEmpleado;
    private String pnombre;
    private String snombre;
    private String papellido;
    private String sapellido;
    private String estadoContrato;
    private BibliotecaExternoDTO biblioteca;
}
