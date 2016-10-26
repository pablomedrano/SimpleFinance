package br.com.mefti.simplefinance.modelo;

/**
 * Created by a_med on 14/10/2016.
 */

public class Usuarios {
    public String cod_usuario;
    public String nome;
    public String senha;
    public String email;
    public String estado;
    /*
    public Usuarios(String cod_usuario, String nome, String senha, String email, String estado){
        this.cod_usuario = cod_usuario;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.estado = estado;
    }
    */


    public void setEmail(String email) {
        this.email = email;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public String getEstado() {
        return estado;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }
}
