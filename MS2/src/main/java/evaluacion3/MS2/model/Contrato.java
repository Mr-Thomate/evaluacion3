package evaluacion3.MS2.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "contrato")
@AllArgsConstructor
@NoArgsConstructor
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El contrato debe tener un tipo de contrato")
    @Size(min = 5, max = 15, message = "El tipo de contrato debe tener entre 5 y 15 caracteres")
    @Column(nullable = false, length = 15)
    private String tipoContrato;

    @NotBlank(message = "El contrato debe tener fecha de inicio")
    @Size(min = 10, max = 10, message = "La fecha de inicio debe tener formato 'dd-mm-yyyy'")
    @Column(nullable = false, length = 10)
    private LocalDate fechaInicio;

    @Size(min = 10, max = 10, message = "La fecha de fin debe tener formato 'dd-mm-yyyy'")
    @Column(nullable = true, length = 10)
    private LocalDate fechaFin;

    @NotNull(message = "El contrato debe incluir un sueldo")
    @Size(max = 8, message = "El sueldo no puede tener mas de 8 digitos")
    @Column(nullable = false, length = 8)
    private Integer sueldo;

    @OneToOne(mappedBy = "contrato")
    private Empleado empleado;
}
