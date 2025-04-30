package study.course.VaadinStudy.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endereco extends BaseEntity{

    private String cep;
    private String logradouro;
    private Long numero;
    private String cidade;
    private String estado;
    private String bairro;
}
