package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.repository.UsuarioRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public boolean create(Usuario usuario){
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
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
