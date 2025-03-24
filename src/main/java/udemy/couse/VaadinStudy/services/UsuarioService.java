package udemy.couse.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import udemy.couse.VaadinStudy.entities.Usuario;
import udemy.couse.VaadinStudy.repository.UsuarioRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario find(String email){
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
        return optionalUsuario.orElse(null);
    }

    public Usuario find(long id){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        return optionalUsuario.orElse(null);
    }

    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    public boolean register(Usuario usuario){
        if(usuarioRepository.findByEmail(usuario.getEmail()).isPresent()){
            return false;
        }
        usuarioRepository.save(usuario);
        return true;
    }

    public boolean update(Usuario usuario){
        Optional<Usuario> optionalUsuarioDB = usuarioRepository.findByEmail(usuario.getEmail().trim());
        if(optionalUsuarioDB.isPresent()){
            if(Objects.equals(optionalUsuarioDB.get().getId(), usuario.getId())){;
                usuarioRepository.save(usuario);
                return true;
            } else {
                return false;
            }
        } else {
            usuarioRepository.save(usuario);
            return true;
        }
    }

    public void delete(Usuario usuario){
        usuarioRepository.delete(usuario);
    }
}
