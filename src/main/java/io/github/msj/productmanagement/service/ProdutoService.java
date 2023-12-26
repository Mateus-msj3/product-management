package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.exception.NotFoundException;
import io.github.msj.productmanagement.model.dto.CategoriaDTO;
import io.github.msj.productmanagement.model.dto.ConfiguracaoCamposDTO;
import io.github.msj.productmanagement.model.dto.ProdutoRequestDTO;
import io.github.msj.productmanagement.model.dto.ProdutoResponseDTO;
import io.github.msj.productmanagement.model.entities.Categoria;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.repository.CategoriaRepository;
import io.github.msj.productmanagement.repository.ProdutoRepository;
import io.github.msj.productmanagement.strategy.campo.CampoStrategy;
import io.github.msj.productmanagement.strategy.campo.impl.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
public class ProdutoService {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);
    private static final String MENSAGEM_PRODUTO_NAO_ENCONTRADO = "Não foi encontrado produto com ID: ";

    private final ProdutoRepository produtoRepository;

    private final CategoriaRepository categoriaRepository;

    private final ModelMapper modelMapper;

    private final ConfiguracaoCamposService configuracaoCamposService;

    private final UsuarioService usuarioService;

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO produtoRequestDTO) {
        Categoria categoria = categoriaRepository.findById(produtoRequestDTO.getCategoria().getId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada com ID: "
                        + produtoRequestDTO.getCategoria().getId()));

        produtoRequestDTO.setCategoria(modelMapper.map(categoria, CategoriaDTO.class));
        produtoRequestDTO.setDataCadastro(LocalDate.now());
        Produto produto = produtoRepository.save(modelMapper.map(produtoRequestDTO, Produto.class));
        logger.info("Produto salvo com sucesso: {}", produto);
        return construirRetorno(produto);
    }

    @Transactional
    public ProdutoResponseDTO editar(Long id, ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = findById(id);
        if (usuarioService.isEstoquista()) {
            BeanUtils.copyProperties(produtoRequestDTO, produto, "id", "valorCusto", "icms");
        }else {
            BeanUtils.copyProperties(produtoRequestDTO, produto, "id");
        }
        Produto produtoEditado = produtoRepository.save(produto);
        logger.info("Produto editado com sucesso: {}", produtoEditado);
        return construirRetorno(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        List<Produto> produtos = produtoRepository.findAll();
        logger.info("Listando todos os produtos. Total de produtos encontrados: {}", produtos.size());

        if (usuarioService.isEstoquista()) {
            ConfiguracaoCamposDTO configuracaoCamposDTO = configuracaoCamposService.obterConfiguracao();
            return produtos.stream().map(produto -> construirDTO(produto, configuracaoCamposDTO)).toList();
        }

        return produtos.stream().map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class)).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = findById(id);
        logger.info("Produto encontrado por ID {}: {}", id, produto);
        return construirRetorno(produto);
    }

    @Transactional
    public void deletar(Long id) {
        Produto produto = findById(id);
        produtoRepository.delete(produto);
        logger.info("Produto deletado com sucesso. ID: {}", id);
    }

    @Transactional
    public void inativar(Long id) {
        Produto produtoEncontrado = findById(id);
        produtoEncontrado.setAtivo(Boolean.FALSE);
        produtoRepository.save(produtoEncontrado);
        logger.info("Produto inativado com sucesso. ID: {}", id);
    }

    private Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(MENSAGEM_PRODUTO_NAO_ENCONTRADO + id);
                    return new NotFoundException(MENSAGEM_PRODUTO_NAO_ENCONTRADO + id);
                });
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

}
