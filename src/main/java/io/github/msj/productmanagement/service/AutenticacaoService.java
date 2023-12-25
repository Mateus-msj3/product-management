package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.model.entities.RefreshToken;
import io.github.msj.productmanagement.security.jwt.JwtService;
import io.github.msj.productmanagement.security.user.dto.LoginRequestDTO;
import io.github.msj.productmanagement.security.user.dto.JWTResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    public AutenticacaoService(AuthenticationManager authenticationManager,
                               JwtService jwtService,
                               RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public JWTResponseDTO autenticarUsuario(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getSenha())
        );

        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.criarRefreshToken(loginRequestDTO.getEmail());
            return JWTResponseDTO.builder()
                    .accessToken(jwtService.generateToken(loginRequestDTO.getEmail()))
                    .token(refreshToken.getToken())
                    .build();

        } else {
            throw new UsernameNotFoundException("Solicitação de login inválida..!!");
        }


    }

}
