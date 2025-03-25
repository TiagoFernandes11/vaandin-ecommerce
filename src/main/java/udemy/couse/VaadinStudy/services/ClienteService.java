package udemy.couse.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import udemy.couse.VaadinStudy.entities.Cliente;
import udemy.couse.VaadinStudy.repository.ClienteRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Cliente find(String email){
        Optional<Cliente> optionalUsuario = clienteRepository.findByEmail(email);
        return optionalUsuario.orElse(null);
    }

    public Cliente find(long id){
        Optional<Cliente> optionalUsuario = clienteRepository.findById(id);
        return optionalUsuario.orElse(null);
    }

    public List<Cliente> findAll(){
        return clienteRepository.findAll();
    }

    public boolean create(Cliente cliente){
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        if(clienteRepository.findByEmail(cliente.getEmail()).isPresent()){
            return false;
        }
        clienteRepository.save(cliente);
        return true;
    }

    public boolean update(Cliente cliente){
        Optional<Cliente> optionalUsuarioDB = clienteRepository.findByEmail(cliente.getEmail().trim());
        if(optionalUsuarioDB.isPresent()){
            if(Objects.equals(optionalUsuarioDB.get().getId(), cliente.getId())){;
                clienteRepository.save(cliente);
                return true;
            } else {
                return false;
            }
        } else {
            clienteRepository.save(cliente);
            return true;
        }
    }

    public void delete(Cliente cliente){
        clienteRepository.delete(cliente);
    }
}
