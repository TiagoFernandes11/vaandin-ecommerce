package study.course.VaadinStudy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario extends BaseEntity{

    private String nomeCompleto;
    private String email;
    private String senha;
    private String role;

    @ManyToOne
    private Endereco enderecoPrincipal;
}
