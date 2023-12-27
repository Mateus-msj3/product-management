package io.github.msj.productmanagement.controller;

import io.github.msj.productmanagement.model.dto.ProdutoAgregadoDTO;
import io.github.msj.productmanagement.model.dto.ProdutoFiltroDTO;
import io.github.msj.productmanagement.model.dto.ProdutoResponseDTO;
import io.github.msj.productmanagement.service.ConsultaProdutoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consulta-produtos")
public class ConsultaProdutoController {

    private final ConsultaProdutoService consultaProdutoService;

    public ConsultaProdutoController(ConsultaProdutoService consultaProdutoService) {
        this.consultaProdutoService = consultaProdutoService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<ProdutoResponseDTO>> consultar(ProdutoFiltroDTO produtoFiltroDTO, Pageable pageable) {
        return ResponseEntity.ok(consultaProdutoService.buscarProdutosComFiltros(produtoFiltroDTO, pageable));
    }

    @GetMapping("/agregados")
    public ResponseEntity<Page<ProdutoAgregadoDTO>> listarProdutosAgregados(ProdutoFiltroDTO produtoFiltroDTO,
                                                                            Pageable pageable) {
        return ResponseEntity.ok(consultaProdutoService.listarValoresAgregados(produtoFiltroDTO, pageable));
    }


}
