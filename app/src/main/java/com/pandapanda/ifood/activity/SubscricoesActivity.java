package com.pandapanda.ifood.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.messaging.FirebaseMessaging;
import com.pandapanda.ifood.R;

import java.util.ArrayList;

public class SubscricoesActivity extends AppCompatActivity {
    /*
    RadioGroup radioGroup;
    int checked;
    */
    /*
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;
    */

    ArrayList<String> selection = new ArrayList<String>();
    TextView final_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscricoes);


        //Configuracoes tooblbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Subscricoes");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // ter voltao voltar

        final_text = findViewById(R.id.final_result);
        final_text.setEnabled(false);


        /*
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
        */
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

    public void selectItem(View view){
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()){
            case R.id.opcao1:
                if(checked){
                    selection.add("Pizza Hut");
                    FirebaseMessaging.getInstance().subscribeToTopic("PIZZAHUT");
                }else{
                    selection.remove("Pizza Hut");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("PIZZAHUT");
                }
                break;
            case R.id.opcao2:
                if(checked){
                    selection.add("Mc Donalds");
                    FirebaseMessaging.getInstance().subscribeToTopic("MCDONALD");
                }else{
                    selection.remove("Mc Donalds");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("MCDONALD");

                }
                break;
            case R.id.opcao3:
                if(checked){
                    selection.add("Taberna Belga");
                    FirebaseMessaging.getInstance().subscribeToTopic("TABERNABELGA");

                }else{
                    selection.remove("Taberna Belga");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("TABERNABELGA");

                }
                break;
        }
    }

    public void finalSelection(View view){

        String final_selection = "";
        for(String Selections : selection){
            final_selection = final_selection + Selections + "\n" ;
        }
        final_text.setText(final_selection);
        final_text.setEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }



}
