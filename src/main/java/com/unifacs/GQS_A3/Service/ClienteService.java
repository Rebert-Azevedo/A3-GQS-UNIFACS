package com.unifacs.GQS_A3.Service;

import com.unifacs.GQS_A3.Repository.ClienteRepository;
import com.unifacs.GQS_A3.exceptions.CampoNaoPreenchidoException;
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
        if(cliente.getNome() == null || cliente.getEmail() == null || cliente.getSenha() == null){
            throw new CampoNaoPreenchidoException("nome, email e senha são obrigatórios");
        }
        return clienteRepository.save(cliente);
    }
    public void removerCliente(Long id){
        clienteRepository.deleteById(id);
    }

    public Cliente editarCliente(Long id, Cliente alterCliente){
        if(clienteRepository.existsById(id)){
            Cliente cliente = clienteRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            cliente.setNome(alterCliente.getNome() != null ? alterCliente.getNome(): cliente.getNome());
            cliente.setSenha(alterCliente.getSenha() != null ? alterCliente.getSenha(): cliente.getSenha());
            cliente.setEmail(alterCliente.getEmail() != null ? alterCliente.getEmail(): cliente.getEmail());
            cliente.setDataNascimento(alterCliente.getDataNascimento() != null ? alterCliente.getDataNascimento(): cliente.getDataNascimento());
            return clienteRepository.save(cliente);
        }else{
            System.out.println("Funcionario não encontrado");
        }
        return null;
    }
}
