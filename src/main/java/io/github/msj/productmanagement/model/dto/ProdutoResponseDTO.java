package io.github.msj.productmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdutoResponseDTO implements Serializable {

    private Long id;

    private String nome;

    private Boolean ativo;

    private String sku;

    private CategoriaDTO categoria;

    private BigDecimal valorCusto;

    private BigDecimal icms;

    private BigDecimal valorVenda;

    private String imagemProduto;

    private LocalDate dataCadastro;

    private Integer quantidadeEstoque;

    private String criadoPor;
}