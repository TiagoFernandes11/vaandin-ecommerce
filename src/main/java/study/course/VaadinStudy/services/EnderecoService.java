package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Endereco;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.repository.EnderecoRepository;
import study.course.VaadinStudy.services.base.BaseService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EnderecoService extends BaseService<Endereco> {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        super(enderecoRepository);
    }
    
    public Endereco find(String logradouro, Long numero){
        Optional<Endereco> endereco = enderecoRepository.findByLogradouroAndNumero(logradouro, numero);
        return endereco.orElse(null);
    }

    public Endereco findAtivoByEmail(String email){
        Usuario usuario = usuarioService.find(email);

        List<Endereco> enderecos = enderecoRepository.findAllByUsuario(usuario);

        if(enderecos.isEmpty()){
            return null;
        }

        return enderecos.stream().filter(e -> e.getAtivo() == true && e.getRemovido() == false).findFirst().orElse(null);
    }

    public void salvarEnderecoCliente(Endereco endereco, String email){
        Usuario usuario = usuarioService.find(email);

        List<Endereco> enderecos = enderecoRepository.findAllByUsuario(usuario);

        enderecos.stream().filter(e -> Objects.equals(e, endereco)).findFirst().ifPresent(e -> {
            e.setAtivo(true);
            e.setRemovido(false);
            save(e);
        });

        Endereco enderecoSalvo = findAtivoByEmail(email);

        if(Objects.nonNull(findAtivoByEmail(email)) && !Objects.equals(endereco, enderecoSalvo)){
            Endereco e = findAtivoByEmail(email);
            e.setAtivo(false);
            e.setRemovido(true);
            save(e);
            save(endereco);
        }

        save(endereco);
    }
}
