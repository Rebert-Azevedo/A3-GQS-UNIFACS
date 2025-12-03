package com.unifacs.GQS_A3.dto.users;


import java.time.LocalDate;

public record ResponseUserDTO(String nome, String email, LocalDate dataNascimento) {
}
