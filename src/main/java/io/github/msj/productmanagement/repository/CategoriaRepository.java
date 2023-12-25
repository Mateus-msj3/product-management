package io.github.msj.productmanagement.repository;

import io.github.msj.productmanagement.model.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}