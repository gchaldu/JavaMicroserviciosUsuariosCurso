package net.youtics.springclouds.msvc.usuario.msvcusuarios.services;

import net.youtics.springclouds.msvc.usuario.msvcusuarios.clients.CursoClienteRest;
import net.youtics.springclouds.msvc.usuario.msvcusuarios.models.entity.Usuario;
import net.youtics.springclouds.msvc.usuario.msvcusuarios.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class UsuarioServiceImpl implements IUsuarioService{
    @Autowired
    private IUsuarioRepository repository;
    @Autowired
    private CursoClienteRest clienteRest;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        repository.deleteById(id);
        clienteRest.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> getByEmail(String email) {
        return repository.porEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAllById(Iterable<Long> ids) {
        return (List<Usuario>) repository.findAllById(ids);
    }
}
