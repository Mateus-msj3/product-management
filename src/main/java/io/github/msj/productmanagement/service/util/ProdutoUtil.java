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
        return processarCampos(produto, configuracaoCamposDTO, true);
    }

    public static ProdutoResponseDTO construirCamposRelatorio(Produto produto,
                                                              ConfiguracaoCamposDTO configuracaoCamposDTO) {
        return processarCampos(produto, configuracaoCamposDTO, false);
    }

    private static ProdutoResponseDTO processarCampos(Produto produto, ConfiguracaoCamposDTO configuracaoCamposDTO,
                                                      boolean inverterCondicao) {
        ProdutoResponseDTO produtoResponseDTO = new ProdutoResponseDTO();

        Map<String, CampoStrategy> strategyMap = criarStrategyMap();
        Map<String, CampoStrategy> hideStrategyMap = criarHideStrategyMap();

        for (Map.Entry<String, CampoStrategy> entry : strategyMap.entrySet()) {
            String campo = entry.getKey();
            CampoStrategy strategy = entry.getValue();

            boolean condicao = configuracaoCamposDTO.getCamposOcultos() != null
                    && configuracaoCamposDTO.getCamposOcultos().contains(campo);
            if (inverterCondicao) {
                condicao = !condicao;
            }

            if (condicao) {
                strategy.atualizarCampo(produtoResponseDTO, produto);
            } else {
                hideStrategyMap.get(campo).atualizarCampo(produtoResponseDTO, produto);
            }
        }

        return produtoResponseDTO;
    }

    private static Map<String, CampoStrategy> criarStrategyMap() {
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
        return strategyMap;
    }

    private static Map<String, CampoStrategy> criarHideStrategyMap() {
        Map<String, CampoStrategy> hideStrategyMap = new HashMap<>();
        for (String key : criarStrategyMap().keySet()) {
            hideStrategyMap.put(key, new NullStrategy());
        }
        return hideStrategyMap;
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
