package com.unifacs.GQS_A3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifacs.GQS_A3.Repository.ClienteRepository;
import com.unifacs.GQS_A3.model.Cliente;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ClienteRepository clienteRepository;

    private Cliente clienteAtual;

    @BeforeEach
    void inicializar(){
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente");
        cliente.setEmail("cliente@cliente.com");
        cliente.setSenha("clienteSenha");
        clienteAtual = clienteRepository.save(cliente);
    }

    @AfterEach
    void desligar(){
        clienteRepository.deleteAll();
    }

    ObjectMapper objectMapper;

    @BeforeEach
    void mapper(){
        objectMapper = new ObjectMapper();
    }

    @Test
    public void deveListarClientes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deveRegistrarCliente() throws Exception {
        Cliente clienteTest = new Cliente();
        clienteTest.setNome("Cliente");
        clienteTest.setEmail("clienteTest@cliente.com");
        clienteTest.setSenha("clienteSenha");

        String body = objectMapper.writeValueAsString(clienteTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveRegistrarClienteSemNome() throws Exception {
        Cliente clienteTest = new Cliente();
        clienteTest.setEmail("clienteTest@cliente.com");
        clienteTest.setSenha("clienteSenha");

        String body = objectMapper.writeValueAsString(clienteTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveRegistrarClienteSemEmail() throws Exception {
        Cliente clienteTest = new Cliente();
        clienteTest.setNome("Cliente");
        clienteTest.setSenha("clienteSenha");

        String body = objectMapper.writeValueAsString(clienteTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveRegistrarClienteSemSenha() throws Exception {
        Cliente clienteTest = new Cliente();
        clienteTest.setNome("Cliente");
        clienteTest.setEmail("clienteTest@cliente.com");

        String body = objectMapper.writeValueAsString(clienteTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveRegistrarClienteComEmailJaCadastrado() throws Exception {
        Cliente clienteTest = new Cliente();
        clienteTest.setNome("Cliente");
        clienteTest.setEmail("cliente@cliente.com");
        clienteTest.setSenha("senhaTeste");
        String body = objectMapper.writeValueAsString(clienteTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isBadGateway())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deveEncontrarClientePorId() throws Exception{
        Long idAtual = clienteAtual.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes/" +idAtual))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idAtual))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveEncontrarClientePorId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes/100000"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deveModificarUmCliente() throws Exception{
        Long idParaModificar = clienteAtual.getId();

        Cliente clienteTest = new Cliente();
        clienteTest.setNome("Cliente Modificado");
        clienteTest.setEmail("clientemodificado@email.com");
        clienteTest.setSenha("senhaModificada@");
        String body = objectMapper.writeValueAsString(clienteTest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/clientes/" +idParaModificar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(clienteTest.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(clienteTest.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.senha").value(clienteTest.getSenha()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveModificarUmClienteInexistente() throws Exception{
        Cliente clienteTest = new Cliente();
        clienteTest.setEmail("clientemodificado@email.com");

        String body = objectMapper.writeValueAsString(clienteTest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/clientes/100000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}
