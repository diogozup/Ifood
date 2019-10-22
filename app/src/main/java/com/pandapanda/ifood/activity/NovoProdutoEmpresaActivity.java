package com.pandapanda.ifood.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pandapanda.ifood.R;
import com.pandapanda.ifood.helper.UtilizadorFirebase;
import com.pandapanda.ifood.model.Produto;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    private EditText editProdutoNome, editProdutoDescricao, editProdutoPreco;
    private String idUtilizadorLogado;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);


        /*Configuracoes iniciais*/
        inicializarComponentes();
        idUtilizadorLogado = UtilizadorFirebase.getIdUtilizador();

        //Configuracao toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void validarDadosProduto(View view){
        //Valida se os campos foram preenchidos
        String nome = editProdutoNome.getText().toString();
        String descricao = editProdutoDescricao.getText().toString();
        String preco = editProdutoPreco.getText().toString();



        if(!nome.isEmpty()){
            if(!descricao.isEmpty()) {
                if(!preco.isEmpty()){

                Produto produto = new Produto();
                produto.setIdUtilizador(idUtilizadorLogado);
                produto.setNome(nome);
                produto.setDescricao(descricao);
                produto.setPreco( Double.parseDouble(preco) );
                produto.gravarProduto();

                finish();
                exibirMensagem("Produto gravado com sucesso");

            }else{
                    exibirMensagem("Insira um preco para um Produto");
                }
            }else{
                exibirMensagem("Insira uma descricao para Produto");
            }
        }else {
            exibirMensagem("Insira nome para o Produto");
        }

    }
    private void exibirMensagem(String texto){
        Toast.makeText(this, texto,Toast.LENGTH_SHORT)
                .show();
    }



    private void inicializarComponentes(){
        editProdutoNome = findViewById(R.id.editProdutoNome);
        editProdutoDescricao = findViewById(R.id.editProdutoDescricao);
        editProdutoPreco = findViewById(R.id.editProdutoPreco);



    }










}
