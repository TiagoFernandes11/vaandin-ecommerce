package study.course.VaadinStudy.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class BaseService<T> implements Service<T>{

    protected final JpaRepository<T, Long> repository;

    public BaseService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    @Override
    public T find(Long idEntidade) {
        return repository.findById(idEntidade).orElse(null);
    }

    public Boolean exists(Long idEntidade){
        return repository.findById(idEntidade).isPresent();
    }

    @Override
    public void save(T entidade) {
        repository.save(entidade);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public void update(T entidade) {
        repository.save(entidade);
    }

    @Override
    public void delete(T entidade) {
        repository.delete(entidade);
    }
}
