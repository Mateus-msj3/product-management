package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.model.dto.ConfiguracaoCamposDTO;
import io.github.msj.productmanagement.model.dto.ProdutoAgregadoDTO;
import io.github.msj.productmanagement.model.dto.ProdutoFiltroDTO;
import io.github.msj.productmanagement.model.dto.ProdutoResponseDTO;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.repository.ProdutoRepository;
import io.github.msj.productmanagement.repository.specifications.ProdutoSpecification;
import io.github.msj.productmanagement.strategy.campo.CampoStrategy;
import io.github.msj.productmanagement.strategy.campo.impl.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConsultaProdutoService {

    private final ProdutoRepository produtoRepository;

    private final ModelMapper modelMapper;

    private final ConfiguracaoCamposService configuracaoCamposService;

    private final UsuarioService usuarioService;

    public Page<ProdutoResponseDTO> buscarProdutosComFiltros(ProdutoFiltroDTO produtoFiltroDTO, Pageable pageable) {
        Specification<Produto> specification = ProdutoSpecification.combinarFiltros(produtoFiltroDTO);
        return produtoRepository.findAll(specification, pageable)
                .map(this::construirRetorno);
    }

    public Page<ProdutoAgregadoDTO> listarValoresAgregados(ProdutoFiltroDTO produtoFiltroDTO, Pageable pageable) {
        Specification<Produto> specification = ProdutoSpecification.combinarFiltros(produtoFiltroDTO);
        Page<Produto> produtos = produtoRepository.findAll(specification, pageable);

        List<ProdutoAgregadoDTO> produtosAgregados = produtos.getContent().stream()
                .map(this::construirRetornoProdutoAgregado)
                .collect(Collectors.toList());

        return new PageImpl<>(produtosAgregados, pageable, produtos.getTotalElements());
    }

    private ProdutoResponseDTO construirDTO(Produto produto, ConfiguracaoCamposDTO configuracaoCamposDTO) {
        ProdutoResponseDTO produtoResponseDTO = new ProdutoResponseDTO();

        Map<String, CampoStrategy> strategyMap = new HashMap<>();
        strategyMap.put("id", new IdStrategy());
        strategyMap.put("nome", new NomeStrategy());
        strategyMap.put("imagemProduto", new ImagemStrategy());
        strategyMap.put("ativo", new AtivoStrategy());
        strategyMap.put("dataCadastro", new DataCadastroStrategy());
        strategyMap.put("sku", new SKUStrategy());
        strategyMap.put("categoria", new CategoriaStrategy());
        strategyMap.put("quantidadeEstoque", new QuantidadeEstoqueStrategy());
        strategyMap.put("valorVenda", new ValorVendaStrategy());
        strategyMap.put("valorCusto", new ValorCustoStrategy());
        strategyMap.put("icms", new IcmsStrategy());
        strategyMap.put("criadoPor", new CriadorPorStrategy());

        Map<String, CampoStrategy> hideStrategyMap = new HashMap<>();
        hideStrategyMap.put("id", new NullStrategy());
        hideStrategyMap.put("nome", new NullStrategy());
        hideStrategyMap.put("imagemProduto", new NullStrategy());
        hideStrategyMap.put("ativo", new NullStrategy());
        hideStrategyMap.put("dataCadastro", new NullStrategy());
        hideStrategyMap.put("sku", new NullStrategy());
        hideStrategyMap.put("categoria", new NullStrategy());
        hideStrategyMap.put("quantidadeEstoque", new NullStrategy());
        hideStrategyMap.put("valorVenda", new NullStrategy());
        hideStrategyMap.put("valorCusto", new NullStrategy());
        hideStrategyMap.put("icms", new NullStrategy());
        hideStrategyMap.put("criadoPor", new NullStrategy());

        for (Map.Entry<String, CampoStrategy> entry : strategyMap.entrySet()) {
            String campo = entry.getKey();
            CampoStrategy strategy = entry.getValue();

            if (configuracaoCamposDTO.getCamposOcultos() == null ||
                    !configuracaoCamposDTO.getCamposOcultos().contains(campo)) {
                strategy.atualizarCampo(produtoResponseDTO, produto);
            } else {
                new NullStrategy().atualizarCampo(produtoResponseDTO, produto);
            }
        }

        return produtoResponseDTO;
    }

    private ProdutoResponseDTO construirRetorno(Produto produto) {
        if (usuarioService.isEstoquista()) {
            ConfiguracaoCamposDTO configuracaoCamposDTO = configuracaoCamposService.obterConfiguracao();
            return construirDTO(produto, configuracaoCamposDTO);
        }
        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    private ProdutoAgregadoDTO construirRetornoProdutoAgregado(Produto produto) {
        ProdutoAgregadoDTO produtoAgregadoDTO = new ProdutoAgregadoDTO();
        produtoAgregadoDTO.setNome(produto.getNome());
        produtoAgregadoDTO.setCusto(produto.getValorCusto());

        // Calculando o custo total (custo * quantidade)
        BigDecimal custoTotal = produto.getValorCusto()
                .multiply(new BigDecimal(produto.getQuantidadeEstoque()));
        produtoAgregadoDTO.setCustoTotal(custoTotal);

        produtoAgregadoDTO.setQuantidade(produto.getQuantidadeEstoque());

        // Calculando o valor total previsto (valorPrevistoPorItem * quantidade)
        BigDecimal valorTotalPrevisto = produto.getValorCusto()
                .multiply(new BigDecimal(produto.getQuantidadeEstoque()));

        produtoAgregadoDTO.setValorTotalPrevisto(valorTotalPrevisto);

        return produtoAgregadoDTO;

    }
}
