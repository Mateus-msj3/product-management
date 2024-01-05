package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.model.dto.AuditoriaResponseDTO;
import io.github.msj.productmanagement.model.dto.DetalheAuditoriaProdutoResponseDTO;
import io.github.msj.productmanagement.model.entities.Auditoria;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.model.enums.TipoOperacao;
import io.github.msj.productmanagement.repository.AuditoriaProdutoRepository;
import io.github.msj.productmanagement.repository.ProdutoRepository;
import io.github.msj.productmanagement.strategy.auditoriacampo.AuditoriaCampoStrategy;
import io.github.msj.productmanagement.strategy.auditoriacampo.impl.*;
import lombok.AllArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuditoriaProdutoService {

    private final AuditReader auditReader;

    private final AuditoriaProdutoRepository auditoriaProdutoRepository;

    private final ProdutoRepository produtoRepository;

    private final ProdutoService produtoService;

    public List<AuditoriaResponseDTO> obterAuditorias(Long id) {
        AuditQuery auditQuery = auditReader.createQuery()
                .forRevisionsOfEntity(Produto.class, true);
        auditQuery.add(AuditEntity.id().eq(id));

        List<Auditoria> results = auditQuery.getResultList();

        List<AuditoriaResponseDTO> dtos = new ArrayList<>();
        for (Auditoria revInfo : results) {

            AuditoriaResponseDTO dto = new AuditoriaResponseDTO();
            dto.setId(Long.valueOf(revInfo.getId()));
            dto.setData(revInfo.getRevisionDate());
            dto.setOperacaoRealizada(obterTipoOperacaoAuditoria(dto.getId()));
            dto.setUsuario(revInfo.getUsuario());
            dto.setProduto(produtoService.buscarPorId(id));
            dtos.add(dto);
        }
        return dtos;
    }

    public DetalheAuditoriaProdutoResponseDTO detalharMudancas(Long id) {
        List<Revision<Integer, Produto>> revisions = produtoRepository.findRevisions(id).getContent();
        DetalheAuditoriaProdutoResponseDTO detalheAuditoria = new DetalheAuditoriaProdutoResponseDTO();

        for (int i = 1; i < revisions.size(); i++) {
            Revision<Integer, Produto> revisionAtual = revisions.get(i);
            Revision<Integer, Produto> revisionAnterior = revisions.get(i - 1);

            Produto produtoAtual = revisionAtual.getEntity();
            Produto produtoAnterior = revisionAnterior.getEntity();

            detalheAuditoria = obterDetalhesCamposModificados(revisionAtual.getRevisionNumber().orElse(null),
                    produtoAtual,
                    produtoAnterior);
        }
        return detalheAuditoria;
    }

    private DetalheAuditoriaProdutoResponseDTO obterDetalhesCamposModificados(Integer idAuditoria, Produto produtoAtual,
                                                                              Produto produtoAnterior) {
        DetalheAuditoriaProdutoResponseDTO detalheAuditoria = new DetalheAuditoriaProdutoResponseDTO();
        detalheAuditoria.setIdAuditoria(idAuditoria);
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

    private String obterTipoOperacaoAuditoria(Long id) {
        Optional<Integer> tipoOperacaoOptional = auditoriaProdutoRepository.buscarTipoOperacao(id);
        return tipoOperacaoOptional
                .map(operacao -> TipoOperacao.obterOperacao(operacao).getDescricao()).orElse(null);
    }


}