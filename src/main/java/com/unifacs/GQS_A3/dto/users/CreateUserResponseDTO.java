package com.unifacs.GQS_A3.dto.users;

import java.time.LocalDateTime;

public record CreateUserResponseDTO(String nome,
                                    String email,
                                    LocalDateTime dataCriacao) {
}
