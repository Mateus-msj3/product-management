package io.github.msj.productmanagement.strategy.auditoriacampo.impl;

import io.github.msj.productmanagement.model.dto.AlteracaoCampoDTO;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.strategy.auditoriacampo.AuditoriaCampoStrategy;

import java.util.Objects;

public class AlteracaoCampoCategoriaStrategy implements AuditoriaCampoStrategy {
    @Override
    public boolean verificaAlteracao(Produto produtoAtual, Produto produtoAnterior) {
        return !Objects.equals(produtoAtual.getCategoria(), produtoAnterior.getCategoria());
    }

    @Override
    public AlteracaoCampoDTO obterAlteracao(Produto produtoAtual, Produto produtoAnterior) {
        return new AlteracaoCampoDTO("Categoria", produtoAnterior.getCategoria().getNome(), produtoAtual.getCategoria().getNome());
    }
}
