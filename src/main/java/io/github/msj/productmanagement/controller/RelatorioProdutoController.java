package io.github.msj.productmanagement.controller;

import io.github.msj.productmanagement.model.dto.ProdutoFiltroDTO;
import io.github.msj.productmanagement.service.RelatorioProdutoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioProdutoController {

    private final RelatorioProdutoService relatorioProdutoService;

    public RelatorioProdutoController(RelatorioProdutoService relatorioProdutoService) {
        this.relatorioProdutoService = relatorioProdutoService;
    }

    @GetMapping("/")
    public void consultar(HttpServletResponse httpServletResponse, @RequestParam String tipoRelatorio, ProdutoFiltroDTO produtoFiltroDTO, @RequestParam String[] campos) throws IOException {
        relatorioProdutoService.gerarRelatorio(httpServletResponse, tipoRelatorio, produtoFiltroDTO, campos);
    }



}
