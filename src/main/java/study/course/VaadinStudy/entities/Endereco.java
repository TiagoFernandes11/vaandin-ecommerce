package study.course.VaadinStudy.entities;

import com.nimbusds.jose.shaded.gson.annotations.SerializedName;
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

    @SerializedName(value = "localidade")
    private String cidade;

    @SerializedName(value = "uf")
    private String estado;

    private String bairro;

    @ManyToOne
    private Usuario usuario;
}
