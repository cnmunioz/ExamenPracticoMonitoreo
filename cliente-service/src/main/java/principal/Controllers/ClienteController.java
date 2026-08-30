package principal.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import principal.Models.Cliente;
import principal.Services.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;
    @PostMapping
    public Cliente crear(@RequestBody Cliente cliente){
        return clienteService.guardar(cliente);
    }
    @GetMapping("/{id}")
    public Cliente getPorId(@PathVariable Long id){
        return clienteService.getById(id);
    }
    @GetMapping
    public List<Cliente> getTodos(){
        return clienteService.getTodos();
    }
}
