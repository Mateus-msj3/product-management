package io.github.msj.productmanagement.security.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "O email deve ser informado")
    @Email(message = "Por favor forneca um email válido")
    private String email;

    @NotBlank(message = "A senha deve ser informada")
    private String senha;

}
