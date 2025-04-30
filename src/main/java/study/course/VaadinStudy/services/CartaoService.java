package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Cartao;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.repository.CartaoRepository;
import study.course.VaadinStudy.services.base.BaseService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartaoService extends BaseService<Cartao> {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public CartaoService(CartaoRepository cartaoRepository) {
        super(cartaoRepository);
    }

    private void salvarCartao(Cartao cartao) {
        if (cartaoRepository.findByNumero(cartao.getNumero()).isEmpty()) {
            save(cartao);
        }
    }

    public void salvarCartaoCliente(Cartao cartao, String email) {
        List<Cartao> cartoes = this.findAllByEmail(email);

        cartoes.stream().filter(c -> c.equals(cartao)).findFirst().ifPresent(c -> {
            c.setAtivo(true);
            c.setRemovido(false);
            this.salvarCartao(c);
        });

        Cartao cartaoSalvo = this.findAtivoByEmail(email).orElse(null);

        if (Objects.nonNull(cartaoSalvo) && !cartaoSalvo.equals(cartao)) {
            cartaoSalvo.setAtivo(false);
            cartaoSalvo.setRemovido(true);
            this.update(cartaoSalvo);
        }
        this.salvarCartao(cartao);
    }

    public void update(Cartao cartao) {
        Cartao cartaoSalvo = cartaoRepository.findByNumero(cartao.getNumero()).orElse(null);
        if (cartaoSalvo != null) {
            cartaoSalvo.setAtivo(cartao.getAtivo());
            cartaoSalvo.setRemovido(cartao.getRemovido());
            cartaoSalvo.setNumero(cartao.getNumero());
            cartaoSalvo.setCvv(cartao.getCvv());
            cartaoSalvo.setValidade(cartao.getValidade());
            cartaoSalvo.setNomeImpresso(cartao.getNomeImpresso());

            save(cartaoSalvo);
        }
    }

    public List<Cartao> findAllByEmail(String emailUsuario) {
        Usuario usuario = usuarioService.find(emailUsuario);
        return cartaoRepository.findAllByUsuario(usuario);
    }

    public Optional<Cartao> findAtivoByEmail(String emailUsuario) {
        Usuario usuario = usuarioService.find(emailUsuario);
        if (Objects.nonNull(usuario)) {
            List<Cartao> cartoes = cartaoRepository.findAllByUsuario(usuario);

            return cartoes.stream().filter(c -> c.getAtivo() == true && c.getRemovido() == false).findFirst();
        }
        return Optional.empty();
    }
}
