package study.course.VaadinStudy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "idproduto")
    @ManyToOne(fetch = FetchType.EAGER)
    private Produto produto;

    private int quantidade;

    private Double subTotal;
}
