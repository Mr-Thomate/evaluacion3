package evaluacion3.MS2.model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name="tipo_contrato", nullable = false, length = 15)
    private String tipoContrato;

    @NotNull(message = "El contrato debe tener fecha de inicio")
    @Column(name="fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name="fecha_fin", nullable = true)
    private LocalDate fechaFin;

    @NotNull(message = "El contrato debe incluir un sueldo")
    @Column(nullable = false)
    private Integer sueldo;

    @OneToOne(mappedBy = "contrato")
    @JsonIgnore
    private Empleado empleado;
}