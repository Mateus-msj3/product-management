package io.github.msj.productmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoFiltroDTO implements Serializable {

    private Long id;

    private String nome;

    private Boolean ativo;

    private LocalDate dataCadastroDe;

    private LocalDate dataCadastroAte;

    private String sku;

    private Long idCategoria;

    private BigDecimal valorCusto;

    private BigDecimal icms;

    private BigDecimal valorVenda;

    private String imagemProduto;

    private Integer quantidadeEstoque;

    private String criadoPor;

}