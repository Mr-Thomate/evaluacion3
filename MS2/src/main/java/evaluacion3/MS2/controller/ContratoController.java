package evaluacion3.MS2.controller;

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

import evaluacion3.MS2.model.Contrato;
import evaluacion3.MS2.service.ContratoService;
import evaluacion3.MS2.dto.ContratoDTO;

@RestController
@RequestMapping("api/v1/contratos")
public class ContratoController {
    
    @Autowired
    private ContratoService contratoService;

    // Retornar lista Contratos
    @GetMapping
    public ResponseEntity<List<ContratoDTO>> obtenerTodosContratos() {
        List<ContratoDTO> listaContratos = contratoService.obtenerTodos();
        if (listaContratos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listaContratos, HttpStatus.OK);
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<ContratoDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            ContratoDTO contrato = contratoService.buscarPorId(id);
            return new ResponseEntity<>(contrato, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Guardar nuevo contrato
    @PostMapping
    public ResponseEntity<Contrato> guardarcontrato(@RequestBody Contrato nuevocontrato) {
        try {
            Contrato guardado = contratoService.guardar(nuevocontrato);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Editar Contrato
    @PatchMapping("/{id}")
    public ResponseEntity<Contrato> editarContrato(@PathVariable Integer id, @RequestBody Contrato contrato) {
        try {
            Contrato editado = contratoService.guardar(contrato);
            return new ResponseEntity<>(editado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // actualizar Contrato
    @PutMapping("/{id}")
    public ResponseEntity<Contrato> actualizarContrato(@PathVariable Integer id, @RequestBody Contrato contrato) {
        try {
            Contrato editado = contratoService.actualizar(id, contrato);
            return new ResponseEntity<>(editado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar contrato
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarContrato(@PathVariable Integer id) {
        String resultado = contratoService.eliminar(id);
        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    // Buscar por empleado
    @GetMapping("/contrato/{id}")
    public ResponseEntity<List<ContratoDTO>> buscarPorIdEmpleado(@PathVariable Integer id) {
        List<ContratoDTO> listaEmpleado = contratoService.buscarPorIdEmpleado(id);
        if (listaEmpleado.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listaEmpleado, HttpStatus.OK);
    }
}
