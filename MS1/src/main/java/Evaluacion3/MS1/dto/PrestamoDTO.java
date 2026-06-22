package Evaluacion3.MS1.dto;

import lombok.Data;

@Data
public class PrestamoDTO {
    private Integer idPrestamo;
    private String fechaIncio; 
    private String fechaFin;   
    private String estado;
    private Integer clienteIdCliente;
    private String nombreClienteCompleto; 
    private Integer libroId;      
    private Integer bibliotecaId; 
}