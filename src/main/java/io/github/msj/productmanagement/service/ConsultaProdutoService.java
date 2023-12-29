package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.model.dto.*;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.repository.ProdutoRepository;
import io.github.msj.productmanagement.repository.specifications.ProdutoSpecification;
import io.github.msj.productmanagement.service.util.ProdutoUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static io.github.msj.productmanagement.service.util.OrdenacaoUtil.construirCriteriosDeOrdenacao;

@Service
@AllArgsConstructor
public class ConsultaProdutoService {

    private final ProdutoRepository produtoRepository;

    private final ProdutoService produtoService;

    public ProdutoPageDTO buscarProdutosComFiltros(ProdutoFiltroDTO produtoFiltroDTO, int pagina, int tamanhoPagina, String[] parametrosOrdenacao) {
        Specification<Produto> specification = ProdutoSpecification.combinarFiltros(produtoFiltroDTO);
        Pageable pageable = PageRequest.of(pagina, tamanhoPagina, Sort.by(construirCriteriosDeOrdenacao(parametrosOrdenacao)));
        Page<ProdutoResponseDTO> produtos = produtoRepository.findAll(specification, pageable)
                .map(produtoService::construirRetorno);
        return new ProdutoPageDTO(produtos.getContent(), produtos.getTotalElements(), produtos.getTotalPages(), produtos.getNumber());
    }

    public ProdutoAgregadoPageDTO listarValoresAgregados(ProdutoFiltroDTO produtoFiltroDTO, int pagina, int tamanhoPagina, String[] parametrosOrdenacao) {
        Specification<Produto> specification = ProdutoSpecification.combinarFiltros(produtoFiltroDTO);
        Pageable pageable = PageRequest.of(pagina, tamanhoPagina, Sort.by(construirCriteriosDeOrdenacao(parametrosOrdenacao)));
        Page<ProdutoAgregadoDTO> produtos = produtoRepository.findAll(specification, pageable)
                .map(ProdutoUtil::construirRetornoProdutoAgregado);
        return new ProdutoAgregadoPageDTO(produtos.getContent(), produtos.getTotalElements(), produtos.getTotalPages(), produtos.getNumber());
    }


}
