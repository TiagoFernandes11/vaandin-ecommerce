package study.course.VaadinStudy.services;

import java.util.List;

public interface Service<T> {

    T find(Long idEntidade);

    Boolean exists(Long idEntidade);

    void save(T entidade);

    List<T> findAll();

    void update(T entidade);

    void delete(T entidade);
}
