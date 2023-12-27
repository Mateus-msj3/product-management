package io.github.msj.productmanagement.repository.specifications;


import io.github.msj.productmanagement.model.dto.ProdutoFiltroDTO;
import io.github.msj.productmanagement.model.entities.Produto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProdutoSpecification {

    public static Specification<Produto> combinarFiltros(ProdutoFiltroDTO produtoFiltroDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (produtoFiltroDTO.getNome() != null && !produtoFiltroDTO.getNome().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")),
                        "%" + produtoFiltroDTO.getNome().toLowerCase() + "%"));
            }

            if (produtoFiltroDTO.getAtivo() != null && !produtoFiltroDTO.getAtivo()) {
                predicates.add(criteriaBuilder.equal(root.get("ativo"), produtoFiltroDTO.getAtivo()));
            }

            if (produtoFiltroDTO.getSku() != null && !produtoFiltroDTO.getSku().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("sku"), produtoFiltroDTO.getSku()));
            }

            if (produtoFiltroDTO.getDataCadastroDe() != null && produtoFiltroDTO.getDataCadastroAte() != null) {
                predicates.add(criteriaBuilder.between(root.get("dataCadastro"),
                        produtoFiltroDTO.getDataCadastroDe(), produtoFiltroDTO.getDataCadastroAte()));
            }

            if (produtoFiltroDTO.getCategoria() != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoria").get("id"),
                        produtoFiltroDTO.getCategoria().getId()));
            }

            if (produtoFiltroDTO.getValorCusto() != null) {
                predicates.add(criteriaBuilder.equal(root.get("valorCusto"), produtoFiltroDTO.getValorCusto()));
            }

            if (produtoFiltroDTO.getIcms() != null) {
                predicates.add(criteriaBuilder.equal(root.get("icms"), produtoFiltroDTO.getIcms()));
            }

            if (produtoFiltroDTO.getValorVenda() != null) {
                predicates.add(criteriaBuilder.equal(root.get("valorVenda"), produtoFiltroDTO.getValorVenda()));
            }

            if (produtoFiltroDTO.getImagemProduto() != null && !produtoFiltroDTO.getImagemProduto().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("imagemProduto")),
                        "%" + produtoFiltroDTO.getImagemProduto().toLowerCase() + "%"));
            }

            if (produtoFiltroDTO.getQuantidadeEstoque() != null) {
                predicates.add(criteriaBuilder.equal(root.get("quantidadeEstoque"),
                        produtoFiltroDTO.getQuantidadeEstoque()));
            }

            if (produtoFiltroDTO.getCriadoPor() != null && !produtoFiltroDTO.getCriadoPor().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("criadoPor")),
                        "%" + produtoFiltroDTO.getCriadoPor().toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
