package io.github.msj.productmanagement.controller;

import io.github.msj.productmanagement.model.dto.ProdutoFiltroDTO;
import io.github.msj.productmanagement.service.RelatorioProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/relatorios")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Relatório de Produtos", description = "Nesta API é possível gerar relatórios do tipo CSV ou XLSX " +
        "informando ou não os campos desejados.")
public class RelatorioProdutoController {

    private final RelatorioProdutoService relatorioProdutoService;

    public RelatorioProdutoController(RelatorioProdutoService relatorioProdutoService) {
        this.relatorioProdutoService = relatorioProdutoService;
    }

    @Operation(summary = "Gera um relatório de acordo com o formato.")
    @GetMapping("/")
    public void gerarRelatorio(HttpServletResponse httpServletResponse,
                               @RequestParam String tipoRelatorio, ProdutoFiltroDTO produtoFiltroDTO,
                               @RequestParam(required = false,
                                       defaultValue = "id,nome,sku,ativo,categoria,valorCusto,icms,valorVenda," +
                                               "imagemProduto,dataCadastro,quantidadeEstoque,criadoPor") String[] campos) throws IOException {
        relatorioProdutoService.gerarRelatorio(httpServletResponse, tipoRelatorio, produtoFiltroDTO, campos);
    }



}
