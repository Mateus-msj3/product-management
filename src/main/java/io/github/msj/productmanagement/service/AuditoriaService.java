package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.model.dto.DetalheAuditoriaResponseDTO;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.repository.AuditoriaRepository;
import io.github.msj.productmanagement.repository.ProdutoRepository;
import io.github.msj.productmanagement.strategy.auditoriacampo.AuditoriaCampoStrategy;
import io.github.msj.productmanagement.strategy.auditoriacampo.impl.*;
import lombok.AllArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class AuditoriaService {

    private final AuditReader auditReader;

    private final AuditoriaRepository auditoriaRepository;

    private final ProdutoRepository produtoRepository;

    public List<Produto> getRevisions(Long id, boolean fetchChanges) {
        AuditQuery auditQuery = null;

        if(fetchChanges) {
            auditQuery = auditReader.createQuery()
                    .forRevisionsOfEntityWithChanges(Produto.class, true);
        }
        else {
            auditQuery = auditReader.createQuery()
                    .forRevisionsOfEntity(Produto.class, true);
        }
        auditQuery.add(AuditEntity.id().eq(id));
        return auditQuery.getResultList();
    }

    public List<Produto> obterAuditoriaProduto(Long id) {
        return produtoRepository.findRevisions(id)
                .stream()
                .map(Revision::getEntity)
                .toList();
    }

    public DetalheAuditoriaResponseDTO detalharMudancas(Long id) {
        List<Revision<Integer, Produto>> revisions = produtoRepository.findRevisions(id).getContent();
        DetalheAuditoriaResponseDTO detalheAuditoria = new DetalheAuditoriaResponseDTO();

        for (int i = 1; i < revisions.size(); i++) {
            Revision<Integer, Produto> revisionAtual = revisions.get(i);
            Revision<Integer, Produto> revisionAnterior = revisions.get(i - 1);

            Produto produtoAtual = revisionAtual.getEntity();
            Produto produtoAnterior = revisionAnterior.getEntity();
            detalheAuditoria = obterDetalhes(produtoAtual, produtoAnterior);
        }

        return detalheAuditoria;
    }

    private DetalheAuditoriaResponseDTO obterDetalhes(Produto produtoAtual, Produto produtoAnterior) {
        DetalheAuditoriaResponseDTO detalheAuditoria = new DetalheAuditoriaResponseDTO();
        List<AuditoriaCampoStrategy> strategies = Arrays.asList(
                new AlteracaoCampoNomeStrategy(),
                new AlteracaoCampoAtivoStrategy(),
                new AlteracaoCampoCategoriaStrategy(),
                new AlteracaoCampoSkuStrategy(),
                new AlteracaoCampoIcmsVendaStrategy(),
                new AlteracaoCampoValorCustoStrategy(),
                new AlteracaoCampoValorVendaStrategy(),
                new AlteracaoCampoImagemProdutoVendaStrategy(),
                new AlteracaoCampoDataCadastroStrategy(),
                new AlteracaoCampoQuantidadeEstoqueStrategy()
        );

        for (AuditoriaCampoStrategy strategy : strategies) {
            if (strategy.verificaAlteracao(produtoAtual, produtoAnterior)) {
                detalheAuditoria.getCamposAlterados().add(strategy.obterAlteracao(produtoAtual, produtoAnterior));
            }
        }

        return detalheAuditoria;
    }


}