package io.github.msj.productmanagement.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produto")
@Audited
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(nullable = false, unique = true)
    private String sku;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "valor_custo", nullable = false)
    private BigDecimal valorCusto;

    @Column(nullable = false)
    private BigDecimal icms;

    @Column(name = "valor_venda", nullable = false)
    private BigDecimal valorVenda;

    @Column(name = "imagem_url")
    private String imagemProduto;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;
}

