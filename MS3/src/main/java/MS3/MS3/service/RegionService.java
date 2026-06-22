package MS3.MS3.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MS3.MS3.dto.RegionDTO;
import MS3.MS3.model.Region;
import MS3.MS3.repository.RegionRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<RegionDTO> obtenerTodas() {
        return regionRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public RegionDTO buscarPorId(Integer id) {
        return regionRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Error: No se encontró la región con el ID: " + id));
    }

    public Region guardar(Region region) {
        return regionRepository.save(region);
    }

    public Region actualizar(Integer id, Region region) {
        Region r = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La región no existe."));
        
        if (region.getNombreRegion() != null) r.setNombreRegion(region.getNombreRegion());
        
        return regionRepository.save(r);
    }

    public String eliminar(Integer id) {
        try {
            Region region = regionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se puede eliminar la región, pues no existe."));
            regionRepository.delete(region);
            return "La región " + region.getNombreRegion() + " ha sido eliminada con exito.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private RegionDTO convertirADTO(Region region) {
        RegionDTO dto = new RegionDTO();
        dto.setIdRegion(region.getIdRegion());
        dto.setNombreRegion(region.getNombreRegion());
        
        if (region.getComuna() != null) {
            dto.setNombreComunas(region.getComuna().stream()
                .map(com -> com.getNombreComuna())
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
}