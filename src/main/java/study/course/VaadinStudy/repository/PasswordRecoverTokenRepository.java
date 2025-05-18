package study.course.VaadinStudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.course.VaadinStudy.entities.PasswordRecoveryToken;

import java.util.UUID;

@Repository
public interface PasswordRecoverTokenRepository extends JpaRepository<PasswordRecoveryToken, UUID> {
}
