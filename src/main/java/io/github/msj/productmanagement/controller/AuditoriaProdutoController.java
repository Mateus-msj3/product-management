package io.github.msj.productmanagement.controller;

import io.github.msj.productmanagement.model.dto.AuditoriaResponseDTO;
import io.github.msj.productmanagement.model.dto.DetalheAuditoriaProdutoResponseDTO;
import io.github.msj.productmanagement.service.AuditoriaProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaProdutoController {

    private final AuditoriaProdutoService auditoriaProdutoService;

    public AuditoriaProdutoController(AuditoriaProdutoService auditoriaProdutoService) {
        this.auditoriaProdutoService = auditoriaProdutoService;
    }


    @GetMapping("/produto-detalhes/{id}")
    public ResponseEntity<DetalheAuditoriaProdutoResponseDTO> obterDetalhes(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaProdutoService.detalharMudancas(id));
    }

    @GetMapping("/produto-revisoes/{id}")
    public ResponseEntity<List<AuditoriaResponseDTO>> obterAuditorias(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaProdutoService.obterAuditorias(id));
    }

}
