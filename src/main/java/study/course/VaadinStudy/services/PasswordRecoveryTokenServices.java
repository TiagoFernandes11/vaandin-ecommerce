package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.PasswordRecoveryToken;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.repository.PasswordRecoverTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordRecoveryTokenServices {

    @Autowired
    private PasswordRecoverTokenRepository repository;

    public PasswordRecoveryToken getToken(UUID uuid){
        return repository.findById(uuid).orElse(null);
    }

    public PasswordRecoveryToken createToken(Usuario usuario){
        PasswordRecoveryToken token = new PasswordRecoveryToken();
        token.setUuid(UUID.randomUUID());
        token.setUsuario(usuario);
        token.setExpiration(LocalDateTime.now().plusMinutes(20));
        token.setActive(true);
        repository.save(token);
        return token;
    }

    public void save(PasswordRecoveryToken passwordRecoveryToken){
        this.repository.save(passwordRecoveryToken);
    }

    public boolean validateToken(UUID token){
        PasswordRecoveryToken savedToken = repository.findById(token).orElse(null);
        return savedToken != null
                && savedToken.isActive()
                && savedToken.getExpiration().isAfter(LocalDateTime.now());
    }
}
