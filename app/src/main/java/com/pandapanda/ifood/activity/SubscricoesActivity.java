package com.pandapanda.ifood.activity;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.messaging.FirebaseMessaging;
import com.pandapanda.ifood.R;

public class SubscricoesActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    int checked;


    /*
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;
    */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscricoes);


        //Configuracoes tooblbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Subscricoes");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // ter voltao voltar


        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                checked = radioGroup.indexOfChild(findViewById(checkedId));

                switch (checked){
                    case 0:
                        // receber apenas notificacoes do topico "PIZZA"
                        FirebaseMessaging.getInstance().subscribeToTopic("PIZZAS");
                        Toast.makeText(getApplicationContext(),"Subscrito em Deliciosas Pizzas", Toast.LENGTH_SHORT).show();

                        break;
                    case 1:
                        FirebaseMessaging.getInstance().subscribeToTopic("HAMBURGERS");
                        Toast.makeText(getApplicationContext(),"Subscrito em Hamburgers Saborosos", Toast.LENGTH_SHORT).show();

                        break;

                    case 2:
                        FirebaseMessaging.getInstance().subscribeToTopic("FRANCESINHAS");
                        Toast.makeText(getApplicationContext(),"Subscrito em Apetitosas Francesinhas", Toast.LENGTH_SHORT).show();
                        break;

                        default:

                            break;
                }
            }
        });









        /*
        radioGroup = findViewById(R.id.radioGroup);
        textView = findViewById(R.id.text_view_selected);


        Button buttonApply = findViewById(R.id.button_apply);
        buttonApply.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();

                radioButton = findViewById(radioId);


                textView.setText("Sua escolha foi: " + radioButton.getText());

                //if radio 1 > subs to "Pizza"


            }
        });
        */
   }



        /*
    public void checkButton(View v ){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        Toast.makeText(this,"Selected Radio Button: " + radioButton.getText(),
                Toast.LENGTH_SHORT).show();
    }
    */


}
