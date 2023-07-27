package net.youtics.springcloud.msvc.cursos.clients;

import net.youtics.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url="localhost:8081")
public interface IUsuarioClientRest {

    @GetMapping("/{id}")
    Usuario getById(@PathVariable Long id);

    @PostMapping("/")
    Usuario guardar(@RequestBody Usuario usuario);

    @GetMapping("/usuarios-curso")
    List<Usuario> listarUsuariosCurso(@RequestParam Iterable<Long> ids);

}
