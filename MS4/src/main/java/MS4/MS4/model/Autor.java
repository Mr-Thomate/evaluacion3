package MS4.MS4.model;

import jakarta.validation.constraints.Size;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "autor")
@AllArgsConstructor
@NoArgsConstructor
public class Autor {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El autor/a debe tener nombre")
    @Size(min = 3, max = 30, message = "El nombre debe estar entre 3 y 30 caracteres")
    @Column(nullable = false, length = 30)
    private String nombre;

    @OneToMany(mappedBy = "autor")
    private List<LibroAutor> libroAutor;
}