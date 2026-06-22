package MS3.MS3.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import MS3.MS3.dto.BibliotecaDTO;
import MS3.MS3.model.Biblioteca;
import MS3.MS3.service.BibliotecaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/bibliotecas")
public class BibliotecaController {

    @Autowired
    private BibliotecaService bibliotecaService;

    @GetMapping
    public ResponseEntity<List<BibliotecaDTO>> obtenerTodas() {
        List<BibliotecaDTO> lista = bibliotecaService.obtenerTodas();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BibliotecaDTO> obtenerPorId(@PathVariable Integer id) {
        BibliotecaDTO dto = bibliotecaService.buscarPorId(id);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Biblioteca> guardarBiblioteca(@Valid @RequestBody Biblioteca biblioteca) {
        try {
            Biblioteca guardada = bibliotecaService.guardar(biblioteca);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Biblioteca> actualizarBiblioteca(@PathVariable Integer id, @Valid @RequestBody Biblioteca biblioteca) {
        try {
            Biblioteca actualizada = bibliotecaService.actualizar(id, biblioteca);
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarBiblioteca(@PathVariable Integer id) {
        String mensaje = bibliotecaService.eliminar(id);
        if (mensaje.startsWith("Error")) {
            return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }

    @GetMapping("/prestamo/{idPrestamo}")
    public ResponseEntity<BibliotecaDTO> obtenerPorPrestamo(@PathVariable Integer idPrestamo) {
        try {
            BibliotecaDTO dto = bibliotecaService.buscarPorIdPrestamo(idPrestamo);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comuna/{idComuna}")
    public ResponseEntity<List<BibliotecaDTO>> obtenerPorComuna(@PathVariable Integer idComuna) {
        try {
            List<BibliotecaDTO> lista = bibliotecaService.buscarPorIdComuna(idComuna);
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/empleado/{idEmpleado}")
    public ResponseEntity<BibliotecaDTO> obtenerPorEmpleado(@PathVariable Integer idEmpleado) {
        try {
            BibliotecaDTO dto = bibliotecaService.buscarPorIdEmpleado(idEmpleado);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}