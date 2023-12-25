package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.exception.NotFoundException;
import io.github.msj.productmanagement.model.dto.CategoriaDTO;
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


@Service
@AllArgsConstructor
public class ProdutoService {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);
    private static final String MENSAGEM_PRODUTO_NAO_ENCONTRADO = "Não foi encontrado produto com ID: ";

    private final ProdutoRepository produtoRepository;

    private final CategoriaRepository categoriaRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO produtoRequestDTO) {
        Categoria categoria = categoriaRepository.findById(produtoRequestDTO.getCategoria().getId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada com ID: "
                        + produtoRequestDTO.getCategoria().getId()));

        produtoRequestDTO.setCategoria(modelMapper.map(categoria, CategoriaDTO.class));
        produtoRequestDTO.setDataCadastro(LocalDate.now());
        Produto produto = produtoRepository.save(modelMapper.map(produtoRequestDTO, Produto.class));
        logger.info("Produto salvo com sucesso: {}", produto);
        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    @Transactional
    public ProdutoResponseDTO editar(Long id, ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = findById(id);
        BeanUtils.copyProperties(produtoRequestDTO, produto, "id");
        Produto produtoEditado = produtoRepository.save(produto);
        logger.info("Produto editado com sucesso: {}", produtoEditado);
        return modelMapper.map(produtoEditado, ProdutoResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        List<Produto> produtos = produtoRepository.findAll();
        logger.info("Listando todos os produtos. Total de produtos encontrados: {}", produtos.size());
        return produtos.stream().map((Produto produto) -> modelMapper.map(produto, ProdutoResponseDTO.class)).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = findById(id);
        logger.info("Produto encontrado por ID {}: {}", id, produto);
        return modelMapper.map(produto, ProdutoResponseDTO.class);
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

}

