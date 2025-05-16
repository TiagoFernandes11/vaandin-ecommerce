package study.course.VaadinStudy.audit;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import study.course.VaadinStudy.services.UsuarioService;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Autowired
    private AuthenticationContext authenticationContext;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public Optional<String> getCurrentAuditor() {
        return authenticationContext.isAuthenticated() ?
                Optional.of(usuarioService.find(authenticationContext.getPrincipalName().orElse(null)).getNomeCompleto())
                : Optional.of("Anonymous");
    }
}
