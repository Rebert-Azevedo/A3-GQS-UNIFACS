package com.unifacs.GQS_A3.controller;

import com.unifacs.GQS_A3.dto.auth.AuthResponseDTO;
import com.unifacs.GQS_A3.dto.auth.AuthenticationDTO;
import com.unifacs.GQS_A3.dto.users.CreateUserDTO;
import com.unifacs.GQS_A3.dto.users.CreateUserResponseDTO;
import com.unifacs.GQS_A3.model.Usuario;
import com.unifacs.GQS_A3.model.enums.UserRole;
import com.unifacs.GQS_A3.repository.UsuarioRepository;
import com.unifacs.GQS_A3.security.TokenService;
import com.unifacs.GQS_A3.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/public")
public class AuthenticatorController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public AuthenticatorController(UsuarioService usuarioService,
                                   PasswordEncoder passwordEncoder,
                                   TokenService tokenService,
                                   UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/registrar")
    public ResponseEntity<CreateUserResponseDTO> registrarUsuario(@RequestBody CreateUserDTO usuario) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuario.nome());
        novoUsuario.setEmail(usuario.email());
        novoUsuario.setSenha(usuario.senha());
        novoUsuario.setDataNascimento(usuario.dataNascimento());
        Usuario usuarioCadastrado = usuarioService.registrarUsuario(novoUsuario);
        return new ResponseEntity<>(new CreateUserResponseDTO(usuarioCadastrado.getNome(),
                usuarioCadastrado.getEmail(),
                usuarioCadastrado.getDataCriacao()), HttpStatus.CREATED);
    }

    @PostMapping("/registrar/admin")
    public ResponseEntity<CreateUserResponseDTO> registrarUsuarioAdm(@RequestBody CreateUserDTO usuario) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuario.nome());
        novoUsuario.setEmail(usuario.email());
        novoUsuario.setSenha(usuario.senha());
        novoUsuario.setDataNascimento(usuario.dataNascimento());
        novoUsuario.setRole(UserRole.ADMIN);
        Usuario usuarioCadastrado = usuarioService.registrarUsuario(novoUsuario);
        return new ResponseEntity<>(new CreateUserResponseDTO(usuarioCadastrado.getNome(),
                usuarioCadastrado.getEmail(),
                usuarioCadastrado.getDataCriacao()), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthenticationDTO login) {

        Usuario usuario = this.usuarioRepository.findByEmailAddress(login.email());
        if (usuario != null && passwordEncoder.matches(login.senha(), usuario.getSenha())) {
            String token = this.tokenService.generateToken(usuario);

            return new ResponseEntity<>(new AuthResponseDTO(usuario.getNome(), token), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
