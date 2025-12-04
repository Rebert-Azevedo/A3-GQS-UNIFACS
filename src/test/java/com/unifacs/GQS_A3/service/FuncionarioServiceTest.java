package com.unifacs.GQS_A3.service;

import com.unifacs.GQS_A3.dto.users.ModificarUsuarioDTO;
import com.unifacs.GQS_A3.dto.users.ResponseUserDTO;
import com.unifacs.GQS_A3.exceptions.RecursoNaoEncontradoException;
import com.unifacs.GQS_A3.model.Funcionario;
import com.unifacs.GQS_A3.model.Usuario;
import com.unifacs.GQS_A3.repository.FuncionarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private FuncionarioService funcionarioService;

    @Test
    public void deveRegistrarUmNovoFuncionario() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Funcionario");
        funcionario.setEmail("funcionario@funcionario.com");
        funcionario.setSenha("funcionarioSenha");
        funcionario.setCargo("Assistente T.I");
        funcionario.setSalario(1900.0);

        Mockito.when(funcionarioRepository.save(funcionario)).thenReturn(funcionario);

        Funcionario novoFuncionario = funcionarioService.registrarFuncionario(funcionario);
        Assertions.assertNotNull(novoFuncionario);
        Assertions.assertEquals(funcionario.getNome(), novoFuncionario.getNome());
        Assertions.assertEquals(funcionario.getEmail(), novoFuncionario.getEmail());
        Assertions.assertEquals(funcionario.getSenha(), novoFuncionario.getSenha());
        Assertions.assertEquals(funcionario.getCargo(), novoFuncionario.getCargo());
        Assertions.assertEquals(funcionario.getSalario(), novoFuncionario.getSalario());
    }

    @Test
    public void deveListarTodosOsFuncionarios() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Funcionario");
        funcionario.setEmail("funcionario@funcionario.com");
        funcionario.setSenha("funcionarioSenha");
        funcionario.setCargo("Assistente T.I");
        funcionario.setSalario(1900.0);

        Funcionario funcionario2 = new Funcionario();
        funcionario.setNome("Funcionario");
        funcionario.setEmail("funcionario2@funcionario.com");
        funcionario.setSenha("funcionarioSenha");
        funcionario.setCargo("Assistente T.I");
        funcionario.setSalario(1900.0);

        List<Funcionario> minhaListaFuncionarios = Arrays.asList(funcionario, funcionario2);

        Mockito.when(funcionarioRepository.findAll()).thenReturn(minhaListaFuncionarios);

        Funcionario novoFuncionario = funcionarioService.registrarFuncionario(funcionario);
        List<Funcionario> listaFuncionarios = funcionarioService.listarFuncionarios();

        Assertions.assertEquals(2, listaFuncionarios.size());
    }

    @Test
    public void deveEncontrarFuncionarioPorId() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("Funcionario");
        funcionario.setEmail("funcionario@funcionario.com");
        funcionario.setSenha("funcionarioSenha");
        funcionario.setCargo("Assistente T.I");
        funcionario.setSalario(1900.0);

        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));

        Funcionario funcionarioBuscado = funcionarioService.buscarPorId(1L);
        Assertions.assertNotNull(funcionarioBuscado);
        Assertions.assertEquals(funcionario.getNome(), funcionarioBuscado.getNome());
        Assertions.assertEquals(funcionario.getEmail(), funcionarioBuscado.getEmail());
    }

    @Test
    public void naoDeveEncontrarFuncionarioPorId() {
        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> funcionarioService.buscarPorId(-1L));
    }

    @Test
    public void deveModificarUmFuncionario() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("Funcionario");
        funcionario.setEmail("funcionario@funcionario.com");
        funcionario.setSenha("funcionarioSenha");
        funcionario.setCargo("Assistente T.I");
        funcionario.setSalario(1900.0);

        Mockito.when(funcionarioRepository.save(funcionario)).thenReturn(funcionario);
        Mockito.when(funcionarioRepository.existsById(1L)).thenReturn(true);
        Mockito.when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));

        Funcionario funcionarioModificado = new Funcionario();
        funcionarioModificado.setNome("funcionarioModificado");
        funcionarioModificado.setEmail("funcionarioModificado@funcionario.com");
        funcionarioModificado.setSenha("funcioarioSenha");

        Funcionario funcionarioAlterado = funcionarioService.editarFuncionario(1L, funcionarioModificado);

        Assertions.assertEquals(funcionarioModificado.getNome(), funcionarioAlterado.getNome());
        Assertions.assertEquals(funcionarioModificado.getEmail(), funcionarioAlterado.getEmail());
    }

    @Test
    public void naoDeveModificarUmFuncionario() {
        Funcionario funcionarioModificado = new Funcionario();
        funcionarioModificado.setNome("funcionarioModificado");
        funcionarioModificado.setEmail("funcionarioModificado@funcionario.com");
        funcionarioModificado.setSenha("funcionarioSenha");

        Assertions.assertNull(funcionarioService.editarFuncionario(1L, funcionarioModificado));
    }

    @Test
    public void naoDeveRemoverUmFuncionarioQueNaoExiste() {
        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> funcionarioService.removerFuncionario(1L));
    }
}
