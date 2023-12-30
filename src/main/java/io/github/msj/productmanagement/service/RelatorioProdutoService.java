package io.github.msj.productmanagement.service;

import io.github.msj.productmanagement.model.dto.ConfiguracaoCamposDTO;
import io.github.msj.productmanagement.model.dto.ProdutoFiltroDTO;
import io.github.msj.productmanagement.model.dto.ProdutoResponseDTO;
import io.github.msj.productmanagement.model.entities.Produto;
import io.github.msj.productmanagement.repository.ProdutoRepository;
import io.github.msj.productmanagement.repository.specifications.ProdutoSpecification;
import io.github.msj.productmanagement.service.util.ProdutoUtil;
import io.github.msj.productmanagement.service.util.RelatorioProdutoUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class RelatorioProdutoService {

    private final ProdutoRepository produtoRepository;

    public void gerarRelatorio(HttpServletResponse httpServletResponse, String tipoRelatorio,
                               ProdutoFiltroDTO produtoFiltroDTO, String[] campos) {
        if (!"csv".equalsIgnoreCase(tipoRelatorio) && !"xlsx".equalsIgnoreCase(tipoRelatorio)) {
            throw new ServiceException("Tipo de relatório inválido");
        }

        ConfiguracaoCamposDTO configuracaoCamposDTO = new ConfiguracaoCamposDTO(null, Arrays.asList(campos));
        List<ProdutoResponseDTO> produtos = obterProdutos(produtoFiltroDTO, configuracaoCamposDTO);

        if ("csv".equalsIgnoreCase(tipoRelatorio)) {
            gerarRelatorioCSV(httpServletResponse, produtos, campos);
        } else {
            gerarRelatorioXLSX(httpServletResponse, produtos, campos);
        }
    }

    private void gerarRelatorioXLSX(HttpServletResponse httpServletResponse, List<ProdutoResponseDTO> produtos,
                                    String[] campos) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = RelatorioProdutoUtil.criarPlanilha(workbook, campos);
            RelatorioProdutoUtil.criarCabecalho(sheet, campos);
            RelatorioProdutoUtil.criarLinha(sheet, produtos, campos);

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=report.xlsx";
            httpServletResponse.setHeader(headerKey, headerValue);
            httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            ServletOutputStream out = httpServletResponse.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (IOException e) {
            throw new ServiceException("Ocorreu um erro ao gerar o relatório", e);
        }
    }

    private void gerarRelatorioCSV(HttpServletResponse httpServletResponse, List<ProdutoResponseDTO> produtos, String[] campos) {
        try {
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=report.csv";
            httpServletResponse.setHeader(headerKey, headerValue);
            httpServletResponse.setContentType("text/csv");

            try (CSVPrinter csvPrinter = new CSVPrinter(httpServletResponse.getWriter(), CSVFormat.DEFAULT)) {

                String[] cabecalho = new String[campos.length];
                for (int i = 0; i < campos.length; i++) {
                    cabecalho[i] = RelatorioProdutoUtil.criarCabecalhoCSV(campos[i]);
                }
                csvPrinter.printRecord((Object[]) cabecalho);

                for (ProdutoResponseDTO produto : produtos) {
                    List<String> valores = new ArrayList<>();
                    for (String campo : campos) {
                        valores.add(RelatorioProdutoUtil.obterValorCampo(produto, campo));
                    }
                    csvPrinter.printRecord(valores);
                }
            }

        } catch (IOException e) {
            throw new ServiceException("Ocorreu um erro ao gerar o relatório", e);
        }
    }


    private List<ProdutoResponseDTO> obterProdutos(ProdutoFiltroDTO produtoFiltroDTO, ConfiguracaoCamposDTO configuracaoCamposDTO) {
        Specification<Produto> produtosSpecification = ProdutoSpecification.combinarFiltros(produtoFiltroDTO);
        return produtoRepository.findAll(produtosSpecification)
                .stream().map(produto -> ProdutoUtil.construirCamposRelatorio(produto, configuracaoCamposDTO)).toList();
    }
}
