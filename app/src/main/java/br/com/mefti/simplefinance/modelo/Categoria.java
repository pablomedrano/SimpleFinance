package br.com.mefti.simplefinance.modelo;

/**
 * Created by a_med on 14/10/2016.
 */

public class Categoria {

    public String cod_categoria;
    public String nome;
    public String tp_lancamento;

    public Categoria (String cod_categoria, String nome, String tp_lancamento){
        this.cod_categoria = cod_categoria;
        this.nome = nome;
        this.tp_lancamento = tp_lancamento;
    }
}
