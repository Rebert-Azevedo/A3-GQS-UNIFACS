package com.unifacs.GQS_A3.controller;

import com.unifacs.GQS_A3.service.FuncionarioService;
import com.unifacs.GQS_A3.model.Funcionario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {
    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;

    }

    @PostMapping
    public ResponseEntity<Funcionario> registrarFuncionario(@RequestBody Funcionario funcionario){
        Funcionario funcionarioRegistrado = funcionarioService.registrarFuncionario(funcionario);
        return new ResponseEntity<>(funcionarioRegistrado, HttpStatus.CREATED);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Funcionario> buscarFuncionario(@PathVariable Long id){
        Funcionario funcionario = funcionarioService.buscarPorId(id);

        return new ResponseEntity<>(funcionario, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Funcionario>> listarFuncionarios(){
        return new ResponseEntity<>(funcionarioService.listarFuncionarios(), HttpStatus.OK);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Funcionario> editarFuncionario(@PathVariable Long id, @RequestBody Funcionario funcionario){
        Funcionario updatedFuncionario = funcionarioService.editarFuncionario(id, funcionario);

        return new ResponseEntity<>(updatedFuncionario, HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> removerFuncionario(@PathVariable Long id){
        funcionarioService.removerFuncionario(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
