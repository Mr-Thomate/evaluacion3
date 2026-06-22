package MS3.MS3;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

// imports de cada clase
import MS3.MS3.controller.BibliotecaController;
import MS3.MS3.dto.BibliotecaDTO;
import MS3.MS3.model.Biblioteca;
import MS3.MS3.repository.BibliotecaRepository;
import MS3.MS3.service.BibliotecaService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class JedisApplicationTests {

	@Mock
	private BibliotecaRepository bibliotecaRepository; // Simulamos el acceso a la base de datos
	
	@InjectMocks
	private BibliotecaService bibliotecaService; // Inyectamos el Mock anterior dentro del servicio real
	private Faker faker = new Faker(); // Nuestro generador de datos de Star Wars
	@BeforeEach
	void setUp() {
		// Inicializa los componentes de simulación antes de ejecutar cada prueba
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testBuscarPorId_Exitoso() {
		// GIVEN: Dado un escenario inicial en la galaxia
		Integer idSimulado = 42;
		String nombreAleatorio = faker.starWars().character(); // Nombre biblioteca
		String direccionAleatoria = faker.breakingBad().character(); // Nombre de la direccion
		Biblioteca bibliotecaFalsa = new Biblioteca();
		bibliotecaFalsa.setIdBiblioteca(idSimulado);
		bibliotecaFalsa.setNombreBiblioteca(nombreAleatorio);
		bibliotecaFalsa.setDireccion(direccionAleatoria);
		// Entrenamos al Mock: Cuando el repositorio busque este ID, responderá con nuestro Jedifalso
		when(bibliotecaRepository.findById(idSimulado)).thenReturn(Optional.of(bibliotecaFalsa));
		// WHEN: Cuando ejecutamos la acción del servicio que queremos evaluar

		BibliotecaDTO resultado = bibliotecaService.buscarPorId(idSimulado);
		// THEN: Entonces validamos que las compuertas de datos funcionen de forma idónea
		assertNotNull(resultado, "El DTO resultante no debería ser nulo");
		assertEquals(nombreAleatorio, resultado.getNombreBiblioteca(), "El nombre transformado al DTO debe coincidir con el de la DB");
		// Verificamos que el servicio realmente haya consultado al repositorio exactamente 1 vez
		verify(bibliotecaRepository, times(1)).findById(idSimulado);
	}

}