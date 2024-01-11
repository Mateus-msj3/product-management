package io.github.msj.productmanagement.controller;

import io.github.msj.productmanagement.model.dto.ProdutoAgregadoPageDTO;
import io.github.msj.productmanagement.model.dto.ProdutoFiltroDTO;
import io.github.msj.productmanagement.model.dto.ProdutoPageDTO;
import io.github.msj.productmanagement.service.ConsultaProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consulta-produtos")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Consulta de Produtos", description = "Nesta API é possível consultar produtos passando qualquer tipo de " +
        "filtro e será retornado de forma paginda")
public class ConsultaProdutoController {

    private final ConsultaProdutoService consultaProdutoService;

    public ConsultaProdutoController(ConsultaProdutoService consultaProdutoService) {
        this.consultaProdutoService = consultaProdutoService;
    }

    @Operation(summary = "Consulta que retorna páginas de produtos")
    @GetMapping("/")
    public ResponseEntity<ProdutoPageDTO> consultar(ProdutoFiltroDTO produtoFiltroDTO,
                                                    @RequestParam(defaultValue = "0")
                                                    @PositiveOrZero int pagina,
                                                    @RequestParam(defaultValue = "10")
                                                    @Positive @Max(100) int tamanhoPagina,
                                                    @RequestParam(defaultValue = "id,desc") String[] parametrosOrdenacao) {
        return ResponseEntity.ok(consultaProdutoService.buscarProdutosComFiltros(produtoFiltroDTO, pagina, tamanhoPagina, parametrosOrdenacao));
    }

    @Operation(summary = "Consulta que retorna páginas de produtos agregados")
    @GetMapping("/agregados")
    public ResponseEntity<ProdutoAgregadoPageDTO> listarProdutosAgregados(ProdutoFiltroDTO produtoFiltroDTO,
                                                                          @RequestParam(defaultValue = "0")
                                                                          @PositiveOrZero int pagina,
                                                                          @RequestParam(defaultValue = "10")
                                                                          @Positive @Max(100) int tamanhoPagina,
                                                                          @RequestParam(defaultValue = "id,desc") String[] parametrosOrdenacao) {
        return ResponseEntity.ok(consultaProdutoService.listarValoresAgregados(produtoFiltroDTO, pagina, tamanhoPagina, parametrosOrdenacao));
    }


}
