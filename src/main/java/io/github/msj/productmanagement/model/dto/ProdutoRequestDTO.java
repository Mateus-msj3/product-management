package io.github.msj.productmanagement.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdutoRequestDTO implements Serializable {

    @NotEmpty(message = "É necessário informar o nome")
    private String nome;

    private Boolean ativo;

    @NotNull(message = "É necessário informar o sku")
    private String sku;

    @NotNull(message = "É necessário informar a categoria")
    private CategoriaDTO categoria;

    @NotNull(message = "O valor de custo deve ser informado")
    @Min(message = "O valor minimo deve ser maior que 0", value = 1)
    @Positive(message = "O valor de custo não poder ser um número negativo")
    private BigDecimal valorCusto;

    @NotNull(message = "O icms deve ser informado")
    @Positive(message = "Não é possivel o icms ser um número negativo")
    private BigDecimal icms;

    @NotNull(message = "É necessário informar o valor de venda")
    private BigDecimal valorVenda;

    private String imagemProduto;

    @NotNull(message = "Informe a quantidade em estoque")
    @Min(message = "O valor minimo deve ser maior que 0", value = 1)
    private Integer quantidadeEstoque;

}