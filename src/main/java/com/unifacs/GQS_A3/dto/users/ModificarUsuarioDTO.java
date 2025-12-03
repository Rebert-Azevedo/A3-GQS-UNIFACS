package com.unifacs.GQS_A3.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModificarUsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;
}
