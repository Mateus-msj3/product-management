package io.github.msj.productmanagement.service.util;

import io.github.msj.productmanagement.model.dto.ConfiguracaoCamposDTO;
import io.github.msj.productmanagement.model.dto.ProdutoAgregadoDTO;
import io.github.msj.productmanagement.model.dto.ProdutoResponseDTO;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.strategy.campo.CampoStrategy;
import io.github.msj.productmanagement.strategy.campo.impl.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProdutoUtil {

    public static ProdutoResponseDTO construirDTO(Produto produto, ConfiguracaoCamposDTO configuracaoCamposDTO) {
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

    public static ProdutoAgregadoDTO construirRetornoProdutoAgregado(Produto produto) {
        ProdutoAgregadoDTO produtoAgregadoDTO = new ProdutoAgregadoDTO();
        produtoAgregadoDTO.setNome(produto.getNome());
        produtoAgregadoDTO.setCusto(produto.getValorCusto());

        BigDecimal custoTotal = produto.getValorCusto()
                .multiply(new BigDecimal(produto.getQuantidadeEstoque()));
        produtoAgregadoDTO.setCustoTotal(custoTotal);

        produtoAgregadoDTO.setQuantidade(produto.getQuantidadeEstoque());

        BigDecimal valorTotalPrevisto = produto.getValorCusto()
                .multiply(new BigDecimal(produto.getQuantidadeEstoque()));

        produtoAgregadoDTO.setValorTotalPrevisto(valorTotalPrevisto);

        return produtoAgregadoDTO;

    }

}
