package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Cartao;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.repository.CartaoRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private UsuarioService usuarioService;

    private void salvarCartao(Cartao cartao){
        if(cartaoRepository.findByNumero(cartao.getNumero()).isEmpty()){
            cartaoRepository.save(cartao);
        }
    }

    public void salvarCartaoCliente(Cartao cartao, String email){
        List<Cartao> cartoes = this.findAllByEmail(email);
        for (Cartao cartao1 : cartoes){
            if (cartao1.equals(cartao)){
                cartao1.setAtivo(true);
                cartao1.setRemovido(false);
                this.salvarCartao(cartao1);
                return;
            }
        }
        Cartao cartaoSalvo = this.findAtivoByEmail(email).orElse(null);
        if(cartaoSalvo != null && !cartaoSalvo.equals(cartao)){
            cartaoSalvo.setAtivo(false);
            cartaoSalvo.setRemovido(true);
            this.update(cartaoSalvo);
        }
        this.salvarCartao(cartao);
    }

    public void update(Cartao cartao){
        Cartao cartaoSalvo = cartaoRepository.findByNumero(cartao.getNumero()).orElse(null);
        if(cartaoSalvo != null){
            cartaoSalvo.setAtivo(cartao.getAtivo());
            cartaoSalvo.setRemovido(cartao.getRemovido());
            cartaoSalvo.setNumero(cartao.getNumero());
            cartaoSalvo.setCvv(cartao.getCvv());
            cartaoSalvo.setValidade(cartao.getValidade());
            cartaoSalvo.setNomeImpresso(cartao.getNomeImpresso());

            cartaoRepository.save(cartaoSalvo);
        }
    }

    public List<Cartao> findAllByEmail(String emailUsuario){
        Usuario usuario = usuarioService.find(emailUsuario);
        return cartaoRepository.findAllByUsuario(usuario);
    }

    public Optional<Cartao> findAtivoByEmail(String emailUsuario){
        Usuario usuario = usuarioService.find(emailUsuario);
        if(Objects.nonNull(emailUsuario)){
            List<Cartao> cartoes = cartaoRepository.findAllByUsuario(usuario);

            if(!cartoes.isEmpty()){
                for(Cartao cartao : cartoes){
                    if(cartao.getAtivo() == true && cartao.getRemovido() == false ){
                        return Optional.of(cartao);
                    }
                }
            }
        }
        return Optional.empty();
    }
}
