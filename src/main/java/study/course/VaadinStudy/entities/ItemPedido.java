package study.course.VaadinStudy.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido extends BaseEntity{

    @JoinColumn(name = "idproduto")
    @ManyToOne(fetch = FetchType.EAGER)
    private Produto produto;

    private int quantidade;

    private Double subTotal;
}
