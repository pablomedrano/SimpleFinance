package br.com.mefti.simplefinance.modelo;

/**
 * Created by a_med on 14/10/2016.
 */

public class Categorias {

    public String cod_categoria;
    public String cod_usuario;
    public String nome;
    public String tp_lancamento;

    public Categorias(String cod_categoria, String cod_usuario, String nome, String tp_lancamento){
        this.cod_categoria = cod_categoria;
        this.cod_usuario = cod_usuario;
        this.nome = nome;
        this.tp_lancamento = tp_lancamento;
    }
}
