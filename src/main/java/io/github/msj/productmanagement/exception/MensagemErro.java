package io.github.msj.productmanagement.exception;

import java.util.Date;

public class MensagemErro {

    private int codigoStatus;

    private Date data;

    private String mensagem;

    private String descricao;

    public MensagemErro(int codigoStatus, Date data, String mensagem, String descricao) {
        this.codigoStatus = codigoStatus;
        this.data = data;
        this.mensagem = mensagem;
        this.descricao = descricao;
    }

    public int getCodigoStatus() {
        return codigoStatus;
    }

    public void setCodigoStatus(int codigoStatus) {
        this.codigoStatus = codigoStatus;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}