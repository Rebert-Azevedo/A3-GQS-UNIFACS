package com.unifacs.GQS_A3.Service;

import com.unifacs.GQS_A3.Repository.FuncionarioRepository;
import com.unifacs.GQS_A3.exceptions.RecursoNaoEncontradoException;
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
        if(!funcionarioRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("Funcionario de id: " + id + " não encontrado");
        }
        funcionarioRepository.deleteById(id);
    }

    public Funcionario buscarPorId(Long id){
        return funcionarioRepository
                .findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionario de id: " + id + " não encontrado"));
    }

    public Funcionario editarFuncionario(Long id, Funcionario alterFuncionario){
        if(funcionarioRepository.existsById(id)){
            Funcionario funcionario = funcionarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));
            funcionario.setNome(alterFuncionario.getNome() != null ? alterFuncionario.getNome(): funcionario.getNome());
            funcionario.setEmail(alterFuncionario.getEmail() != null ? alterFuncionario.getEmail(): funcionario.getEmail());
            funcionario.setSenha(alterFuncionario.getSenha() != null ? alterFuncionario.getSenha(): funcionario.getSenha());
            funcionario.setDataNascimento(alterFuncionario.getDataNascimento() != null ? alterFuncionario.getDataNascimento(): funcionario.getDataNascimento());
            funcionario.setSalario(alterFuncionario.getSalario() != 0 ? alterFuncionario.getSalario(): funcionario.getSalario());
            funcionario.setCargo(alterFuncionario.getCargo() != null ? alterFuncionario.getCargo(): funcionario.getCargo());

            return funcionarioRepository.save(funcionario);
        }else{
            System.out.println("Funcionario não encontrado");
        }
        return null;
    }

}
