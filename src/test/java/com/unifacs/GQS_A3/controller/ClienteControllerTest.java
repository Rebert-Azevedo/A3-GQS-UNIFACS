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

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ClienteRepository clienteRepository;

    @BeforeEach
    void inicializar(){
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente");
        cliente.setEmail("cliente@cliente.com");
        cliente.setSenha("clienteSenha");
        clienteRepository.save(cliente);
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cliente"))
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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveRegistrarClienteSemNome() throws Exception {
        Cliente clienteTest = new Cliente();
        clienteTest.setEmail("clienteTest@cliente.com");
        clienteTest.setSenha("clienteSenha");

        String body = objectMapper.writeValueAsString(clienteTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cliente")
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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cliente")
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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deveEncontrarClientePorId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cliente/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveEncontrarClientePorId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cliente/2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deveModificarUmCliente() throws Exception{
        Cliente cliente = new Cliente();
        cliente.setId(2L);
        cliente.setNome("Cliente");
        cliente.setEmail("cliente@cliente.com");
        cliente.setSenha("clienteSenha");
        cliente.setDataCriacao(LocalDateTime.now());
        clienteRepository.save(cliente);

        Cliente clienteTest = new Cliente();
        clienteTest.setNome("Cliente Modificado");

        String body = objectMapper.writeValueAsString(clienteTest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/cliente/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Cliente Modificado"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveModificarUmClienteInexistente() throws Exception{
        Cliente clienteTest = new Cliente();
        clienteTest.setEmail("clientemodificado@email.com");

        String body = objectMapper.writeValueAsString(clienteTest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/cliente/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}
