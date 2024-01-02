package io.github.msj.productmanagement.strategy.auditoriacampo.impl;

import io.github.msj.productmanagement.model.dto.AlteracaoCampoDTO;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.strategy.auditoriacampo.AuditoriaCampoStrategy;

import java.util.Objects;

public class AlteracaoCampoNomeStrategy implements AuditoriaCampoStrategy {
    @Override
    public boolean verificaAlteracao(Produto produtoAtual, Produto produtoAnterior) {
        return !Objects.equals(produtoAtual.getNome(), produtoAnterior.getNome());
    }

    @Override
    public AlteracaoCampoDTO obterAlteracao(Produto produtoAtual, Produto produtoAnterior) {
        return new AlteracaoCampoDTO("Nome", produtoAnterior.getNome(), produtoAtual.getNome());
    }
}
