package Evaluacion3.MS1.controller;

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

import Evaluacion3.MS1.dto.PrestamoDTO;
import Evaluacion3.MS1.model.Prestamo;
import Evaluacion3.MS1.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/prestamos")
@Tag(name = "Prestamo Controller", description = "Orquestador distribuido de Préstamos")
public class PrestamoController {
    
    @Autowired
    private PrestamoService prestamoService;

    @Operation(summary = "Obtener todos los prestamos")
    @GetMapping
    public ResponseEntity<List<PrestamoDTO>> obtenerTodosPrestamos() {
        List<PrestamoDTO> resultado = prestamoService.obtenerTodos();
        if (resultado.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Buscar prestamo por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoDTO> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(prestamoService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Generar un nuevo registro de prestamo con tipo DATE")
    @PostMapping
    public ResponseEntity<Prestamo> agregarPrestamo(@Valid @RequestBody Prestamo prestamo) {
        try {
            return new ResponseEntity<>(prestamoService.guardar(prestamo), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar un prestamo por ID")
    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> actualizarPrestamo(@PathVariable Integer id, @Valid @RequestBody Prestamo prestamo) {
        try {
            return ResponseEntity.ok(prestamoService.actualizar(id, prestamo));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un prestamo por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPrestamo(@PathVariable Integer id) {
        String resultado = prestamoService.eliminar(id);
        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}