package com.unifacs.GQS_A3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifacs.GQS_A3.repository.UsuarioRepository;
import com.unifacs.GQS_A3.repository.PedidoRepository;
import com.unifacs.GQS_A3.repository.ProdutoRepository;
import com.unifacs.GQS_A3.dto.PedidoRequestDTO;
import com.unifacs.GQS_A3.dto.ProdutoPedidoDTO;
import com.unifacs.GQS_A3.model.Usuario;
import com.unifacs.GQS_A3.model.Pedido;
import com.unifacs.GQS_A3.model.Produto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
    UsuarioRepository usuarioRepository;
    private Usuario usuarioSalvo;

    @Autowired
    PedidoRepository pedidoRepository;
    private Pedido pedidoSalvo;

    @Autowired
    ProdutoRepository produtoRepository;
    private Produto produtoSalvo;

    ObjectMapper objectMapper;

    @BeforeEach
    void inicializar(){
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario");
        usuario.setEmail("usuario@email.com");
        usuario.setSenha("senha");

        usuarioSalvo = usuarioRepository.save(usuario);

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setValorTotal(100.00);

        pedidoSalvo = pedidoRepository.save(pedido);
    }

    @AfterEach
    void desligar(){
        pedidoRepository.deleteAll();
        usuarioRepository.deleteAll();
        produtoRepository.deleteAll();
    }

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

    PedidoRequestDTO pedidoRequest(List<ProdutoPedidoDTO> listaProdutos, Usuario usuario){
        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO();
        pedidoRequestDTO.setIdUsuario(usuario.getId());
        pedidoRequestDTO.setProdutos(listaProdutos);
        return pedidoRequestDTO;
    }


    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void deveListarTodosOsPedidos() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/pedidos/user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = "USER")
    public void deveCriarPedido() throws Exception{
        criarProduto();
        List<ProdutoPedidoDTO> listaProdutos = produtoPedido(produtoSalvo);
        PedidoRequestDTO pedido = pedidoRequest(listaProdutos, usuarioSalvo);

        String body = objectMapper.writeValueAsString(pedido);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pedidos/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void naoDeveCriarPedidoComUsuarioInexistente() throws Exception{
        Usuario usuarioInexistente = new Usuario();
        usuarioInexistente.setId(100L);
        criarProduto();
        List<ProdutoPedidoDTO> listaProdutos = produtoPedido(produtoSalvo);
        PedidoRequestDTO pedido = pedidoRequest(listaProdutos, usuarioInexistente);

        String body = objectMapper.writeValueAsString(pedido);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pedidos/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = "USER")
    public void naoDeveCriarPedidoComProdutoInexistente() throws Exception{
        Produto produto = new Produto();
        produto.setId(100L);

        List<ProdutoPedidoDTO> listaProdutos = produtoPedido(produto);
        PedidoRequestDTO pedido = pedidoRequest(listaProdutos, usuarioSalvo);

        String body = objectMapper.writeValueAsString(pedido);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pedidos/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = "USER")
    public void naoDeveCriarPedidoComEstoqueInsuficiente() throws Exception{
        Produto produto = new Produto();
        produto.setNome("Produto com estoque baixo");
        produto.setValor(10.00);
        produto.setEstoque(1);
        Produto produtoSalvoLocal = produtoRepository.save(produto);

        ProdutoPedidoDTO produtoPedidoDTO = new ProdutoPedidoDTO();
        produtoPedidoDTO.setIdProduto(produtoSalvoLocal.getId());
        produtoPedidoDTO.setQuantidade(5); // Requesting more than available

        List<ProdutoPedidoDTO> listaProdutos = List.of(produtoPedidoDTO);
        PedidoRequestDTO pedido = pedidoRequest(listaProdutos, usuarioSalvo);

        String body = objectMapper.writeValueAsString(pedido);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pedidos/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Estoque insuficiente"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void deveEncontrarPedido() throws Exception{
        Long idPedidoAtual = pedidoSalvo.getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/pedidos/user/"+idPedidoAtual))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void naoDeveEncontrarPedidoInexistente() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/pedidos/user/25"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}
