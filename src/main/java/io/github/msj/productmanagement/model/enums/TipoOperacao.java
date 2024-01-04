package io.github.msj.productmanagement.model.enums;

public enum TipoOperacao {

    INCLUSAO(0, "Inclusão"),
    ATUALIZACAO(1, "Atualização"),
    DELECAO(2, "Deleção");

    private final int valor;
    private final String descricao;

    TipoOperacao(int valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public int getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    // Método para obter o enum com base no valor inteiro
    public static TipoOperacao obterOperacao(int valor) {
        for (TipoOperacao tipoOperacao : TipoOperacao.values()) {
            if (tipoOperacao.valor == valor) {
                return tipoOperacao;
            }
        }
        throw new IllegalArgumentException("Valor inválido: " + valor);
    }
}


