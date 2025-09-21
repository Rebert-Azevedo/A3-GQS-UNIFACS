package com.unifacs.GQS_A3.Service;

import com.unifacs.GQS_A3.Repository.FuncionarioRepository;
import com.unifacs.GQS_A3.model.Cliente;
import com.unifacs.GQS_A3.model.Funcionario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Funcionario> listarFuncionarios(){
        return funcionarioRepository.findAll();
    }
    public Funcionario registrarFuncionario(Funcionario funcionario){
        return funcionarioRepository.save(funcionario);
    }
    public void removerFuncionario(Long id){
        funcionarioRepository.deleteById(id);
    }
    public Optional<Funcionario> buscarPorId(Long id){
        return funcionarioRepository.findById(id);
    }

}
