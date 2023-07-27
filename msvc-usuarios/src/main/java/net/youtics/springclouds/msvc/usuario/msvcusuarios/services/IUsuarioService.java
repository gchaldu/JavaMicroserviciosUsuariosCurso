package net.youtics.springclouds.msvc.usuario.msvcusuarios.services;

import net.youtics.springclouds.msvc.usuario.msvcusuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> listar();
    Optional<Usuario> getById(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar (Long id);
    Optional<Usuario> getByEmail(String email);
    boolean existeEmail(String email);
    List<Usuario> findAllById(Iterable<Long> ids);

}
