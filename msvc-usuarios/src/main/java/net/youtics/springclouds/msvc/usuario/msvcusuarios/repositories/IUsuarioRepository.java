package net.youtics.springclouds.msvc.usuario.msvcusuarios.repositories;

import net.youtics.springclouds.msvc.usuario.msvcusuarios.models.entity.Usuario;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUsuarioRepository extends CrudRepository<Usuario, Long> {

    //Consulta personalizada con palabra clave findBy->algo
    Optional<Usuario> findByEmail(String email);

    //Misma validacion pero con query
    @Query("Select u From Usuario u Where u.email = ?1")
    Optional<Usuario> porEmail(String email);

    //Utilizando palabra clave exists
    boolean existsByEmail(String email);

}
