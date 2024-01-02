package io.github.msj.productmanagement.controller;

import io.github.msj.productmanagement.model.dto.DetalheAuditoriaResponseDTO;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.service.AuditoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Produto>> consultar(@PathVariable Long id) {
        List<Produto> produtos = auditoriaService.obterAuditoriaProduto(id);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/detalhes/{id}")
    public ResponseEntity<DetalheAuditoriaResponseDTO> obterDetalhes(@PathVariable Long id) {
        DetalheAuditoriaResponseDTO detalheAuditoriaResponseDTO = auditoriaService.detalharMudancas(id);
        return ResponseEntity.ok(detalheAuditoriaResponseDTO);
    }

    @GetMapping(path = "/{id}/revisions")
    public ResponseEntity<?> getRevisions(@PathVariable(name = "id") String customerId,
                                          @RequestParam(value = "fetchChanges", required = false) boolean fetchChanges) {

        List results = auditoriaService.getRevisions(Long.valueOf(customerId), fetchChanges);
        return ResponseEntity.ok(results);
    }

}
