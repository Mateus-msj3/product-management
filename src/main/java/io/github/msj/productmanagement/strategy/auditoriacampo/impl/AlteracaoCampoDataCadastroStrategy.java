package io.github.msj.productmanagement.strategy.auditoriacampo.impl;

import io.github.msj.productmanagement.model.dto.AlteracaoCampoDTO;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.strategy.auditoriacampo.AuditoriaCampoStrategy;

import java.util.Objects;

public class AlteracaoCampoDataCadastroStrategy implements AuditoriaCampoStrategy {
    @Override
    public boolean verificaAlteracao(Produto produtoAtual, Produto produtoAnterior) {
        return !Objects.equals(produtoAtual.getDataCadastro(), produtoAnterior.getDataCadastro());
    }

    @Override
    public AlteracaoCampoDTO obterAlteracao(Produto produtoAtual, Produto produtoAnterior) {
        return new AlteracaoCampoDTO("Data Cadastro", produtoAnterior.getDataCadastro().toString(), produtoAtual.getDataCadastro().toString());
    }
}
