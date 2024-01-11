package io.github.msj.productmanagement.controller;


import io.github.msj.productmanagement.model.dto.ProdutoRequestDTO;
import io.github.msj.productmanagement.model.dto.ProdutoResponseDTO;
import io.github.msj.productmanagement.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Produtos", description = "Nesta API é possível criar, editar, listar por id, listar todos, " +
        "deletar e inativar produtos.")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(summary = "Cria um novo Produto.")
    @PostMapping("/")
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {
        ProdutoResponseDTO produtoResponseDTO = produtoService.salvar(produtoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoResponseDTO);
    }

    @Operation(summary = "Edita um novo Produto.")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> editar(@Valid @RequestBody ProdutoRequestDTO produtoRequestDTO,
                                                     @PathVariable Long id) {
        ProdutoResponseDTO produtoResponseDTO = produtoService.editar(id, produtoRequestDTO);
        return ResponseEntity.ok(produtoResponseDTO);
    }

    @Operation(summary = "Lista todos os Produtos.")
    @GetMapping("/")
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        List<ProdutoResponseDTO> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Lista um Produto por id.")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> listarPorId(@PathVariable Long id) {
        ProdutoResponseDTO produtoResponseDTO = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produtoResponseDTO);
    }

    @Operation(summary = "Inativa um Produto.")
    @PutMapping("/{id}/inativar")
    public ResponseEntity<ProdutoResponseDTO> inativar(@PathVariable Long id) {
        produtoService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta um Produto.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
