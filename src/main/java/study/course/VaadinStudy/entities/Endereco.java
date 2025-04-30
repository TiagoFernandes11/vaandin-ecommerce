package study.course.VaadinStudy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endereco extends BaseEntity{

    private Boolean ativo;
    private Boolean removido;
    private String cep;
    private String logradouro;
    private Long numero;
    private String cidade;
    private String estado;
    private String bairro;

    @ManyToOne
    private Usuario usuario;
}
