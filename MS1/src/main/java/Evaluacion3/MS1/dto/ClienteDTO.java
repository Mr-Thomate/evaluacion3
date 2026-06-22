package Evaluacion3.MS1.dto;

import java.util.List;

import lombok.Data;

@Data
public class ClienteDTO {
    private Integer id;
    private String pnombre;
    private String snombre;
    private String papellido;
    private String sapellido;
    private String fechaNacimiento;
    private String sexo;
    private List<Integer> idPrestamosAsociados;
}