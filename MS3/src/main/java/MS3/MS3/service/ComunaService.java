package MS3.MS3.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MS3.MS3.dto.ComunaDTO;
import MS3.MS3.model.Comuna;
import MS3.MS3.repository.ComunaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    public List<ComunaDTO> obtenerTodas() {
        return comunaRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public ComunaDTO buscarPorId(Integer id) {
        return comunaRepository.findById(id).map(this::convertirADTO).orElseThrow(() -> new RuntimeException("Error: No se encontró la comuna con el ID: " + id));
    }

    public List<ComunaDTO> buscarBibliotecasEnComuna(String nombreBiblioteca){
        return comunaRepository.findByNombreBiblioteca(nombreBiblioteca).stream().map(this::convertirADTO).collect(Collectors.toList());
    }
    
    public Comuna guardar(Comuna comuna) {
        return comunaRepository.save(comuna);
    }

    public Comuna actualizar(Integer id, Comuna comuna) {
        Comuna com = comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La comuna no existe."));
        if (comuna.getNombreComuna() != null) {
            com.setNombreComuna(comuna.getNombreComuna());
        }
        if (comuna.getRegion() != null) {
            com.setRegion(comuna.getRegion());
        }
        return comunaRepository.save(com);
    }

    public String eliminar(Integer id) {
        try {
            Comuna comuna = comunaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se encuentra la comuna"));
            comunaRepository.delete(comuna);
            return "La comuna " + comuna.getNombreComuna() + " ha sido eliminada con exito.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private ComunaDTO convertirADTO(Comuna comuna) {
        ComunaDTO dto = new ComunaDTO();
        dto.setIdComuna(comuna.getIdComuna());
        dto.setNombreComuna(comuna.getNombreComuna());
        if (comuna.getRegion() != null) {
            dto.setRegion(comuna.getRegion().getNombreRegion());
        }
        if (comuna.getBiblioteca() != null) {
            dto.setNombreBibliotecas(comuna.getBiblioteca().stream().map(bib -> bib.getNombreBiblioteca()).collect(Collectors.toList()));
        }

        return dto;
    }
}