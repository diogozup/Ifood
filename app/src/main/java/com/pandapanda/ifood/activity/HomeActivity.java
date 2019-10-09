package com.pandapanda.ifood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.pandapanda.ifood.R;
import com.pandapanda.ifood.helper.ConfiguracaoFirebase;

public class HomeActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //
        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();


        //Configuracoes Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood - empresa");
        setSupportActionBar(toolbar);
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
