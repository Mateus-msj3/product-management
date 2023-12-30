package io.github.msj.productmanagement.service.util;

import io.github.msj.productmanagement.exception.NotFoundException;
import io.github.msj.productmanagement.model.dto.ProdutoResponseDTO;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public class RelatorioProdutoUtil {

    public static final Map<String, String> CABECALHOS = Map.ofEntries(
            Map.entry("id", "ID"),
            Map.entry("nome", "NOME"),
            Map.entry("sku", "SKU"),
            Map.entry("ativo", "ATIVO"),
            Map.entry("categoria", "CATEGORIA"),
            Map.entry("valorCusto", "VALOR CUSTO"),
            Map.entry("icms", "ICMS"),
            Map.entry("valorVenda", "VALOR VENDA"),
            Map.entry("imagemProduto", "IMAGEM PRODUTO"),
            Map.entry("dataCadastro", "DATA CADASTRO"),
            Map.entry("quantidadeEstoque", "QUANTIDADE"),
            Map.entry("criadoPor", "CRIADO POR")
    );

    public static final Map<String, Integer> LARGURA_COLUNAS = Map.ofEntries(
            Map.entry("id", 4000),
            Map.entry("nome", 8000),
            Map.entry("sku", 4000),
            Map.entry("ativo", 4000),
            Map.entry("categoria", 4000),
            Map.entry("valorCusto", 4000),
            Map.entry("icms", 4000),
            Map.entry("valorVenda", 4000),
            Map.entry("imagemProduto", 9000),
            Map.entry("dataCadastro", 4000),
            Map.entry("quantidadeEstoque", 6000),
            Map.entry("criadoPor", 4000)
    );

    public static XSSFSheet criarPlanilha(XSSFWorkbook workbook, String[] campos) {
        XSSFSheet sheet = workbook.createSheet("Relatório de Produtos");
        for (int i = 0; i < campos.length; i++) {
            sheet.setColumnWidth(i, LARGURA_COLUNAS.getOrDefault(campos[i], 4000));
        }
        return sheet;
    }

    public static void criarCelula(XSSFRow row, int index, String value) {
        XSSFCell celula = row.createCell(index);
        celula.setCellValue(value);
    }

    public static void criarCabecalho(XSSFSheet sheet, String[] campos) {
        XSSFRow cabecalho = sheet.createRow(0);
        for (int i = 0; i < campos.length; i++) {
            String valorCabecalho = CABECALHOS.getOrDefault(campos[i], campos[i]);
            criarCelula(cabecalho, i, valorCabecalho);
        }
    }

    public static void criarLinha(XSSFSheet sheet, List<ProdutoResponseDTO> produtos, String[] campos) {
        int rowCounter = 1;

        for (ProdutoResponseDTO produto : produtos) {
            XSSFRow row = sheet.createRow(rowCounter);
            for (int cellCounter = 0; cellCounter < campos.length; cellCounter++) {
                String valor = obterValorCampo(produto, campos[cellCounter]);
                criarCelula(row, cellCounter, valor);
            }
            rowCounter++;
        }
    }

    public static String obterValorCampo(ProdutoResponseDTO produto, String campo) {
        switch (campo) {
            case "id":
                return produto.getId().toString();
            case "nome":
                return produto.getNome();
            case "sku":
                return produto.getSku();
            case "ativo":
                return produto.getAtivo().toString();
            case "categoria":
                return produto.getCategoria().getNome();
            case "valorCusto":
                return produto.getValorCusto().toString();
            case "icms":
                return produto.getIcms().toString();
            case "valorVenda":
                return produto.getValorVenda().toString();
            case "imagemProduto":
                return produto.getImagemProduto();
            case "dataCadastro":
                return produto.getDataCadastro().toString();
            case "quantidadeEstoque":
                return produto.getQuantidadeEstoque().toString();
            case "criadoPor":
                return produto.getCriadoPor();
            default:
                throw new NotFoundException("O campo: " + campo + " não foi encontrado");
        }
    }

    public static String criarCabecalhoCSV(String campo) {
        return CABECALHOS.getOrDefault(campo, campo.toUpperCase());
    }


}
