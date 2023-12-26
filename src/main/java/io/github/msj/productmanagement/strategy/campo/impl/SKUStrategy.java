package io.github.msj.productmanagement.strategy.campo.impl;

import io.github.msj.productmanagement.model.dto.ProdutoResponseDTO;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.strategy.campo.CampoStrategy;

public class SKUStrategy implements CampoStrategy {
    @Override
    public void atualizarCampo(ProdutoResponseDTO dto, Produto produto) {
        dto.setSku(produto.getSku());
    }
}
