package com.pandapanda.ifood.model;

import com.google.firebase.database.DatabaseReference;
import com.pandapanda.ifood.helper.ConfiguracaoFirebase;

public class Produto {
    private String idUtilizador;
    private String nome;
    private String descricao;
    private Double preco;

    private String idProduto;


    public Produto() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("produtos");
        setIdProduto(produtoRef.push().getKey());
    }


    public void gravarProduto(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("produtos")
                .child(getIdUtilizador() )
                .child(getIdProduto()); //apaguei aqui o ".push()"
        produtoRef.setValue(this);


    }

    public void removerProduto(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("produtos")
                .child(getIdUtilizador() )
                .child(getIdProduto());
        produtoRef.removeValue();
    }


    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(String idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
