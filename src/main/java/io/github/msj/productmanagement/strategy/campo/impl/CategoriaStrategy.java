package io.github.msj.productmanagement.strategy.campo.impl;

import io.github.msj.productmanagement.model.dto.CategoriaDTO;
import io.github.msj.productmanagement.model.dto.ProdutoResponseDTO;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.strategy.campo.CampoStrategy;

public class CategoriaStrategy implements CampoStrategy {
    @Override
    public void atualizarCampo(ProdutoResponseDTO dto, Produto produto) {
        dto.setCategoria(new CategoriaDTO(produto.getCategoria().getId(), produto.getCategoria().getNome(),
                produto.getCategoria().getAtivo(), produto.getCategoria().getTipo()));
    }
}
