package com.unifacs.GQS_A3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifacs.GQS_A3.model.Usuario;
import com.unifacs.GQS_A3.repository.UsuarioRepository;
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
public class UsuarioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UsuarioRepository usuarioRepository;

    private Usuario usuarioAtual;

    @BeforeEach
    void inicializar(){
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario");
        usuario.setEmail("usuario@usuario.com");
        usuario.setSenha("usuarioSenha");
        usuarioAtual = usuarioRepository.save(usuario);
    }

    @AfterEach
    void desligar(){
        usuarioRepository.deleteAll();
    }

    ObjectMapper objectMapper;

    @BeforeEach
    void mapper(){
        objectMapper = new ObjectMapper();
    }

    @Test
    public void deveListarClientes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/usuarios"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deveRegistrarUsuario() throws Exception {
        Usuario usuarioTest = new Usuario();
        usuarioTest.setNome("Usuario");
        usuarioTest.setEmail("usuarioTest@usuario.com");
        usuarioTest.setSenha("usuarioSenha");

        String body = objectMapper.writeValueAsString(usuarioTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveRegistrarUsuarioSemNome() throws Exception {
        Usuario usuarioTest = new Usuario();
        usuarioTest.setEmail("usuarioTest@usuario.com");
        usuarioTest.setSenha("usuarioSenha");

        String body = objectMapper.writeValueAsString(usuarioTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveRegistrarUsuarioSemEmail() throws Exception {
        Usuario usuarioTest = new Usuario();
        usuarioTest.setNome("Usuario");
        usuarioTest.setSenha("usuarioSenha");

        String body = objectMapper.writeValueAsString(usuarioTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveRegistrarUsuarioSemSenha() throws Exception {
        Usuario usuarioTest = new Usuario();
        usuarioTest.setNome("Usuario");
        usuarioTest.setEmail("usuarioTest@usuario.com");

        String body = objectMapper.writeValueAsString(usuarioTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveRegistrarUsuarioComEmailJaCadastrado() throws Exception {
        Usuario usuarioTest = new Usuario();
        usuarioTest.setNome("Usuario");
        usuarioTest.setEmail("usuario@usuario.com");
        usuarioTest.setSenha("senhaTeste");
        String body = objectMapper.writeValueAsString(usuarioTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isBadGateway())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deveEncontrarUsuarioPorId() throws Exception{
        Long idAtual = usuarioAtual.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/usuarios/" +idAtual))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idAtual))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveEncontrarUsuarioPorId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/usuarios/100000"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deveModificarUmUsuario() throws Exception{
        Long idParaModificar = usuarioAtual.getId();

        Usuario usuarioTest = new Usuario();
        usuarioTest.setNome("Usuario Modificado");
        usuarioTest.setEmail("usuariomodificado@email.com");
        usuarioTest.setSenha("senhaModificada@");
        String body = objectMapper.writeValueAsString(usuarioTest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/usuarios/" +idParaModificar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(usuarioTest.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(usuarioTest.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.senha").value(usuarioTest.getSenha()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveModificarUmUsuarioInexistente() throws Exception{
        Usuario usuarioTest = new Usuario();
        usuarioTest.setEmail("usuariomodificado@email.com");

        String body = objectMapper.writeValueAsString(usuarioTest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/usuarios/100000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}
