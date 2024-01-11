package io.github.msj.productmanagement.controller;

import io.github.msj.productmanagement.model.dto.ConfiguracaoCamposDTO;
import io.github.msj.productmanagement.service.ConfiguracaoCamposService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracao")
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Configuração para visualização de campos", description = "Nesta API é possível criar configurações " +
        "de quais campos deve ser ocultados quando um usuário do tipo estoquista consultar determinado produto.")
public class ConfiguracaoCamposController {

    private ConfiguracaoCamposService configuracaoService;

    @Operation(summary = "Consulta e retorna sempre a última configuração criada.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<ConfiguracaoCamposDTO> obterConfiguracao() {
        return ResponseEntity.ok(configuracaoService.obterConfiguracao());
    }

    @Operation(summary = "Cria uma configuração para ocultar campos da consulta de produtos.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ConfiguracaoCamposDTO> criarConfiguracao(@RequestBody ConfiguracaoCamposDTO configuracao) {
        ConfiguracaoCamposDTO novaConfiguracao = configuracaoService.criarConfiguracao(configuracao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConfiguracao);
    }

}
