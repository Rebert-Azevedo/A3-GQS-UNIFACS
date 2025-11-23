package com.unifacs.GQS_A3.Service;

import com.unifacs.GQS_A3.Repository.ClienteRepository;
import com.unifacs.GQS_A3.exceptions.CampoNaoPreenchidoException;
import com.unifacs.GQS_A3.exceptions.EmailJaCadastrado;
import com.unifacs.GQS_A3.exceptions.RecursoNaoEncontradoException;
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

    public Cliente buscarPorId(Long id){
        return clienteRepository.findById(id).
                orElseThrow(() -> new RecursoNaoEncontradoException("Cliente com id "+id+" não encontrado"));
    }

    public Cliente registrarCliente(Cliente cliente){
        if(cliente.getNome() == null || cliente.getEmail() == null || cliente.getSenha() == null){
            throw new CampoNaoPreenchidoException("nome, email e senha são obrigatórios");
        }

        boolean emailExistente = verificarEmailExistente(cliente.getEmail());
        if(emailExistente){
            throw new EmailJaCadastrado("Email informado já está cadastrado");
        }
        return clienteRepository.save(cliente);
    }
    public void removerCliente(Long id){
        if(!clienteRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("Cliente com id "+id+" não encontrado");
        }
        clienteRepository.deleteById(id);
    }

    public Cliente editarCliente(Long id, Cliente alterCliente){
        Cliente cliente = clienteRepository
                .findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente com id "+id+" não encontrado"));

        cliente.setNome(alterCliente.getNome() != null ? alterCliente.getNome(): cliente.getNome());
        cliente.setSenha(alterCliente.getSenha() != null ? alterCliente.getSenha(): cliente.getSenha());
        cliente.setEmail(alterCliente.getEmail() != null ? alterCliente.getEmail(): cliente.getEmail());
        cliente.setDataNascimento(alterCliente.getDataNascimento() != null ? alterCliente.getDataNascimento(): cliente.getDataNascimento());
        return clienteRepository.save(cliente);
    }
    public Boolean verificarEmailExistente(String email){
        Cliente clienteEmail = clienteRepository.findByEmailAddress(email);
        return clienteEmail != null;
    }
}
