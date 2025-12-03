package com.unifacs.GQS_A3.dto.users;

import java.time.LocalDate;

public record CreateUserDTO(String nome,
                            String email,
                            String senha,
                            LocalDate dataNascimento) {
}
