package net.youtics.springcloud.msvc.cursos.repositories;

import net.youtics.springcloud.msvc.cursos.models.entity.Curso;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ICursosRepository extends CrudRepository<Curso, Long> {

    @Modifying //esta anotacion se utiliza cuando vamos a modificar la base de datos insert, update y delete
    @Query("Delete From CursoUsuario cu where cu.idUsuario = ?1")
    void eliminarCursoUsuarioPorId(Long id);
}
