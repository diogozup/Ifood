package com.pandapanda.ifood.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pandapanda.ifood.R;
import com.pandapanda.ifood.adapter.AdapterProduto;
import com.pandapanda.ifood.helper.ConfiguracaoFirebase;
import com.pandapanda.ifood.helper.UtilizadorFirebase;
import com.pandapanda.ifood.model.Empresa;
import com.pandapanda.ifood.model.Produto;
import com.pandapanda.ifood.model.Utilizador;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MenuActivity extends AppCompatActivity {


    private RecyclerView recyclerProdutosMenu;
    private ImageView imageEmpresaMenu;
    private TextView textNomeEmpresaMenu;
    private Empresa empresaSelecionada;

    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private String idEmpresaSelecionada;

    private AlertDialog dialog;
    private String idUtilizadorLogado;
    private Utilizador utilizador;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        //-- Configuracoes Iniciais
        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUtilizadorLogado = UtilizadorFirebase.getIdUtilizador();


        //GET empresa selecionada
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            empresaSelecionada = (Empresa) bundle.getSerializable("empresa"); //mesma key que definimos na HomeActivity

            textNomeEmpresaMenu.setText(empresaSelecionada.getNome());


            String url = empresaSelecionada.getUrlImagem();
            Picasso.get().load(url).into(imageEmpresaMenu);

            idEmpresaSelecionada = empresaSelecionada.getIdUtilizador();


        }


        //-- Configuracao toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*-- Configura recyclerview --*/
        recyclerProdutosMenu.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutosMenu.setHasFixedSize(true);

        adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutosMenu.setAdapter(adapterProduto);

        // RECUPERA DADOS DOS PRODUTOS DA EMPRESA
        recuperarProdutos();
        recuperarDadosUtilizador();





    }


    private void inicializarComponentes(){
        recyclerProdutosMenu = findViewById(R.id.recyclerProdutoMenu);
        imageEmpresaMenu = findViewById(R.id.imageEmpresaMenu);
        textNomeEmpresaMenu = findViewById(R.id.textNomeEmpresaMenu);
    }


    private void recuperarPedido(){
        dialog.dismiss();

    }


    private void recuperarProdutos(){
        DatabaseReference produtosRef = firebaseRef
                .child("produtos")
                .child(idEmpresaSelecionada);


        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtos.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    produtos.add(ds.getValue(Produto.class) );
                }
                adapterProduto.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void recuperarDadosUtilizador(){
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados")
                .setCancelable(false)
                .build();
        dialog.show();

        DatabaseReference utilizadoresRef = firebaseRef
                .child("utilizadores")
                .child(idUtilizadorLogado); // aqui fui criar novo private String idUtlzdorLogado e depois inicializei em //Configuracoes Iniciais

        utilizadoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.getValue() != null){
                    utilizador = dataSnapshot.getValue(Utilizador.class);
                }
                recuperarPedido();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu , menu );
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuPedido:
                //metodo confirmar pedido

                break;
        }

        return super.onOptionsItemSelected(item);
    }































}
