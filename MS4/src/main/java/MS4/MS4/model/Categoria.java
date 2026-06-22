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
@Table(name = "categoria")
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "La categoria debe tener nombre")
    @Size(min = 3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres")
    @Column(nullable = false, length = 40)
    private String nombre;

    @OneToMany(mappedBy = "categoria")
    private List<LibroCategoria> libroCategoria;
}