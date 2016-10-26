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
    public Date data;
    public String repetir;
    public Date previsao_data;
    public double previsao_valor;
    public  String observacao;

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
}
