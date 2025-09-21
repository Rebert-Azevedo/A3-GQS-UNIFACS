package com.unifacs.GQS_A3.Controller;

import com.unifacs.GQS_A3.Service.FuncionarioService;
import com.unifacs.GQS_A3.model.Funcionario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionario")
public class FuncionarioController {
    private final FuncionarioService funcionarioService;


    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;

    }

    @PostMapping
    public Funcionario registrarFuncionario(@RequestBody Funcionario funcionario){
        return funcionarioService.registrarFuncionario(funcionario);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> removerFuncionario(@PathVariable Long id){
        funcionarioService.removerFuncionario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Funcionario> listarFuncionarios(){
        return funcionarioService.listarFuncionarios();
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Funcionario> buscarFuncionario(@PathVariable Long id){
        return funcionarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
