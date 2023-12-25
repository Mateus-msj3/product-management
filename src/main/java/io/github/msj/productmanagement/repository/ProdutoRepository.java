package io.github.msj.productmanagement.repository;

import io.github.msj.productmanagement.model.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto>, RevisionRepository<Produto, Long, Integer> {
}