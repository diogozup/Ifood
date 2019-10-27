package com.pandapanda.ifood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pandapanda.ifood.R;
import com.pandapanda.ifood.helper.ConfiguracaoFirebase;
import com.pandapanda.ifood.helper.UtilizadorFirebase;


public class AuthenticationActivity extends AppCompatActivity {
    private Button botaoEntrar;
    private EditText campoEmail, campoPassword;
    private Switch tipoEntrar , tipoUtilizador;
    private LinearLayout linearTipoUtilizador;

    private FirebaseAuth autenticacao;

    private Button botaoAbout;


    @Override//------------------------------------------------ Metodo OnCreate Autenticacao
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);
        //getSupportActionBar().hide();

        // receber apenas notificacoes do topico "Pizza"
        FirebaseMessaging.getInstance().subscribeToTopic("Pizza");


        iniciarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signOut();


        //Verificar se user ja fez login
        verificarUtilizadorLogado();

        tipoEntrar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//empresa
                    linearTipoUtilizador.setVisibility(View.VISIBLE);
                } else {//utilizador
                    linearTipoUtilizador.setVisibility(View.GONE);
                }
            }
        });


        botaoEntrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String password = campoPassword.getText().toString();

                if (!email.isEmpty()  ){
                    if( !password.isEmpty() ){

                        //------------------------------- Verifica estado do switch
                        if(tipoEntrar.isChecked()){ //--- Registar
                            autenticacao.createUserWithEmailAndPassword(
                                    email, password
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //redirect somewhere
                                        Toast.makeText(AuthenticationActivity.this,"Registo realizado com sucesso!", Toast.LENGTH_SHORT).show();

                                        String tipoUtilizador = getTipoUtilizador();
                                        UtilizadorFirebase.atualizarTipoUtilizador(tipoUtilizador);
                                        abrirTelaPrincipal(tipoUtilizador);
                                    }else{
                                        //erro de execao
                                        String erroExcecao = "";

                                        try{
                                            throw task.getException();
                                        }catch (FirebaseAuthWeakPasswordException e){
                                            erroExcecao = "Digite uma password mais forte!";
                                        }catch (FirebaseAuthInvalidCredentialsException e){
                                            erroExcecao = "Por favor, digite um e-mail válido";
                                        }catch (FirebaseAuthUserCollisionException e){
                                            erroExcecao = "Esta conta ja existe";
                                        }catch(Exception e){
                                            erroExcecao = "Ao registar utilizador: " + e.getMessage();
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(AuthenticationActivity.this, "Erro" + erroExcecao, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{ //--- Login
                            autenticacao.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AuthenticationActivity.this, "Login com sucesso! " ,Toast.LENGTH_SHORT).show();
                                        String tipoUtilizador = task.getResult().getUser().getDisplayName();

                                        UtilizadorFirebase.atualizarTipoUtilizador(tipoUtilizador);
                                        abrirTelaPrincipal(tipoUtilizador);

                                    }else{//Erro login
                                        Toast.makeText(AuthenticationActivity.this, "Erro de login: " + task.getException(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }else{
                        Toast.makeText(AuthenticationActivity.this,
                                "Preencha a Password",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AuthenticationActivity.this,
                            "Preencha o Email!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        botaoAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaAbout();

            }
        });

    }

    //-------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------- Metodos Aux
    private void verificarUtilizadorLogado(){
        FirebaseUser utilizadorAtual = autenticacao.getCurrentUser();
        if ( utilizadorAtual != null){
            String tipoUtilizador = utilizadorAtual.getDisplayName();
            abrirTelaPrincipal(tipoUtilizador);
        }
    }

    private String getTipoUtilizador(){
        return tipoUtilizador.isChecked() ? "E" : "U";
    }

    private void abrirTelaPrincipal(String tipoUtilizador){
        if(tipoUtilizador.equals("E")){ //empresa
            startActivity(new Intent(getApplicationContext(), EmpresaActivity.class));

        }else{// utilizador
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

        }
    }

    private void abrirTelaAbout(){
        startActivity(new Intent(getApplicationContext(),AboutActivity.class));
    }

    private void iniciarComponentes(){
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoPassword = findViewById(R.id.editCadastroSenha);
        botaoEntrar = findViewById(R.id.buttaoAcesso);
        tipoEntrar = findViewById(R.id.switchAcesso);

        tipoUtilizador = findViewById(R.id.switchTipoUtilizador);
        linearTipoUtilizador = findViewById(R.id.linearTipoUtilizador);

        botaoAbout = findViewById(R.id.buttonAbout);


    }

}
