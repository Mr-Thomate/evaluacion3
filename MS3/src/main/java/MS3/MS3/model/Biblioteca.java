package MS3.MS3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "biblioteca")
public class Biblioteca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBiblioteca;

    @NotBlank(message = "El nombre de la biblioteca no puede estar vacío")
    private String nombreBiblioteca;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "id_comuna")
    private Comuna comuna;
}