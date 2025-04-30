package study.course.VaadinStudy.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Categoria extends BaseEntity{

    private String nome;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "categorias_produtos",
            joinColumns = @JoinColumn(name = "id_categoria"),
            inverseJoinColumns = @JoinColumn(name = "id_produto")
    )
    private List<Produto> produtos;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(getId(), categoria.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
