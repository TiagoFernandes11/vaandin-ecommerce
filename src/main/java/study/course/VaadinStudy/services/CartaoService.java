package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Cartao;
import study.course.VaadinStudy.repository.CartaoRepository;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    public void save(Cartao cartao){
        if(cartaoRepository.findByNumero(cartao.getNumero()).isEmpty()){
            cartaoRepository.save(cartao);
        }
    }
}
