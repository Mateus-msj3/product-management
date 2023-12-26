package io.github.msj.productmanagement.controller;

import io.github.msj.productmanagement.model.dto.ConfiguracaoCamposDTO;
import io.github.msj.productmanagement.service.ConfiguracaoCamposService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracao")
@AllArgsConstructor
public class ConfiguracaoCamposController {

    private ConfiguracaoCamposService configuracaoService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<ConfiguracaoCamposDTO> obterConfiguracao() {
        return ResponseEntity.ok(configuracaoService.obterConfiguracao());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ConfiguracaoCamposDTO> criarConfiguracao(@RequestBody ConfiguracaoCamposDTO configuracao) {
        ConfiguracaoCamposDTO novaConfiguracao = configuracaoService.criarConfiguracao(configuracao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConfiguracao);
    }

}
