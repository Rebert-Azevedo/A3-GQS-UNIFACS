package com.unifacs.GQS_A3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifacs.GQS_A3.repository.ProdutoRepository;
import com.unifacs.GQS_A3.model.Produto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProdutoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProdutoRepository produtoRepository;

    private Produto produtoSalvo;

    @BeforeEach
    void inicializar(){
        Produto produto = new Produto();
        produto.setNome("Produto");
        produto.setDescricao("Produto para teste de integração");
        produto.setValor(12.50);
        produto.setEstoque(5);
        produtoSalvo = produtoRepository.save(produto);
    }

    @AfterEach
    void desligar(){
        produtoRepository.deleteAll();
    }

    ObjectMapper objectMapper;

    @BeforeEach
    void mapper(){
        objectMapper = new ObjectMapper();
    }

    @Test
    public void deveListarTodosOsProdutos() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos/public"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void deveAdicionarNovoProduto() throws Exception{
        Produto produtoTeste = new Produto();
        produtoTeste.setNome("ProdutoTeste");
        produtoTeste.setDescricao("Produto para teste de integração");
        produtoTeste.setValor(10.50);
        produtoTeste.setEstoque(10);

        String body = objectMapper.writeValueAsString(produtoTeste);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/produtos/admin/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("ProdutoTeste"))
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    @WithMockUser(username = "user@user.com", roles = "USER")
    public void usuariosComunsNaoDevemAdicionarNovoProduto() throws Exception{
        Produto produtoTeste = new Produto();
        produtoTeste.setNome("ProdutoTeste");
        produtoTeste.setDescricao("Produto para teste de integração");
        produtoTeste.setValor(10.50);
        produtoTeste.setEstoque(10);

        String body = objectMapper.writeValueAsString(produtoTeste);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/produtos/admin/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void naoDeveAdicionarNovoProdutoSemNome() throws Exception{
        Produto produtoTeste = new Produto();
        produtoTeste.setDescricao("Produto para teste de integração");
        produtoTeste.setValor(10.50);
        produtoTeste.setEstoque(10);

        String body = objectMapper.writeValueAsString(produtoTeste);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/produtos/admin/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void naoDeveAdicionarNovoProdutoComValorZerado() throws Exception{
        Produto produtoTeste = new Produto();
        produtoTeste.setNome("ProdutoTeste");
        produtoTeste.setDescricao("Produto para teste de integração");
        produtoTeste.setValor(0);
        produtoTeste.setEstoque(10);

        String body = objectMapper.writeValueAsString(produtoTeste);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/produtos/admin/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void naoDeveAdicionarNovoProdutoComEstoqueZerado() throws Exception{
        Produto produtoTeste = new Produto();
        produtoTeste.setNome("ProdutoTeste");
        produtoTeste.setDescricao("Produto para teste de integração");
        produtoTeste.setValor(10.50);
        produtoTeste.setEstoque(0);

        String body = objectMapper.writeValueAsString(produtoTeste);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/produtos/admin/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void deveEncontrarProdutoPorId() throws Exception{
        Long idAtual = produtoSalvo.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos/public/" +idAtual))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idAtual))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveEncontrarProdutoInexistente() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos/public/10000"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void deveModificarUmProduto() throws Exception{
        Long idParaModificar = produtoSalvo.getId();

        Produto produtoModificado = new Produto();
        produtoModificado.setNome("Produto modificado");
        produtoModificado.setValor(20.00);
        produtoModificado.setEstoque(20);

        String body = objectMapper.writeValueAsString(produtoModificado);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/produtos/admin/editar/" +idParaModificar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(produtoModificado.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.valor").value(produtoModificado.getValor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.estoque").value(produtoModificado.getEstoque()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = "USER")
    public void usuariosComunsNaodevemModificarUmProduto() throws Exception{
        Long idParaModificar = produtoSalvo.getId();

        Produto produtoModificado = new Produto();
        produtoModificado.setNome("Produto modificado");
        produtoModificado.setValor(20.00);
        produtoModificado.setEstoque(20);

        String body = objectMapper.writeValueAsString(produtoModificado);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/produtos/admin/editar/" +idParaModificar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void naoDeveModificarUmProdutoComIdInexistente() throws Exception{

        Produto produtoModificado = new Produto();
        produtoModificado.setNome("Produto modificado");

        String body = objectMapper.writeValueAsString(produtoModificado);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/produtos/admin/editar/10000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}
