package principal.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import principal.Models.Cliente;
import principal.Repositories.ClienteRepository;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    public Cliente getById(Long id) {
        //return clienteRepository.findById(id).get();
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }
    public List<Cliente> getTodos() {
        return clienteRepository.findAll();
    }
}
