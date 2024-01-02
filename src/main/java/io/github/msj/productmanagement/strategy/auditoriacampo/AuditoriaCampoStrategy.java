package io.github.msj.productmanagement.strategy.auditoriacampo;

import io.github.msj.productmanagement.model.dto.AlteracaoCampoDTO;
import io.github.msj.productmanagement.model.entities.Produto;

public interface AuditoriaCampoStrategy {
    boolean verificaAlteracao(Produto produtoAtual, Produto produtoAnterior);

    AlteracaoCampoDTO obterAlteracao(Produto produtoAtual, Produto produtoAnterior);
}
