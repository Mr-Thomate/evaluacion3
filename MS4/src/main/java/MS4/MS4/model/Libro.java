package MS4.MS4.model;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "libro")
@AllArgsConstructor
@NoArgsConstructor
public class Libro {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer isbn;

    @NotBlank(message = "El libro debe tener un titulo")
    @Size(min = 3, max = 30, message = "El titulo debe contener entre 3 y 30 caracteres")
    @Column(nullable = false, length = 30)
    private String titulo;

    @NotBlank(message = "El libro debe tener una fecha de publicacion")
    @Size(min = 10, max = 10, message = "La fecha debe estar en formato 'dd-mm-yyyy'")
    private LocalDate fechaPublicacion;

    @OneToMany(mappedBy = "libro")
    private List<LibroCategoria> libroCategoria;

    @OneToMany(mappedBy = "libro")
    private List<LibroEditorial> libroEditorial;

    @OneToMany(mappedBy = "libro")
    private List<LibroAutor> libroAutor;
}