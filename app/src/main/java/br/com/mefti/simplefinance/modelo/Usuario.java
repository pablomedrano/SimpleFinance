package br.com.mefti.simplefinance.modelo;

/**
 * Created by a_med on 14/10/2016.
 */

public class Usuario {
    public String cod_usuario;
    public String nome;
    public String senha;
    public String email;
    public String estado;

    public Usuario(String cod_usuario, String nome, String senha, String email, String estado){
        this.cod_usuario = cod_usuario;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.estado = estado;
    }
}
