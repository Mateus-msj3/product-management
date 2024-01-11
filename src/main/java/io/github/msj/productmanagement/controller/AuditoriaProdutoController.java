package io.github.msj.productmanagement.controller;

import io.github.msj.productmanagement.model.dto.AuditoriaResponseDTO;
import io.github.msj.productmanagement.model.dto.DetalheAuditoriaProdutoResponseDTO;
import io.github.msj.productmanagement.service.AuditoriaProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Auditoria de Produtos", description = "Nesta API é possível consultar auditoria de produtos passando " +
        "o id do produto desejado.")
public class AuditoriaProdutoController {

    private final AuditoriaProdutoService auditoriaProdutoService;

    public AuditoriaProdutoController(AuditoriaProdutoService auditoriaProdutoService) {
        this.auditoriaProdutoService = auditoriaProdutoService;
    }


    @Operation(summary = "Consulta os detalhes de campos alterados de um determiando produto.")
    @GetMapping("/produto-detalhes/{id}")
    public ResponseEntity<DetalheAuditoriaProdutoResponseDTO> obterDetalhes(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaProdutoService.detalharMudancas(id));
    }

    @Operation(summary = "Consulta todas as auditorias criadas para um determinado produto.")
    @GetMapping("/produto-revisoes/{id}")
    public ResponseEntity<List<AuditoriaResponseDTO>> obterAuditorias(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaProdutoService.obterAuditorias(id));
    }

}
