package com.pandapanda.ifood.model;

import com.google.firebase.database.DatabaseReference;
import com.pandapanda.ifood.helper.ConfiguracaoFirebase;

import java.io.Serializable;


public class Empresa implements Serializable {
    private String idUtilizador;
    private String urlImagem;
    private String nome;
    private String tempo;
    private String categoria;
    private Double precoEntrega;

    private String nome_filtro;


    public Empresa(){
    }

    public void gravar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference empresaRef = firebaseRef.child("empresas").child(getIdUtilizador() );
        empresaRef.setValue(this);

    }


    public String getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(String idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {

        this.nome = nome;
        this.nome_filtro = nome.toLowerCase();
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getPrecoEntrega() {
        return precoEntrega;
    }

    public void setPrecoEntrega(Double precoEntrega) {
        this.precoEntrega = precoEntrega;
    }
}
