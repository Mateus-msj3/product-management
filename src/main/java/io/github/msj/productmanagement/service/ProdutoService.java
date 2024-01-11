package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.exception.NotFoundException;
import io.github.msj.productmanagement.model.dto.ConfiguracaoCamposDTO;
import io.github.msj.productmanagement.model.dto.ProdutoRequestDTO;
import io.github.msj.productmanagement.model.dto.ProdutoResponseDTO;
import io.github.msj.productmanagement.model.entities.Categoria;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.repository.CategoriaRepository;
import io.github.msj.productmanagement.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static io.github.msj.productmanagement.service.util.ProdutoUtil.construirDTO;


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
        Categoria categoria = buscarCategoria(produtoRequestDTO.getCategoria().getId());
        Produto produto = modelMapper.map(produtoRequestDTO, Produto.class);
        produto.setCriadoPor(usuarioService.obterUsuarioLogado().getName());
        produto.setCategoria(categoria);
        produto.setDataCadastro(LocalDate.now());
        Produto produtoSalvo = produtoRepository.save(produto);
        logger.info("Produto salvo com sucesso: {}", produtoSalvo);
        return construirRetorno(produtoSalvo);
    }

    @Transactional
    public ProdutoResponseDTO editar(Long id, ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = findById(id);
        if (usuarioService.isEstoquista()) {
            BeanUtils.copyProperties(produtoRequestDTO, produto,"valorCusto", "icms");
        }else {
            BeanUtils.copyProperties(produtoRequestDTO, produto);
        }
        if (!produto.getCategoria().getId().equals(produtoRequestDTO.getCategoria().getId())) {
            produto.setCategoria(buscarCategoria(produtoRequestDTO.getCategoria().getId()));
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

    private Categoria buscarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada com ID: " + id));
        return categoria;
    }

    private Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(MENSAGEM_PRODUTO_NAO_ENCONTRADO + id);
                    return new NotFoundException(MENSAGEM_PRODUTO_NAO_ENCONTRADO + id);
                });
    }

    public ProdutoResponseDTO construirRetorno(Produto produto) {
        if (usuarioService.isEstoquista()) {
            ConfiguracaoCamposDTO configuracaoCamposDTO = configuracaoCamposService.obterConfiguracao();
            return construirDTO(produto, configuracaoCamposDTO);
        }
        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

}

