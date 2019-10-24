package com.pandapanda.ifood.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.pandapanda.ifood.R;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);


        //inicializarComponentes
        inicializarComponentes();


        //Configuracao toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pedidos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);








    }


    private void inicializarComponentes(){
        recyclerPedidos = findViewById(R.id.recyclerPedidos);
    }


}
