package io.github.msj.productmanagement.repository;

import io.github.msj.productmanagement.model.entities.ConfiguracaoCampos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracaoCamposRepository extends JpaRepository<ConfiguracaoCampos, Long> {

    ConfiguracaoCampos findTopByOrderByIdDesc();
}