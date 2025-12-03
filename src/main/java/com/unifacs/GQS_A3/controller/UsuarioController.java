package com.unifacs.GQS_A3.controller;

import com.unifacs.GQS_A3.dto.users.ModificarUsuarioDTO;
import com.unifacs.GQS_A3.dto.users.ResponseUserDTO;
import com.unifacs.GQS_A3.model.Usuario;
import com.unifacs.GQS_A3.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return new ResponseEntity<>(usuarioService.listarUsuarios(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> buscarUsuario(@PathVariable Long id) {
        ResponseUserDTO usuario = usuarioService.buscarPorId(id);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerUsuario(@PathVariable Long id) {
        usuarioService.removerUsuario(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping({"/editar/{id}"})
    public ResponseEntity<ResponseUserDTO> editarUsuario(@PathVariable Long id, @RequestBody ModificarUsuarioDTO usuario) {
        ModificarUsuarioDTO updatedUsuario = usuarioService.editarUsuario(id, usuario);
        return new ResponseEntity<>(new ResponseUserDTO(updatedUsuario.getNome(),
                updatedUsuario.getEmail(),
                updatedUsuario.getDataNascimento()),
                HttpStatus.OK);
    }

}
