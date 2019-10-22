package com.pandapanda.ifood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pandapanda.ifood.R;
import com.pandapanda.ifood.adapter.AdapterProduto;
import com.pandapanda.ifood.helper.ConfiguracaoFirebase;
import com.pandapanda.ifood.helper.UtilizadorFirebase;
import com.pandapanda.ifood.listener.RecyclerItemClickListener;
import com.pandapanda.ifood.model.Produto;

import java.util.ArrayList;
import java.util.List;


public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    // recycle view
    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    //recuperar produtos
    private DatabaseReference firebaseRef;
    private String idUtilizadorLogado;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        /*--Configuracoes Iniciais--*/
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        inicializarComponentes(); //recycle view
        firebaseRef = ConfiguracaoFirebase.getFirebase(); // recuperar produtos
        idUtilizadorLogado = UtilizadorFirebase.getIdUtilizador(); // get id of logged empresa


        /*--Configuracoes Toolbar--*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood - empresa");
        setSupportActionBar(toolbar);

        /*-- Configura recyclerview --*/
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);

        adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutos.setAdapter(adapterProduto);

        // RECUPERA DADOS DOS PRODUTOS DA EMPRESA
        recuperarProdutos();


        // ADICIONA EVENTO AO CLICK NO RECYCLE VIEW
        recyclerProdutos.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerProdutos, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Produto produtoSelecionado = produtos.get(position);
                        produtoSelecionado.removerProduto();
                        Toast.makeText(EmpresaActivity.this, "Produto Excluido com sucesso!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                })
        );



    }


    private void recuperarProdutos(){
        DatabaseReference produtosRef = firebaseRef
                .child("produtos")
                .child(idUtilizadorLogado);

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

    private void inicializarComponentes(){
        recyclerProdutos = findViewById(R.id.recyclerEmpresa);


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa , menu );
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                //logoutUser();
                logoutUtilizador();
                break;
            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                break;
            case R.id.menuNovoProduto:
                abrirNovoProduto();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //-------------------  METODOS AUXILIXARES ----------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    private void logoutUtilizador(){
        try {
            autenticacao.signOut();
            finish();
            //startActivity(new Intent(EmpresaActivity.this,AuthenticationActivity.class));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void abrirConfiguracoes(){
        startActivity(new Intent(EmpresaActivity.this, ConfiguracoesEmpresaActivity.class));
    }

    private void abrirNovoProduto(){
        startActivity(new Intent(EmpresaActivity.this, NovoProdutoEmpresaActivity.class));
    }









}
