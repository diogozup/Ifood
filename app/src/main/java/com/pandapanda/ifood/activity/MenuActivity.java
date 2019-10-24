package com.pandapanda.ifood.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.pandapanda.ifood.listener.RecyclerItemClickListener;
import com.pandapanda.ifood.model.Empresa;
import com.pandapanda.ifood.model.ItemPedido;
import com.pandapanda.ifood.model.Pedido;
import com.pandapanda.ifood.model.Produto;
import com.pandapanda.ifood.model.Utilizador;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
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
    private List<ItemPedido> itensCarrinho = new ArrayList<>();

    private Pedido pedidoRecuperado;
    private int qtdItensCarrinho;
    private Double totalCarrinho;

    private TextView textCarrinhoQtd, textCarrinhoTotal;




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


        /*--------------------------- Configura recyclerview ----------------------------*/
        recyclerProdutosMenu.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutosMenu.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutosMenu.setAdapter(adapterProduto);

        // Configurar evento de clique
        recyclerProdutosMenu.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerProdutosMenu, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                confirmarQuantidade(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));


        // RECUPERA DADOS DOS PRODUTOS DA EMPRESA
        recuperarProdutos();
        recuperarDadosUtilizador();





    }


    private void inicializarComponentes(){
        recyclerProdutosMenu = findViewById(R.id.recyclerProdutoMenu);
        imageEmpresaMenu = findViewById(R.id.imageEmpresaMenu);
        textNomeEmpresaMenu = findViewById(R.id.textNomeEmpresaMenu);


        textCarrinhoQtd = findViewById(R.id.textCarrinhoQtd);
        textCarrinhoTotal = findViewById(R.id.textCarrinhoTotal);
    }


    private void recuperarPedido(){

        final DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_utilizador")
                .child(idEmpresaSelecionada)
                .child(idUtilizadorLogado);

        pedidoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdItensCarrinho = 0;
                totalCarrinho = 0.0;
                itensCarrinho = new ArrayList<>();



                if(dataSnapshot.getValue() != null){
                    pedidoRecuperado = dataSnapshot.getValue(Pedido.class);
                    itensCarrinho = pedidoRecuperado.getItens();

                    for(ItemPedido itemPedido: itensCarrinho){
                        int qtde = itemPedido.getQuantidade();
                        Double preco = itemPedido.getPreco();

                        totalCarrinho += (qtde * preco);
                        qtdItensCarrinho += qtde;
                    }
                }
                // para formatar o total
                DecimalFormat df = new DecimalFormat("0.00");

                textCarrinhoQtd.setText("qtd: " + String.valueOf(qtdItensCarrinho) );
                textCarrinhoTotal.setText("€ur: " + df.format(totalCarrinho));

                dialog.dismiss(); // nao fazer o dialog (loading animation) dont show

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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


    private void confirmarQuantidade(final int posicao){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quantidade");
        builder.setMessage("Insira quantidade");

        final EditText editQuantidade = new EditText(this);
        editQuantidade.setText("1");

        builder.setView(editQuantidade);



        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantidade = editQuantidade.getText().toString();

                Produto produtoSelecionado = produtos.get(posicao);
                ItemPedido itemPedido = new ItemPedido();
                itemPedido.setIdProduto(produtoSelecionado.getIdProduto());
                itemPedido.setNomeProduto(produtoSelecionado.getNome());
                itemPedido.setPreco(produtoSelecionado.getPreco());
                itemPedido.setQuantidade( Integer.parseInt(quantidade) );

                itensCarrinho.add(itemPedido);
                // para nao haver pedidos repetidos teria que:
                //1- Adicionar uma verificacao onde percorre todos os items do carrinho e assinala se existe ja um id desse produto
                //   ja existente. Se ja tem apenas incrementa quantidade.
                //2- Verifica se tem quantidade 0. nao pode ter ZERO.

                if(pedidoRecuperado == null ){
                    pedidoRecuperado = new Pedido(idUtilizadorLogado,idEmpresaSelecionada);
                }

                pedidoRecuperado.setNome(utilizador.getNome());
                pedidoRecuperado.setEndereco(utilizador.getEndereco());
                pedidoRecuperado.setItens(itensCarrinho);
                pedidoRecuperado.gravar();



            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }





























}
