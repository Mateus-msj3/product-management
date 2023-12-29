package io.github.msj.productmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoAgregadoPageDTO {

    private List<ProdutoAgregadoDTO> produtos;

    private long totalElementos;

    private int totalPaginas;

    private int paginaAtual;

}
