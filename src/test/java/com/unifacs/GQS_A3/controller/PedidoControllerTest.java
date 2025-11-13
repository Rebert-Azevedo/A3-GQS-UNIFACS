package com.unifacs.GQS_A3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifacs.GQS_A3.Repository.ClienteRepository;
import com.unifacs.GQS_A3.Repository.PedidoRepository;
import com.unifacs.GQS_A3.Repository.ProdutoRepository;
import com.unifacs.GQS_A3.dto.PedidoRequestDTO;
import com.unifacs.GQS_A3.dto.ProdutoPedidoDTO;
import com.unifacs.GQS_A3.model.Cliente;
import com.unifacs.GQS_A3.model.Pedido;
import com.unifacs.GQS_A3.model.Produto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PedidoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ClienteRepository clienteRepository;
    private Cliente clienteSalvo;

    @Autowired
    PedidoRepository pedidoRepository;
    private Pedido pedidoSalvo;

    @Autowired
    ProdutoRepository produtoRepository;
    private Produto produtoSalvo;

    @BeforeEach
    void inicializar(){
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente");
        cliente.setEmail("cliente@email.com");
        cliente.setSenha("senha");

        clienteSalvo = clienteRepository.save(cliente);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setValorTotal(100.00);

        pedidoSalvo = pedidoRepository.save(pedido);
    }

    @AfterEach
    void desligar(){
        pedidoRepository.deleteAll();
        clienteRepository.deleteAll();
        produtoRepository.deleteAll();
    }

    ObjectMapper objectMapper;

    @BeforeEach
    void mapper(){
        objectMapper = new ObjectMapper();
    }

    void criarProduto(){
        Produto produto = new Produto();
        produto.setNome("Produto");
        produto.setValor(10.00);
        produto.setEstoque(5);
        produtoSalvo = produtoRepository.save(produto);
    }

    List<ProdutoPedidoDTO> produtoPedido(Produto produto){
        ProdutoPedidoDTO produtoPedidoDTO = new ProdutoPedidoDTO();
        produtoPedidoDTO.setIdProduto(produto.getId());
        produtoPedidoDTO.setQuantidade(3);

        return List.of(produtoPedidoDTO);
    }

    PedidoRequestDTO pedidoRequest(List<ProdutoPedidoDTO> listaProdutos, Cliente cliente){
        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO();
        pedidoRequestDTO.setIdCliente(cliente.getId());
        pedidoRequestDTO.setProdutos(listaProdutos);
        return pedidoRequestDTO;
    }


    @Test
    public void deveListarTodosOsPedidos() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/pedidos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deveCriarPedido() throws Exception{
        criarProduto();
        List<ProdutoPedidoDTO> listaProdutos = produtoPedido(produtoSalvo);
        PedidoRequestDTO pedido = pedidoRequest(listaProdutos, clienteSalvo);

        String body = objectMapper.writeValueAsString(pedido);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveCriarPedidoComClienteInexistente() throws Exception{
        Cliente clienteInexistente = new Cliente();
        clienteInexistente.setId(100L);
        criarProduto();
        List<ProdutoPedidoDTO> listaProdutos = produtoPedido(produtoSalvo);
        PedidoRequestDTO pedido = pedidoRequest(listaProdutos, clienteInexistente);

        String body = objectMapper.writeValueAsString(pedido);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveCriarPedidoComProdutoInexistente() throws Exception{
        Produto produto = new Produto();
        produto.setId(100L);

        List<ProdutoPedidoDTO> listaProdutos = produtoPedido(produto);
        PedidoRequestDTO pedido = pedidoRequest(listaProdutos, clienteSalvo);

        String body = objectMapper.writeValueAsString(pedido);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deveEncontrarPedido() throws Exception{
        Long idPedidoAtual = pedidoSalvo.getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/pedidos/"+idPedidoAtual))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveEncontrarPedidoInexistente() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/pedidos/25"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}
