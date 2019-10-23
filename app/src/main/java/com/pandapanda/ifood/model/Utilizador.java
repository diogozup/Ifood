package com.pandapanda.ifood.model;

import com.google.firebase.database.DatabaseReference;
import com.pandapanda.ifood.helper.ConfiguracaoFirebase;

public class Utilizador {
    private String idUtilizador;
    private String nome;
    private String endereco;


    //------------------------------------------------ construtor vazio
    public Utilizador() {
    }

    //------------------------------------------------ metodos
    public void gravar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference utilizadorRef = firebaseRef
                .child("utilizadores")
                .child( getIdUtilizador() ); // Aqui utilizamos este getIdUtlzdor para definir a key para o nó "utlzadores"
        utilizadorRef.setValue(this);

    }


    //------------------------------------------------ getrs and setrs

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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }








}
