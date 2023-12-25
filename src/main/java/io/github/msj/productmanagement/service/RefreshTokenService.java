package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.model.entities.RefreshToken;
import io.github.msj.productmanagement.repository.RefreshTokenRepository;
import io.github.msj.productmanagement.repository.UserRepository;
import io.github.msj.productmanagement.security.jwt.JwtService;
import io.github.msj.productmanagement.security.user.dto.JWTResponseDTO;
import io.github.msj.productmanagement.security.user.dto.RefreshTokenRequestDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public RefreshToken criarRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .usuario(userRepository.findByEmail(username).orElse(null))
                .token(UUID.randomUUID().toString())
                .dataExpiracao(Instant.now().plusMillis(300000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verificarExpiracao(RefreshToken token){
        if(token.getDataExpiracao().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() +
                    " O token de atualização expirou. Por favor faça um novo login..!");
        }
        return token;
    }

    public JWTResponseDTO atualizarTokenRefresh(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return findByToken(refreshTokenRequestDTO.getToken())
                .map(this::verificarEGerarNovoToken)
                .orElseThrow(() -> new RuntimeException("O token de atualização não está no banco de dados"));
    }

    private JWTResponseDTO verificarEGerarNovoToken(RefreshToken refreshToken) {
        RefreshToken token = verificarExpiracao(refreshToken);
        String accessToken = jwtService.generateToken(token.getUsuario().getEmail());
        return JWTResponseDTO.builder()
                .accessToken(accessToken)
                .token(refreshToken.getToken()).build();
    }

}
