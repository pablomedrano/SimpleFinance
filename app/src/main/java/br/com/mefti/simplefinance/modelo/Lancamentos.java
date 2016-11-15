package br.com.mefti.simplefinance.modelo;

import java.util.Date;


/**
 * Created by a_med on 14/10/2016.
 */

public class Lancamentos {
    public String cod_lancamento;
    public String cod_usuario;
    public String cod_categoria;
    public String tp_lancamento;
    public String descricao;
    public double valor;
    public long data;
    public String repetir;
    public long previsao_data;
    public double previsao_valor;
    public  String observacao;

    /*
    public Lancamentos(String cod_lancamento, String cod_usuario, String cod_categoria, String tp_lancamento, String descricao,
                       double valor, Date data, String repetir, Date previsao_data, double previsao_valor, String observacao){
        this.cod_lancamento = cod_lancamento;
        this.cod_usuario = cod_usuario;
        this.cod_categoria = cod_categoria;
        this.tp_lancamento = tp_lancamento;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.repetir = repetir;
        this.previsao_data = previsao_data;
        this.previsao_valor = previsao_valor;
        this.observacao = observacao;
    }
*/
    public String getCod_categoria() {
        return cod_categoria;
    }

    public String getCod_lancamento() {
        return cod_lancamento;
    }

    public String getCod_usuario() {
        return cod_usuario;
    }

    public long getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getObservacao() {
        return observacao;
    }

    public long getPrevisao_data() {
        return previsao_data;
    }

    public double getPrevisao_valor() {
        return previsao_valor;
    }

    public String getRepetir() {
        return repetir;
    }

    public String getTp_lancamento() {
        return tp_lancamento;
    }

    public double getValor() {
        return valor;
    }

    public void setCod_categoria(String cod_categoria) {
        this.cod_categoria = cod_categoria;
    }

    public void setCod_lancamento(String cod_lancamento) {
        this.cod_lancamento = cod_lancamento;
    }

    public void setCod_usuario(String cod_usuario) {
        this.cod_usuario = cod_usuario;
    }

    public void setData(long data) {
        this.data = data;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setPrevisao_data(long previsao_data) {
        this.previsao_data = previsao_data;
    }

    public void setPrevisao_valor(double previsao_valor) {
        this.previsao_valor = previsao_valor;
    }

    public void setRepetir(String repetir) {
        this.repetir = repetir;
    }

    public void setTp_lancamento(String tp_lancamento) {
        this.tp_lancamento = tp_lancamento;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
