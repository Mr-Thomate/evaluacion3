package Evaluacion3.MS1.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "prestamo")
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Integer id;
    
    @NotNull(message = "El prestamo debe tener fecha de inicio")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_devolucion", nullable = true)
    private LocalDate fechaDevolucion;

    @NotBlank(message = "El prestamo debe tener registrado su estado")
    @Size(min = 3, max = 15, message = "El estado debe tener entre 3 y 15 caracteres")
    @Column(nullable = false, length = 15)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "cliente_id_cliente", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El préstamo debe tener un libro asociado")
    @Column(name = "libro_id", nullable = false)
    private Integer libroId;

    @NotNull(message = "El préstamo debe tener una biblioteca asociada")
    @Column(name = "biblioteca_id", nullable = false)
    private Integer bibliotecaId;
}