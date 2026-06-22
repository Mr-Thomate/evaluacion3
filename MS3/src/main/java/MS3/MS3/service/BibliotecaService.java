package MS3.MS3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MS3.MS3.dto.BibliotecaDTO;
import MS3.MS3.model.Biblioteca;
import MS3.MS3.repository.BibliotecaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BibliotecaService {

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    public List<BibliotecaDTO> obtenerTodas() {
        List<BibliotecaDTO> listaDTO = new ArrayList<>();
        for (Biblioteca bib : bibliotecaRepository.findAll()) {
            listaDTO.add(convertirADTO(bib));
        }
        return listaDTO;
    }

    public BibliotecaDTO buscarPorId(Integer id) {
        Biblioteca bib = bibliotecaRepository.findById(id).orElse(null);
        return (bib != null) ? convertirADTO(bib) : null;
    }

    public Biblioteca guardar(Biblioteca biblioteca) {
        return bibliotecaRepository.save(biblioteca);
    }

    public Biblioteca actualizar(Integer id, Biblioteca biblioteca) {
        Biblioteca bib = bibliotecaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La biblioteca no existe."));
        if (biblioteca.getNombreBiblioteca() != null) {
            bib.setNombreBiblioteca(biblioteca.getNombreBiblioteca());
        }
        if (biblioteca.getDireccion() != null) {
            bib.setDireccion(biblioteca.getDireccion());
        }
        if (biblioteca.getComuna() != null) {
            bib.setComuna(biblioteca.getComuna());
        }
        return bibliotecaRepository.save(bib);
    }

    public String eliminar(Integer id) {
        try {
            Biblioteca bib = bibliotecaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se puede eliminar la biblioteca, pues no existe."));
            bibliotecaRepository.delete(bib);
            return "La biblioteca " + bib.getNombreBiblioteca() + " ha sido eliminada con exito.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public BibliotecaDTO buscarPorIdPrestamo(Integer idPrestamo) {
        return bibliotecaRepository.findByIdPrestamo(idPrestamo)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("No se encontró biblioteca asociada al préstamo ID: " + idPrestamo));
    }

    public List<BibliotecaDTO> buscarPorIdComuna(Integer idComuna) {
        List<Biblioteca> bibliotecas = bibliotecaRepository.findByIdComuna(idComuna);
        if (bibliotecas.isEmpty()) {
            throw new RuntimeException("No se encontraron bibliotecas en la comuna ID: " + idComuna);
        }
        return bibliotecas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public BibliotecaDTO buscarPorIdEmpleado(Integer idEmpleado) {
        return bibliotecaRepository.findByIdEmpleado(idEmpleado)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("No se encontró biblioteca para el empleado ID: " + idEmpleado));
    }

    private BibliotecaDTO convertirADTO(Biblioteca bib) {
        BibliotecaDTO dto = new BibliotecaDTO();
        dto.setId(bib.getIdBiblioteca());
        dto.setNombreBiblioteca(bib.getNombreBiblioteca());
        dto.setDireccion(bib.getDireccion());
        if (bib.getComuna() != null) {
            dto.setNombreComuna(bib.getComuna().getNombreComuna());
        }
        return dto;
    }
}