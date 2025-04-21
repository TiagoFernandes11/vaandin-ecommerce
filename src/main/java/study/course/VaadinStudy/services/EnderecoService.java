package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Endereco;
import study.course.VaadinStudy.repository.EnderecoRepository;

import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    public void save(Endereco enderecoPedido) {
        enderecoRepository.save(enderecoPedido);
    }

    public boolean exists(Endereco endereco){
        Optional<Endereco> endereco1 = enderecoRepository.findByLogradouroAndNumero(endereco.getLogradouro(), endereco.getNumero());
        return endereco1.isPresent();
    }
    
    public Endereco find(String logradouro, Long numero){
        Optional<Endereco> endereco = enderecoRepository.findByLogradouroAndNumero(logradouro, numero);
        return endereco.orElse(null);
    }
}
