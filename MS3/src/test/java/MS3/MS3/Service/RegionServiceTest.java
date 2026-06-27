package MS3.MS3.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import MS3.MS3.dto.RegionDTO;
import MS3.MS3.model.Region;
import MS3.MS3.repository.RegionRepository;
import MS3.MS3.service.RegionService;

@ExtendWith(MockitoExtension.class)
public class RegionServiceTest {

    @Mock
    private RegionRepository regionRepositoryMock;

    @InjectMocks
    private RegionService regionService;

    private Region crearRegion() {
        Region region = new Region();
        region.setIdRegion(1);
        region.setNombreRegion("Region LOL");
        return region;
    }

    @Test
    public void testObtenerTodas() {
        Region region = crearRegion();
        when(regionRepositoryMock.findAll()).thenReturn(List.of(region));
        List<RegionDTO> resultado = regionService.obtenerTodas();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Region LOL", resultado.get(0).getNombreRegion());
    }

    @Test
    public void testBuscarPorId() {
        Integer id = 1;
        Region region = crearRegion();
        when(regionRepositoryMock.findById(id)).thenReturn(Optional.of(region));

        RegionDTO resultado = regionService.buscarPorId(id);
        assertNotNull(resultado);
        assertEquals("Region LOL", resultado.getNombreRegion());
    }

    @Test
    public void testGuardar() {
        Region region = crearRegion();
        when(regionRepositoryMock.save(region)).thenReturn(region);

        Region resultado = regionService.guardar(region);
        assertNotNull(resultado);
        assertEquals("Region LOL", resultado.getNombreRegion());
    }

    @Test
    public void testActualizar() {
        Integer id = 1;
        Region regionexistente = crearRegion();
        Region cambios = new Region();
        cambios.setIdRegion(id);
        cambios.setNombreRegion("Region Actualizada");

        when(regionRepositoryMock.findById(id)).thenReturn(Optional.of(regionexistente));
        when(regionRepositoryMock.save(cambios)).thenReturn(cambios);

        Region resultado = regionService.actualizar(id, cambios);

        assertNotNull(resultado);
        assertEquals("Region Actualizada", resultado.getNombreRegion());
    }

    @Test
    public void testEliminar() {
        Integer id = 1;
        Region region = crearRegion();
        when(regionRepositoryMock.findById(id)).thenReturn(Optional.of(region));
        doNothing().when(regionRepositoryMock).delete(region);

        String resultado = regionService.eliminar(id);

        assertEquals("La región Region LOL ha sido eliminada con exito.", resultado);
        verify(regionRepositoryMock).delete(region);
    }
}

