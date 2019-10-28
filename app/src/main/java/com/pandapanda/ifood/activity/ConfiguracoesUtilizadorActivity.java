package com.pandapanda.ifood.activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pandapanda.ifood.R;
import com.pandapanda.ifood.helper.ConfiguracaoFirebase;
import com.pandapanda.ifood.helper.UtilizadorFirebase;
import com.pandapanda.ifood.model.Utilizador;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ConfiguracoesUtilizadorActivity extends AppCompatActivity {

    private EditText editUtilizadorNome, editUtilizadorEndereco;
    private String idUtilizador;
    private DatabaseReference firebaseRef;

    //address+", "+area+", "+city+", "+country+", "+postalcode
    //GeoCode
    TextView textView, textView_Address, textView_Area, textView_City, textView_Country, textView_PostalCode;
    Geocoder geocoder;
    List<Address> addresses;

    //ESTG coordenadas
    //41.367147, -8.194802
    Double latitude = 41.367147;
    Double longitude = -8.194802;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_utilizador);


        // Configuracoes Iniciais
        inicializarComponentes();
        idUtilizador = UtilizadorFirebase.getIdUtilizador();
        firebaseRef = ConfiguracaoFirebase.getFirebase();


        //Configuracoes tooblbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar dados do utilizador
        recuperarDadosUtilizador();

        //--------GeoCoder
        textView = (TextView) findViewById(R.id.textView_Morada);

        textView_Address = findViewById(R.id.textView_Address);
        textView_Area = findViewById(R.id.textView_Area);
        textView_City = findViewById(R.id.textView_City);
        textView_Country = findViewById(R.id.textView_Country);
        textView_PostalCode = findViewById(R.id.textView_PostalCode);





        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1);
            String address = addresses.get(0).getAddressLine(0);
            String area = addresses.get(0).getLocality();
            String city = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalcode = addresses.get(0).getPostalCode();

            String fullAddress = address+", "+area+", "+city+", "+country+", "+postalcode;
            textView.setText(fullAddress);

            textView_Address.setText(address);
            textView_Area.setText(area);
            textView_City.setText(city);
            textView_Country.setText(country);
            textView_PostalCode.setText(postalcode);



        } catch (IOException e) {
            e.printStackTrace();
        }

    }



   public void validarDadosUtilizador(View view){
        // Valida se os campos foram preenchidos
        String nome = editUtilizadorNome.getText().toString();
        String endereco = editUtilizadorEndereco.getText().toString();

        if(!nome.isEmpty()){
            if(!endereco.isEmpty()){
                //Aqui vamos ter que criar uma class com nome e endereco do user la no firebase
                //1- criar model Utilizador (campos: id,nome,endereco + construtor vazio + getrs&setrs
                //2- criar aqui uma instancia nova de utilizador
                //3- criar um metodo gravar(); onde vamos ao model e criamos esse mesmo metodo
                //4- adicionar aqui ainda o exibir mensagem de sucesso + finish();
                //5- AGORA VAMOS FAZER LOAD DOS DADOS PARA O FORMULARIO ATRAVEZ DO FIREBASE (DATABASE-RealTimeDatabase)
                //6- Criar depois de "Configs toolbar" recuperarDadosUtilizador();

                Utilizador utilizador = new Utilizador();
                utilizador.setIdUtilizador(idUtilizador); // em configuracoes iniciais ja demos valor a este id
                utilizador.setNome(nome);
                utilizador.setEndereco(endereco);
                utilizador.gravar();

                exibirMensagem("Dados atualizados com sucesso!");
                finish();



            }else{
                exibirMensagem("Introduza um endereco de entrega!");
            }
        }else{
            exibirMensagem("Introduza um nome de utilizador");
        }



   }

   private void exibirMensagem(String texto){
       Toast.makeText(this,texto,Toast.LENGTH_SHORT)
               .show();
   }


    private void inicializarComponentes(){
        editUtilizadorNome = findViewById(R.id.editUtilizadorNome);
        editUtilizadorEndereco = findViewById(R.id.editUtilizadorEndereco);
    }


    private void recuperarDadosUtilizador(){

        DatabaseReference utilizadorRef = firebaseRef
                .child("utilizadores")
                .child(idUtilizador);

        utilizadorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null ){
                    Utilizador utilizador = dataSnapshot.getValue(Utilizador.class);
                    editUtilizadorNome.setText( utilizador.getNome());
                    editUtilizadorEndereco.setText(utilizador.getEndereco());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



}
