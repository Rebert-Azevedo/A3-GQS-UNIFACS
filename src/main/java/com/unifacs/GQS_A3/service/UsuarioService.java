package com.unifacs.GQS_A3.service;

import com.unifacs.GQS_A3.model.Usuario;
import com.unifacs.GQS_A3.repository.UsuarioRepository;
import com.unifacs.GQS_A3.exceptions.CampoNaoPreenchidoException;
import com.unifacs.GQS_A3.exceptions.EmailJaCadastrado;
import com.unifacs.GQS_A3.exceptions.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).
                orElseThrow(() -> new RecursoNaoEncontradoException("Usuario com id " + id + " não encontrado"));
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getEmail() == null || usuario.getSenha() == null) {
            throw new CampoNaoPreenchidoException("nome, email e senha são obrigatórios");
        }

        boolean emailExistente = verificarEmailExistente(usuario.getEmail());
        if (emailExistente) {
            throw new EmailJaCadastrado("Email informado já está cadastrado");
        }
        return usuarioRepository.save(usuario);
    }

    public void removerUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuario com id " + id + " não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public Usuario editarUsuario(Long id, Usuario alterUsuario) {
        Usuario usuario = usuarioRepository
                .findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario com id " + id + " não encontrado"));

        usuario.setNome(alterUsuario.getNome() != null ? alterUsuario.getNome() : usuario.getNome());
        usuario.setSenha(alterUsuario.getSenha() != null ? alterUsuario.getSenha() : usuario.getSenha());
        usuario.setEmail(alterUsuario.getEmail() != null ? alterUsuario.getEmail() : usuario.getEmail());
        usuario.setDataNascimento(alterUsuario.getDataNascimento() != null ? alterUsuario.getDataNascimento() : usuario.getDataNascimento());
        return usuarioRepository.save(usuario);
    }

    public Boolean verificarEmailExistente(String email) {
        Usuario usuarioEmail = usuarioRepository.findByEmailAddress(email);
        return usuarioEmail != null;
    }
}
