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

import evaluacion3.MS2.dto.EmpleadoDTO;
import evaluacion3.MS2.model.Empleado;
import evaluacion3.MS2.service.EmpleadoService;

@RestController
@RequestMapping("api/v1/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> obtenerTodosEmpleados() {
        List<EmpleadoDTO> listaEmpleados = empleadoService.obtenerTodos();
        if (listaEmpleados.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listaEmpleados, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Empleado> guardarEmpleado(@RequestBody Empleado nuevoEmpleado) {
        try {
            Empleado guardado = empleadoService.guardar(nuevoEmpleado);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable Integer id, @RequestBody Empleado empleado) {
        try {
            Empleado editado = empleadoService.actualizar(id, empleado);
            return new ResponseEntity<>(editado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Empleado> editarEmpleado(@PathVariable Integer id, @RequestBody Empleado empleado) {
        try {
            Empleado editado = empleadoService.actualizar(id, empleado);
            return new ResponseEntity<>(editado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable Integer id) {
        try {
            empleadoService.eliminar(id);
            return new ResponseEntity<>("Empleado eliminado con éxito", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error al eliminar", HttpStatus.NOT_FOUND);
        }
    }
}
