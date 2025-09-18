package com.unifacs.GQS_A3.Service;

import com.unifacs.GQS_A3.Repository.ClienteRepository;
import com.unifacs.GQS_A3.model.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id){
        return clienteRepository.findById(id);
    }

    public Cliente registrarCliente(Cliente cliente){
        return clienteRepository.save(cliente);
    }
    public void removerCliente(Long id){
        clienteRepository.deleteById(id);
    }
}
