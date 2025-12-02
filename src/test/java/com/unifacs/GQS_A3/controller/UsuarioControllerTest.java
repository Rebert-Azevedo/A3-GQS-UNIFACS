package com.unifacs.GQS_A3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifacs.GQS_A3.model.Usuario;
import com.unifacs.GQS_A3.model.enums.UserRole;
import com.unifacs.GQS_A3.repository.UsuarioRepository;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UsuarioRepository usuarioRepository;

    private Usuario usuarioComum;

    @BeforeEach
    void inicializar() {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario");
        usuario.setEmail("usuario@usuario.com");
        usuario.setSenha("usuarioSenha");
        usuario.setRole(UserRole.USER);

        Usuario admin = new Usuario();
        admin.setNome("Admin");
        admin.setEmail("admin@adm.com");
        admin.setSenha("admin");
        admin.setRole(UserRole.ADMIN);

        usuarioComum = usuarioRepository.save(usuario);
        usuarioRepository.save(admin);
    }

    @AfterEach
    void desligar() {
        usuarioRepository.deleteAll();
    }

    ObjectMapper objectMapper;

    @BeforeEach
    void mapper() {
        objectMapper = new ObjectMapper();
    }


    @Test
    @WithMockUser(username = "admin@adm.com", roles = {"USER", "ADMIN"})
    public void deveListarUsuariosParaUsuariosAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/usuarios/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void naoDeveListarUsuariosParaUsuariosComuns() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/usuarios/admin"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void deveEncontrarUsuarioPorId() throws Exception {
        Long idAtual = usuarioComum.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/usuarios/" + idAtual))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(usuarioComum.getNome()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@adm.com", roles = {"USER", "ADMIN"})
    public void naoDeveEncontrarUsuarioComIdInexistente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/usuarios/100000"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "usuario@usuario.com", roles = "USER")
    public void deveModificarUmUsuario() throws Exception {
        Long idParaModificar = usuarioComum.getId();

        Usuario usuarioTest = new Usuario();
        usuarioTest.setNome("Usuario Modificado");
        usuarioTest.setEmail("usuariomodificado@email.com");
        usuarioTest.setSenha("senhaModificada@");
        String body = objectMapper.writeValueAsString(usuarioTest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/usuarios/editar/" + idParaModificar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(usuarioTest.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(usuarioTest.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataNascimento").value(usuarioTest.getDataNascimento()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@adm.com", roles = {"USER", "ADMIN"})
    public void naoDeveModificarUmUsuarioInexistente() throws Exception {
        Usuario usuarioTest = new Usuario();
        usuarioTest.setEmail("usuariomodificado@email.com");

        String body = objectMapper.writeValueAsString(usuarioTest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/usuarios/editar/100000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}
