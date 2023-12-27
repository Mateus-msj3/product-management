package io.github.msj.productmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoAgregadoDTO {

    private String nome;

    private BigDecimal custo;

    private BigDecimal custoTotal;

    private Integer quantidade;

    private BigDecimal valorTotalPrevisto;

}
