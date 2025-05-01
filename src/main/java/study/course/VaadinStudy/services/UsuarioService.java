package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.repository.UsuarioRepository;
import study.course.VaadinStudy.services.base.BaseService;

import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioService extends BaseService<Usuario> {

    private UsuarioRepository usuarioRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario find(String email){
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
        return optionalUsuario.orElse(null);
    }


    public boolean create(Usuario usuario){
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        if(usuarioRepository.findByEmail(usuario.getEmail()).isPresent()){
            return false;
        }
        save(usuario);
        return true;
    }

    public boolean isUpdatable(Usuario usuario){
        Optional<Usuario> optionalUsuarioDB = usuarioRepository.findByEmail(usuario.getEmail().trim());
        if(optionalUsuarioDB.isPresent()){
            return Objects.equals(optionalUsuarioDB.get().getId(), usuario.getId());
        } else {
            return true;
        }
    }

}
