package com.unifacs.GQS_A3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifacs.GQS_A3.model.Funcionario;
import com.unifacs.GQS_A3.repository.FuncionarioRepository;
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
public class FuncionarioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private FuncionarioRepository funcionarioRepository;
    private Funcionario funcionarioAtual;

    ObjectMapper objectMapper;


    @BeforeEach
    void inicializar() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("funcionario");
        funcionario.setEmail("funcionario@funcionario.com");
        funcionario.setSenha("funcionarioSenha");
        funcionario.setCargo("Assistente T.I");
        funcionario.setSalario(1900.0);
        funcionarioAtual = funcionarioRepository.save(funcionario);
    }

    @AfterEach
    void desligar() {
        funcionarioRepository.deleteAll();
    }

    @BeforeEach
    void mapper() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void usuariosAdminDevemAdicionarNovosFuncionarios() throws Exception {
        Funcionario funcionarioTeste = new Funcionario();
        funcionarioTeste.setNome("Funcionario");
        funcionarioTeste.setEmail("funcionario2@funcionario.com");
        funcionarioTeste.setSenha("funcionarioSenha");
        funcionarioTeste.setCargo("Assistente T.I");
        funcionarioTeste.setSalario(1900.0);

        String body = objectMapper.writeValueAsString(funcionarioTeste);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/funcionarios/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Funcionario"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "usuario@usuario.com", roles = "USER")
    public void usuariosComunsNaoDevemAdicionarNovosFuncionarios() throws Exception {
        Funcionario funcionarioTeste = new Funcionario();
        funcionarioTeste.setNome("funcionario");
        funcionarioTeste.setEmail("funcionario@usuario.com");
        funcionarioTeste.setSenha("funcionarioSenha");
        funcionarioTeste.setCargo("Assistente T.I");
        funcionarioTeste.setSalario(1900.0);

        String body = objectMapper.writeValueAsString(funcionarioTeste);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/funcionarios/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void usuariosAdminDevemListarTodosOsFuncionarios() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/funcionarios/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = "USER")
    public void usuariosComunsNaoDevemListarTodosOsFuncionarios() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/funcionarios/admin"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void usuariosAdminDevemEncontrarFuncionariosPorId() throws Exception {
        Long idAtual = funcionarioAtual.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/funcionarios/admin/" + idAtual))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idAtual))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void usuariosAdminNaoDevemEncontrarFuncionariosInexistentes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/funcionarios/admin" + -1))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = "USER")
    public void usuariosComunsNaoDevemEncontrarFuncionariosPorId() throws Exception {
        Long idAtual = funcionarioAtual.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/funcionarios/admin" + idAtual))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void usuariosAdminDevemModificarUmFuncionario() throws Exception {
        Long idParaModificar = funcionarioAtual.getId();

        Funcionario funcionarioModificado = new Funcionario();
        funcionarioModificado.setCargo("Supervisor T.I");
        funcionarioModificado.setSalario(3500.0);

        String body = objectMapper.writeValueAsString(funcionarioModificado);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/funcionarios/admin/" + idParaModificar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cargo").value(funcionarioModificado.getCargo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salario").value(funcionarioModificado.getSalario()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = "USER")
    public void usuariosComunsNaoDevemModificarUmFuncionario() throws Exception {
        Long idParaModificar = funcionarioAtual.getId();

        Funcionario funcionarioModificado = new Funcionario();
        funcionarioModificado.setCargo("Supervisor T.I");
        funcionarioModificado.setSalario(3500.0);

        String body = objectMapper.writeValueAsString(funcionarioModificado);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/funcionarios/admin/" + idParaModificar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void usuariosAdminDevemRemoverUmFuncionario() throws Exception {
        Long idParaRemover = funcionarioAtual.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/funcionarios/admin/" + idParaRemover))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"USER", "ADMIN"})
    public void usuariosAdminNaoDevemRemoverUmFuncionarioInexistente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/funcionarios/admin/" + -1))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = "USER")
    public void usuariosComunsNaoDevemRemoverUmFuncionario() throws Exception {
        Long idParaRemover = funcionarioAtual.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/funcionarios/admin/" + idParaRemover))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }
}
