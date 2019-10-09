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
import com.pandapanda.ifood.R;
import com.pandapanda.ifood.helper.ConfiguracaoFirebase;


public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //Configuracoes Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood - empresa");
        setSupportActionBar(toolbar);

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
