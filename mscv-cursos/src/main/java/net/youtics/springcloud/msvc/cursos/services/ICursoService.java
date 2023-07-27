package net.youtics.springcloud.msvc.cursos.services;

import net.youtics.springcloud.msvc.cursos.models.Usuario;
import net.youtics.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface ICursoService {
    /**Logica de negocios de persistencia a BD*/
    List<Curso> listar();
    Optional<Curso> getById(Long id);
    Curso guardar(Curso curso);
    void eliminar(Long id);
    Optional<Curso> porIdConUsuarios(Long id);

    void eliminarCursoUsuarioPorId(Long id);

    /**Logica de negocios pero los datos se obtienen de otro servicio*/
    Optional<Usuario> matricularUsuario(Usuario usuario,Long idCurso);
    Optional<Usuario> crearUsuario(Usuario usuario, Long idCurso);
    Optional<Usuario> eliminarUsuario(Usuario usuario, Long idCurso);

}
