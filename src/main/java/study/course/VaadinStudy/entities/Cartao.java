package study.course.VaadinStudy.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cartao extends BaseEntity{

    private Boolean ativo;
    private Boolean removido;
    private String numero;
    private String nomeImpresso;
    private LocalDate validade;
    private String cvv;

    @ManyToOne
    private Usuario usuario;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cartao cartao = (Cartao) o;
        return Objects.equals(numero, cartao.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numero);
    }
}
