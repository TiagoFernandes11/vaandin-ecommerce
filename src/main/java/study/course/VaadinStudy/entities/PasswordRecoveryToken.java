package study.course.VaadinStudy.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import study.course.VaadinStudy.services.UsuarioService;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class PasswordRecoveryToken {

    @Id
    private UUID uuid;

    @ManyToOne
    private Usuario usuario;

    private LocalDateTime expiration;

    private boolean active;

}
