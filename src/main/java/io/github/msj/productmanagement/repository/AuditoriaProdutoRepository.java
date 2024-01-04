package io.github.msj.productmanagement.repository;

import io.github.msj.productmanagement.model.entities.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuditoriaProdutoRepository extends JpaRepository<Auditoria, Long> {

    @Query(value = "SELECT revtype FROM produto_aud WHERE rev = :id", nativeQuery = true)
    Optional<Integer> buscarTipoOperacao(Long id);
}