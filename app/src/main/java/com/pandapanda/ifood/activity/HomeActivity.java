package com.pandapanda.ifood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.pandapanda.ifood.R;
import com.pandapanda.ifood.adapter.AdapterEmpresa;
import com.pandapanda.ifood.helper.ConfiguracaoFirebase;
import com.pandapanda.ifood.listener.RecyclerItemClickListener;
import com.pandapanda.ifood.model.Empresa;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;
    //recycle view
    private RecyclerView recyclerEmpresa;
    private List<Empresa> empresas = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private AdapterEmpresa adapterEmpresa;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*-- Configuracoes Iniciais --*/
        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //recyclerview
        firebaseRef = ConfiguracaoFirebase.getFirebase();




        /*-- Configuracoes Toolbar --*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood - empresa");
        setSupportActionBar(toolbar);



        /*-- Configura recyclerview --*/
        recyclerEmpresa.setLayoutManager(new LinearLayoutManager(this));
        recyclerEmpresa.setHasFixedSize(true);

        adapterEmpresa = new AdapterEmpresa(empresas);
        recyclerEmpresa.setAdapter(adapterEmpresa);

        // RECUPERA DADOS DE EMPRESAS para LISTAR TODAS EXISTENTEs
        recuperarEmpresas();

        //Configuracao do search view
        searchView.setHint("Pesquisar restaurantes");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //pesquisarEmpresas (query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarEmpresas ( newText) ;
                return true;
            }
        });

        // Configurar evento de clique nas empresas para mostrar produtos
        recyclerEmpresa.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerEmpresa, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Empresa empresaSelecionada = empresas.get(position);
                Intent i = new Intent(HomeActivity.this, MenuActivity.class);

                i.putExtra("empresa", empresaSelecionada);

                startActivity(i);


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));


    }








    private void pesquisarEmpresas(String pesquisa){
        DatabaseReference empresasRef = firebaseRef
                .child("empresas");
        Query query = empresasRef.orderByChild("nome") // ALTERAR AQUI PARA "nome_filtro" SE QUISER NA SEARCHBAR IGNORAR MAIUS/MINUSCLAS NA PRODURA

                .startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                empresas.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    empresas.add(ds.getValue(Empresa.class) );
                }

                adapterEmpresa.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void recuperarEmpresas(){
        DatabaseReference empresaRef = firebaseRef.child("empresas");
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                empresas.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren() ){
                    empresas.add(ds.getValue(Empresa.class) );
                }
                adapterEmpresa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_utilizador , menu );

        //Configurar botao de pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);


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
        }

        return super.onOptionsItemSelected(item);
    }


    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //-------------------  METODOS AUXILIXARES ----------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------

    private void inicializarComponentes(){
        searchView = findViewById(R.id.materialSearchView);
        recyclerEmpresa = findViewById(R.id.recyclerEmpresa);

    }







    //------------------------------------ ja vinha copiado do EmpresaActivity

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
        startActivity(new Intent(HomeActivity.this, ConfiguracoesUtilizador.class));
    }










}
