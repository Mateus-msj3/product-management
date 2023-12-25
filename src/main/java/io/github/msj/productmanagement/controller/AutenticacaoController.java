package io.github.msj.productmanagement.controller;


import io.github.msj.productmanagement.security.user.dto.LoginRequestDTO;
import io.github.msj.productmanagement.security.user.dto.JWTResponseDTO;
import io.github.msj.productmanagement.security.user.dto.RefreshTokenRequestDTO;
import io.github.msj.productmanagement.service.AutenticacaoService;
import io.github.msj.productmanagement.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autenticacao")
public class AutenticacaoController {

    private AutenticacaoService autenticacaoService;

    private RefreshTokenService refreshTokenService;

    public AutenticacaoController(AutenticacaoService autenticacaoService, RefreshTokenService refreshTokenService) {
        this.autenticacaoService = autenticacaoService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public JWTResponseDTO autenticar(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return autenticacaoService.autenticarUsuario(loginRequestDTO);
    }

    @PostMapping("/refreshToken")
    public JWTResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.atualizarTokenRefresh(refreshTokenRequestDTO);
    }
}
