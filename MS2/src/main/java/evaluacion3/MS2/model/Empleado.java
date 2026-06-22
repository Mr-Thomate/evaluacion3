package evaluacion3.MS2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "empleado")
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El empleado debe tener primer nombre")
    @Size(min = 3, max = 15, message = "El primer nombre debe tener entre 3 y 15 caracteres.")
    @Column(nullable = false, length = 15)
    private String pnombre;

    @Column(nullable = true, length = 15)
    private String snombre;

    @NotBlank(message = "El empleado debe tener primer apellido")
    @Size(min = 3, max = 15, message = "El primer apellido debe tener entre 3 y 15 caracteres.")
    @Column(nullable = false, length = 15)
    private String papellido;

    @Column(nullable = true, length = 15)
    private String sapellido; 

    @NotBlank(message = "El empleado debe registrar su fecha de nacimiento")
    @Size(min = 10, max = 10, message = "La fecha de nacimiento debe estar en formato 'dd-mm-yyyy'")
    @Column(nullable = false, length = 10)
    private String fechaNacimiento;

    @OneToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    @ManyToOne
    @JoinColumn(name = "biblioteca_id")
    private Integer bibliotecaId;
}
