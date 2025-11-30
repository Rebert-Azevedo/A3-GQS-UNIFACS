package com.unifacs.GQS_A3.service;

import com.unifacs.GQS_A3.model.Usuario;
import com.unifacs.GQS_A3.repository.UsuarioRepository;
import com.unifacs.GQS_A3.exceptions.CampoNaoPreenchidoException;
import com.unifacs.GQS_A3.exceptions.RecursoNaoEncontradoException;
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
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    public void deveRegistrarUmNovoUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario");
        usuario.setEmail("usuario@usuario.com");
        usuario.setSenha("usuarioSenha");

        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario novoUsuario = usuarioService.registrarUsuario(usuario);
        Assertions.assertNotNull(novoUsuario);
        Assertions.assertEquals(usuario.getNome(), novoUsuario.getNome());
        Assertions.assertEquals(usuario.getEmail(), novoUsuario.getEmail());
        Assertions.assertEquals(usuario.getSenha(), novoUsuario.getSenha());
    }

    @Test
    public void naoDeveRegistrarUmNovoUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario");
        usuario.setSenha("usuarioSenha");

        Assertions.assertThrows(CampoNaoPreenchidoException.class, () -> usuarioService.registrarUsuario(usuario));
    }

    @Test
    public void deveListarTodosOsUsuarios(){
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Usuario1");
        usuario1.setEmail("usuario1@usuario.com");
        usuario1.setSenha("usuario1Senha");

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Usuario2");
        usuario2.setEmail("usuario2@usuario.com");
        usuario2.setSenha("usuario2Senha");

        List<Usuario> minhaListaUsuarios = Arrays.asList(usuario1, usuario2);

        Mockito.when(usuarioRepository.findAll()).thenReturn(minhaListaUsuarios);

        List<Usuario> listaUsuario = usuarioService.listarUsuarios();

        Assertions.assertEquals(2, listaUsuario.size());
    }


    @Test
    public void deveEncontrarUsuarioPorId(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuario");
        usuario.setEmail("usuario@usuario.com");
        usuario.setSenha("usuarioSenha");

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario usuarioBuscado = usuarioService.buscarPorId(1L);
        Assertions.assertNotNull(usuarioBuscado);
        Assertions.assertEquals(usuario.getId(), usuarioBuscado.getId());
        Assertions.assertEquals(usuario.getNome(), usuarioBuscado.getNome());
    }

    @Test
    public void naoDeveEncontrarUsuarioPorId(){
        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> usuarioService.buscarPorId(1L));
    }

    @Test
    public void deveModificarUmUsuario(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuario");
        usuario.setEmail("usuario@usuario.com");
        usuario.setSenha("usuarioSenha");

        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario usuarioModificado = new Usuario();
        usuarioModificado.setId(1L);
        usuarioModificado.setNome("usuarioModificado");
        usuarioModificado.setEmail("usuarioModificado@usuario.com");
        usuarioModificado.setSenha("usuarioSenha");

        Usuario usuarioAlterado = usuarioService.editarUsuario(1L, usuarioModificado);

        Assertions.assertEquals(usuarioModificado.getNome(), usuarioAlterado.getNome());
        Assertions.assertEquals(usuarioModificado.getEmail(), usuarioAlterado.getEmail());
    }

    @Test
    public void naoDeveModificarUmUsuario(){
        Usuario usuarioModificado = new Usuario();
        usuarioModificado.setId(1L);
        usuarioModificado.setNome("usuarioModificado");
        usuarioModificado.setEmail("usuarioModificado@usuario.com");
        usuarioModificado.setSenha("usuarioSenha");

        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> usuarioService.editarUsuario(1L, usuarioModificado));
    }
}
