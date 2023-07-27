package net.youtics.springclouds.msvc.usuario.msvcusuarios.controllers;

import jakarta.validation.Valid;
import net.youtics.springclouds.msvc.usuario.msvcusuarios.models.entity.Usuario;
import net.youtics.springclouds.msvc.usuario.msvcusuarios.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private IUsuarioService servicio;

    @GetMapping("/")
    public List<Usuario> listar(){
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        if(servicio.getById(id).isPresent())
        {
            return ResponseEntity.ok().body(servicio.getById(id).get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-curso")
    public ResponseEntity<?> listarUsuariosCurso(@RequestParam List<Long> ids)
    {
        return ResponseEntity.ok(servicio.findAllById(ids));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> guardar(@Valid @RequestBody Usuario usuario, BindingResult result){
        if(result.hasErrors()){
            return getMapResponseEntity(result);
        }

        if(servicio.getByEmail(usuario.getEmail()).isPresent())
        {
            return ResponseEntity.badRequest().body(Collections.singletonMap("email", "El campo email ya existe en la BD!"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.guardar(usuario));
    }

    private static ResponseEntity<Map<String, String>> getMapResponseEntity(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error->{
            errores.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id){

        if(result.hasErrors()){
            return getMapResponseEntity(result);
        }
        Optional<Usuario> userEncontrado = servicio.getById(id);
        if(userEncontrado.isPresent())
        {
            Usuario u = userEncontrado.get();
            if(!usuario.getEmail().isEmpty() && servicio.existeEmail(usuario.getEmail()) && !usuario.getEmail().equalsIgnoreCase(u.getEmail()))
            {
                return ResponseEntity.badRequest().body(Collections.singletonMap("email", "El campo email ya existe en la BD!"));
            }
            u.setEmail(usuario.getEmail());
            u.setUsername(usuario.getUsername());
            u.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(servicio.guardar(u));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Usuario> userEncontrado = servicio.getById(id);
        if(userEncontrado.isPresent())
        {
            servicio.eliminar(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
