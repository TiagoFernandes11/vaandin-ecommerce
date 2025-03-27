package study.course.VaadinStudy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.services.UsuarioService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String senha = authentication.getCredentials().toString();
        Usuario usuarioDB = usuarioService.find(email);
        if(Objects.isNull(usuarioDB)){
            throw new BadCredentialsException("Não existe um usuario com esse email");
        }
        if(passwordEncoder.matches(senha, usuarioDB.getSenha())){
            return new UsernamePasswordAuthenticationToken(email, senha, getAuthorities(usuarioDB));
        }
        throw new BadCredentialsException("A senha está incorreta");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public List<SimpleGrantedAuthority> getAuthorities(Usuario usuario){
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(usuario.getRole()));
        return grantedAuthorities;
    }
}
