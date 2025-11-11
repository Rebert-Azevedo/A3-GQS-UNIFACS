package com.unifacs.GQS_A3.Service;


import com.unifacs.GQS_A3.Repository.ClienteRepository;
import com.unifacs.GQS_A3.Repository.PedidoRepository;
import com.unifacs.GQS_A3.Repository.ProdutoRepository;
import com.unifacs.GQS_A3.dto.PedidoRequestDTO;
import com.unifacs.GQS_A3.dto.PedidoResponseDTO;
import com.unifacs.GQS_A3.dto.ProdutoPedidoDTO;
import com.unifacs.GQS_A3.exceptions.RecursoNaoEncontradoException;
import com.unifacs.GQS_A3.model.Cliente;
import com.unifacs.GQS_A3.model.Pedido;
import com.unifacs.GQS_A3.model.Produto;
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
public class PedidoServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    public void deveCriarPedido(){
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente de teste");
        cliente.setEmail("cliente@teste.com");
        cliente.setSenha("senha");

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto de teste");
        produto.setEstoque(5);
        produto.setValor(3.0);

        ProdutoPedidoDTO produtoPedido = new ProdutoPedidoDTO();
        produtoPedido.setIdProduto(produto.getId());
        produtoPedido.setQuantidade(3);
        List<ProdutoPedidoDTO> listaProdutosDePedido = List.of(produtoPedido);

        PedidoRequestDTO pedidoRequest = new PedidoRequestDTO();
        pedidoRequest.setIdCliente(cliente.getId());
        pedidoRequest.setProdutos(listaProdutosDePedido);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCliente(cliente);

        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produto);
        Mockito.when(pedidoRepository.save(Mockito.any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedidoSalvo = invocation.getArgument(0);
            pedidoSalvo.setId(1L);
            return pedidoSalvo;
        });

        PedidoResponseDTO pedidoResponse = pedidoService.criarPedido(pedidoRequest);

        Assertions.assertNotNull(pedidoResponse);
        Assertions.assertEquals(1L, pedidoResponse.getId());
        Assertions.assertEquals("Cliente de teste", pedidoResponse.getNomeCliente());
        Assertions.assertEquals(9.0, pedidoResponse.getVlrTotal());
        Assertions.assertEquals(1, pedidoResponse.getProdutos().size());
    }

    @Test
    public void naoDeveCriarPedidoSemCliente(){
        PedidoRequestDTO pedidoRequest = new PedidoRequestDTO();
        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> pedidoService.criarPedido(pedidoRequest));
    }

    @Test
    public void naoDeveCriarPedidoSemProduto(){
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente de teste");
        cliente.setEmail("cliente@teste.com");
        cliente.setSenha("senha");

        PedidoRequestDTO pedidoRequest = new PedidoRequestDTO();
        pedidoRequest.setIdCliente(cliente.getId());
        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> pedidoService.criarPedido(pedidoRequest));
    }

    @Test
    public void deveListarPedidos(){
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente de teste");
        cliente.setEmail("cliente@teste.com");
        cliente.setSenha("senha");

        Pedido pedido1 = new Pedido();
        pedido1.setCliente(cliente);
        pedido1.setValorTotal(10.0);

        Pedido pedido2 = new Pedido();
        pedido2.setCliente(cliente);
        pedido2.setValorTotal(15.0);

        List<Pedido> listaPedidos = Arrays.asList(pedido1, pedido2);

        Mockito.when(pedidoRepository.findAll()).thenReturn(listaPedidos);

        List<Pedido> listaRetornada = pedidoService.listarPedidos();
        Assertions.assertEquals(2, listaRetornada.size());
        Assertions.assertEquals(1L, listaRetornada.get(0).getCliente().getId());
        Assertions.assertEquals(10.0, listaRetornada.get(0).getValorTotal());
        Assertions.assertEquals(15.0, listaRetornada.get(1).getValorTotal());
    }

    @Test
    public void deveEncontrarPedidoPorId(){
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente de teste");
        cliente.setEmail("cliente@teste.com");
        cliente.setSenha("senha");

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCliente(cliente);
        pedido.setValorTotal(10.0);

        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        Pedido pedidoEncontrado = pedidoService.buscarPedido(1L);

        Assertions.assertEquals(1L, pedidoEncontrado.getId());
    }

    @Test
    public void naoDeveEncontrarPedidoInexistente(){
        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> pedidoService.buscarPedido(1L));
    }

    @Test
    public void deveCalcularTotalDoPedido(){
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente de teste");
        cliente.setEmail("cliente@teste.com");
        cliente.setSenha("senha");

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto de teste");
        produto.setEstoque(5);
        produto.setValor(3.0);

        ProdutoPedidoDTO produtoPedido = new ProdutoPedidoDTO();
        produtoPedido.setIdProduto(produto.getId());
        produtoPedido.setQuantidade(3);
        List<ProdutoPedidoDTO> listaProdutosDePedido = List.of(produtoPedido);

        PedidoRequestDTO pedidoRequest = new PedidoRequestDTO();
        pedidoRequest.setIdCliente(cliente.getId());
        pedidoRequest.setProdutos(listaProdutosDePedido);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCliente(cliente);

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        double total = pedidoService.calcTotalPedido(pedidoRequest, pedido);
        Assertions.assertEquals(9, total);
    }

    @Test
    public void naoDeveCalcularTotalPedido(){
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente de teste");
        cliente.setEmail("cliente@teste.com");
        cliente.setSenha("senha");

        ProdutoPedidoDTO produtoPedido = new ProdutoPedidoDTO();
        produtoPedido.setQuantidade(3);
        List<ProdutoPedidoDTO> listaProdutosDePedido = List.of(produtoPedido);

        PedidoRequestDTO pedidoRequest = new PedidoRequestDTO();
        pedidoRequest.setIdCliente(cliente.getId());
        pedidoRequest.setProdutos(listaProdutosDePedido);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCliente(cliente);

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> pedidoService.calcTotalPedido(pedidoRequest, pedido));
    }
    @Test
    public void naoDeveCalcularProdutoSemEstoque(){
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setEstoque(1);
        produto.setValor(3.0);

        ProdutoPedidoDTO produtoPedido = new ProdutoPedidoDTO();
        produtoPedido.setIdProduto(produto.getId());
        produtoPedido.setQuantidade(3);
        List<ProdutoPedidoDTO> listaProdutosDePedido = List.of(produtoPedido);

        PedidoRequestDTO pedidoRequest = new PedidoRequestDTO();
        pedidoRequest.setIdCliente(cliente.getId());
        pedidoRequest.setProdutos(listaProdutosDePedido);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCliente(cliente);

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Assertions.assertThrows(RuntimeException.class, () -> pedidoService.calcTotalPedido(pedidoRequest, pedido));
    }
}
