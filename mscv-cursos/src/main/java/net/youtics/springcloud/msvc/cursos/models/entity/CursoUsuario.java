package net.youtics.springcloud.msvc.cursos.models.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "cursos_usuarios")
public class CursoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_usuario", unique = true)
    private Long idUsuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean equals(Object obj) {
        //Pregunta si las dos referencias son las mismas
        if(this == obj)
            return true;
        //se fija se es la clase CursoUsuario
        if(!(obj instanceof CursoUsuario) )
            return false;
        //si pasa, se fija q el id no sea nulo y que el id no sea el mismo
        return ((CursoUsuario) obj).getId().equals(this.idUsuario) && this.idUsuario!=null;
    }
}
