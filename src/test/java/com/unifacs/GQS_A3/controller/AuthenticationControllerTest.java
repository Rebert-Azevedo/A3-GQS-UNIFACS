package com.unifacs.GQS_A3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifacs.GQS_A3.dto.auth.AuthenticationDTO;
import com.unifacs.GQS_A3.model.Usuario;
import com.unifacs.GQS_A3.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    ObjectMapper objectMapper;

    private Usuario usuarioAtual;

    @BeforeEach
    void inicializar() {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario");
        usuario.setEmail("usuario@usuario.com");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuarioAtual = usuarioRepository.save(usuario);

    }

    @AfterEach
    void desligar() {
        usuarioRepository.deleteAll();
    }

    @BeforeEach
    void mapper() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void deveRegistrarUsuario() throws Exception {
        Usuario usuarioTest = new Usuario();
        usuarioTest.setNome("Usuario");
        usuarioTest.setEmail("usuarioTest@usuario.com");
        usuarioTest.setSenha("usuarioSenha");

        String body = objectMapper.writeValueAsString(usuarioTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/public/registrar")
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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/public/registrar")
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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/public/registrar")
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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/public/registrar")
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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/public/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isBadGateway())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deveFazerLogin() throws Exception {
        String body = objectMapper.writeValueAsString(
                new AuthenticationDTO(usuarioAtual.getEmail(), "senha123")
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveFazerLoginComEmailInexistente() throws Exception {
        AuthenticationDTO usuarioInexistente =
                new AuthenticationDTO("emailInexistente@email.com", "senha");
        String body = objectMapper.writeValueAsString(usuarioInexistente);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveFazerLoginComSenhaIncorreta() throws Exception {
        AuthenticationDTO usuarioInexistente =
                new AuthenticationDTO(usuarioAtual.getEmail(), "senhaErrada");
        String body = objectMapper.writeValueAsString(usuarioInexistente);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}
