package com.example.hubspot.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "ContatoDTO", description = "Dados para criação de contato")
public class ContatoDTO {
    @NotBlank @Email
    @Schema(description = "Email do contato", example = "usuario@exemplo.com")
    private String email;

    @NotBlank
    @Schema(description = "Primeiro nome", example = "João")
    private String firstname;

    @NotBlank
    @Schema(description = "Último nome", example = "Silva")
    private String lastname;
}
