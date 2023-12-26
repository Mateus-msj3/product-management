package io.github.msj.productmanagement.strategy.campo;

import io.github.msj.productmanagement.model.dto.ProdutoResponseDTO;
import io.github.msj.productmanagement.model.entities.Produto;

public interface CampoStrategy {

    void atualizarCampo(ProdutoResponseDTO dto, Produto produto);
}
