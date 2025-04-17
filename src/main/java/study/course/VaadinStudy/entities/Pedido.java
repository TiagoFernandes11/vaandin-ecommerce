package study.course.VaadinStudy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idCliente;

    private String status;

    private Date dataCriacao;

    private boolean pago;

    private Date dataPagamento;

    private Date dataEntrega;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ItemPedido> itens;

    private Double total;
}
