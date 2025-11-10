package com.unifacs.GQS_A3.Service;

import com.unifacs.GQS_A3.Repository.ClienteRepository;
import com.unifacs.GQS_A3.exceptions.CampoNaoPreenchidoException;
import com.unifacs.GQS_A3.exceptions.RecursoNaoEncontradoException;
import com.unifacs.GQS_A3.model.Cliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    public void deveRegistrarUmNovoCliente(){
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente");
        cliente.setEmail("cliente@cliente.com");
        cliente.setSenha("clienteSenha");

        Mockito.when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente novoCliente = clienteService.registrarCliente(cliente);
        Assertions.assertNotNull(novoCliente);
        Assertions.assertEquals(cliente.getNome(), novoCliente.getNome());
        Assertions.assertEquals(cliente.getEmail(), novoCliente.getEmail());
        Assertions.assertEquals(cliente.getSenha(), novoCliente.getSenha());
    }

    @Test
    public void naoDeveRegistrarUmNovoCliente(){
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente");
        cliente.setSenha("clienteSenha");

        Assertions.assertThrows(CampoNaoPreenchidoException.class, () -> clienteService.registrarCliente(cliente));
    }

    @Test
    public void deveListarTodosOsClientes(){
        Cliente cliente1 = new Cliente();
        cliente1.setNome("Cliente1");
        cliente1.setEmail("cliente1@cliente.com");
        cliente1.setSenha("cliente1Senha");

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Cliente2");
        cliente2.setEmail("cliente2@cliente.com");
        cliente2.setSenha("cliente2Senha");

        List<Cliente> minhaListaClientes = Arrays.asList(cliente1, cliente2);

        Mockito.when(clienteRepository.findAll()).thenReturn(minhaListaClientes);

        List<Cliente> listaCliente = clienteService.listarClientes();

        Assertions.assertEquals(2, listaCliente.size());
    }


    @Test
    public void deveEncontrarClientePorId(){
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente");
        cliente.setEmail("cliente@cliente.com");
        cliente.setSenha("clienteSenha");

        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente clienteBuscado = clienteService.buscarPorId(1L);
        Assertions.assertNotNull(clienteBuscado);
        Assertions.assertEquals(cliente.getId(), clienteBuscado.getId());
        Assertions.assertEquals(cliente.getNome(), clienteBuscado.getNome());
    }

    @Test
    public void naoDeveEncontrarClientePorId(){
        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> clienteService.buscarPorId(1L));
    }

    @Test
    public void deveModificarUmCliente(){
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente");
        cliente.setEmail("cliente@cliente.com");
        cliente.setSenha("clienteSenha");

        Mockito.when(clienteRepository.save(cliente)).thenReturn(cliente);
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente clienteModificado = new Cliente();
        clienteModificado.setId(1L);
        clienteModificado.setNome("clienteModificado");
        clienteModificado.setEmail("clienteModificado@cliente.com");
        clienteModificado.setSenha("clienteSenha");

        Cliente clienteAlterado = clienteService.editarCliente(1L, clienteModificado);

        Assertions.assertEquals(clienteModificado.getNome(), clienteAlterado.getNome());
        Assertions.assertEquals(clienteModificado.getEmail(), clienteAlterado.getEmail());
    }

    @Test
    public void naoDeveModificarUmCliente(){
        Cliente clienteModificado = new Cliente();
        clienteModificado.setId(1L);
        clienteModificado.setNome("clienteModificado");
        clienteModificado.setEmail("clienteModificado@cliente.com");
        clienteModificado.setSenha("clienteSenha");

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> clienteService.editarCliente(1L, clienteModificado));
    }
}
