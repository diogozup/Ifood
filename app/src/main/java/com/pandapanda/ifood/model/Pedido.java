package com.pandapanda.ifood.model;

import com.google.firebase.database.DatabaseReference;
import com.pandapanda.ifood.helper.ConfiguracaoFirebase;

import java.util.HashMap;
import java.util.List;

public class Pedido {
    private String idUtilizador;
    private String idEmpresa;
    private String idPedido;
    private String nome;
    private String endereco;
    private List<ItemPedido> itens;
    private Double total;
    private String status = "pendente";
    private int metodoPagamento;
    private String observacao;


    public Pedido(String idUti, String idEmp){
        setIdEmpresa(idEmp);
        setIdUtilizador(idUti);

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_utilizador")
                .child(idEmp)
                .child(idUti);
        setIdPedido( pedidoRef.push().getKey()) ;
    }



    public String getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(String idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
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

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(int metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }


    public void gravar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_utilizador")
                .child(getIdEmpresa())
                .child(getIdUtilizador());

        pedidoRef.setValue(this);
    }

    public void confirmar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos")
                .child(getIdEmpresa())
                .child(getIdPedido());

        pedidoRef.setValue(this);
    }

    public void remover(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_utilizador")
                .child(getIdEmpresa())
                .child(getIdUtilizador());

        pedidoRef.removeValue();
    }

    public void atualizarStatus(){
        //instanciamos um HashMap para mudar apenas 1 parametro child de pedido
        HashMap<String,Object> status = new HashMap<>();
        status.put("status", getStatus());


        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos")
                .child(getIdEmpresa() )
                .child(getIdPedido()  );
        pedidoRef.updateChildren( status );
    }


}
