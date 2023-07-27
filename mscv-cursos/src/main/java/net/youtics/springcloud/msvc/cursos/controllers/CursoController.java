package net.youtics.springcloud.msvc.cursos.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import net.youtics.springcloud.msvc.cursos.models.Usuario;
import net.youtics.springcloud.msvc.cursos.models.entity.Curso;
import net.youtics.springcloud.msvc.cursos.services.ICursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CursoController {

    @Autowired
    private ICursoService servicio;

    @GetMapping
    public ResponseEntity<List<Curso>> listar ()
    {
        return ResponseEntity.ok(servicio.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id)
    {
        Optional<Curso> response = servicio.porIdConUsuarios(id); //servicio.getById(id);
        if(response.isPresent())
        {
            return ResponseEntity.ok(response.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result)
    {
        if(result.hasErrors())
        {
            return validar(result);
        }

        Curso cursoDB = servicio.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDB);
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err->{
            errores.put(err.getField(), "Error en el campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id){
        if(result.hasErrors())
        {
            return validar(result);
        }
        Optional<Curso> cursoDB = servicio.getById(id);
        if(cursoDB.isPresent())
        {
            cursoDB.get().setNombre(curso.getNombre());
            servicio.guardar(cursoDB.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoDB.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id)
    {
        Optional<Curso> cursoDB = servicio.getById(id);
        if(cursoDB.isPresent()) {
             servicio.eliminar(id);
             return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> usu = null;
        try{

            usu=servicio.matricularUsuario(usuario, cursoId);
        }catch (FeignException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("msg", "No existe usuario o error en la comunicación | " + e.getMessage()));
        }

        if(usu.isPresent())
        {
            return ResponseEntity.status(HttpStatus.CREATED).body(usu.get());
        }
        return  ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> usu = null;
        try{

            usu=servicio.crearUsuario(usuario, cursoId);
        }catch (FeignException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("msg", "No se pudo crear el usuario o error en la comunicación | " + e.getMessage()));
        }

        if(usu.isPresent())
        {
            return ResponseEntity.status(HttpStatus.CREATED).body(usu.get());
        }
        return  ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> usu = null;
        try{

            usu=servicio.eliminarUsuario(usuario, cursoId);
        }catch (FeignException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("msg", "No existe usuario o error en la comunicación | " + e.getMessage()));
        }

        if(usu.isPresent())
        {
            return ResponseEntity.status(HttpStatus.OK).body(usu.get());
        }
        return  ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id){
        servicio.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }


}
