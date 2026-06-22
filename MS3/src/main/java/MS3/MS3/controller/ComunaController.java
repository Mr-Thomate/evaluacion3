package MS3.MS3.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import MS3.MS3.dto.ComunaDTO;
import MS3.MS3.model.Comuna;
import MS3.MS3.service.ComunaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/comunas")
public class ComunaController {
    
    @Autowired
    private ComunaService comunaService;

    // Retornar lista Comunas
    @GetMapping
    public ResponseEntity<List<ComunaDTO>> obtenerTodasComunas() {
        List<ComunaDTO> listaComunas = comunaService.obtenerTodas();
        if (listaComunas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listaComunas, HttpStatus.OK);
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<ComunaDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            ComunaDTO comuna = comunaService.buscarPorId(id);
            return new ResponseEntity<>(comuna, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Guardar nueva comuna
    @PostMapping
    public ResponseEntity<Comuna> guardarcomuna(@Valid @RequestBody Comuna comunanueva) {
        try {
            Comuna guardada = comunaService.guardar(comunanueva);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Editar Comuna
    @PatchMapping("/{id}")
    public ResponseEntity<Comuna> editarComuna(@PathVariable Integer id, @Valid @RequestBody Comuna comuna) {
        try {
            Comuna editada = comunaService.guardar(comuna);
            return new ResponseEntity<>(editada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // actualizar Comuna
    @PutMapping("/{id}")
    public ResponseEntity<Comuna> actualizarComuna(@PathVariable Integer id, @Valid @RequestBody Comuna comuna) {
        try {
            Comuna editada = comunaService.actualizar(id, comuna);
            return new ResponseEntity<>(editada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar comuna
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCategoria(@PathVariable Integer id) {
        String resultado = comunaService.eliminar(id);
        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    // Buscar bibliotecas en comuna
    @GetMapping("/{comuna}")
    public ResponseEntity<List<ComunaDTO>> buscarBibliotecasEnComuna(@PathVariable String biblioteca) {
        List<ComunaDTO> listabibliotecas = comunaService.buscarBibliotecasEnComuna(biblioteca);
        if (listabibliotecas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listabibliotecas, HttpStatus.OK);
    }
}