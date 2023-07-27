package net.youtics.springcloud.msvc.cursos.services;

import net.youtics.springcloud.msvc.cursos.clients.IUsuarioClientRest;
import net.youtics.springcloud.msvc.cursos.models.Usuario;
import net.youtics.springcloud.msvc.cursos.models.entity.Curso;
import net.youtics.springcloud.msvc.cursos.models.entity.CursoUsuario;
import net.youtics.springcloud.msvc.cursos.repositories.ICursosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements ICursoService{

    @Autowired
    private ICursosRepository repository;

    @Autowired
    private IUsuarioClientRest clientRest;
    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> getById(Long id) {

        return repository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> o = repository.findById(id);
        if(o.isPresent()){
            Curso curso = o.get();
            if(curso.getCursoUsuarios().size()>0)
            {
                List<Long> ids = curso.getCursoUsuarios().stream().map(cu->cu.getIdUsuario()).collect(Collectors.toList());
                List<Usuario> listUsuarios = clientRest.listarUsuariosCurso(ids);
                curso.setUsuarios(listUsuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        repository.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> matricularUsuario(Usuario usuario, Long idCurso) {
            Optional<Curso> curso = repository.findById(idCurso);
            if(curso.isPresent())
            {
                Usuario usuarioMsvc = clientRest.getById(usuario.getId());
                Curso c = curso.get();
                CursoUsuario cursoUsuario = new CursoUsuario();
                cursoUsuario.setIdUsuario(usuarioMsvc.getId());
                c.agregarCursoUsuario(cursoUsuario);
                repository.save(c);
                return Optional.of(usuarioMsvc);
            }
            return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long idCurso) {
        Optional<Curso> curso = repository.findById(idCurso);
        if(curso.isPresent())
        {
            Usuario usuarioNuevo = clientRest.guardar(usuario);
            Curso c = curso.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setIdUsuario(usuarioNuevo.getId());
            c.agregarCursoUsuario(cursoUsuario);
            repository.save(c);
            return Optional.of(usuarioNuevo);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long idCurso) {
        Optional<Curso> curso = repository.findById(idCurso);
        if(curso.isPresent())
        {
            Usuario usuarioMsvc = clientRest.getById(usuario.getId());
            Curso c = curso.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setIdUsuario(usuarioMsvc.getId());
            c.eliminarCursoUsuario(cursoUsuario);
            //guarda el curso actualizado
            repository.save(c);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }


}
