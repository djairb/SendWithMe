package com.example.emano.sendwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConfirmarPedidoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedido);
    }

    Intent intent2 = getIntent();
    private String origem = intent2.getStringExtra("origem");
    private String destino = intent2.getStringExtra("destino");
    private EditText ttitulo = (EditText)findViewById(R.id.editTítuloPedido);
    private EditText tnome = (EditText)findViewById(R.id.editNomeItem);
    private String titulo = ttitulo.getText().toString();
    private String nome = tnome.getText().toString();

    public Pedido montarPedido(){
        Pedido pedido = new Pedido();
        pedido.setOrigem(origem);
        pedido.setDestino(destino);
        pedido.setObjeto(nome);
        pedido.setTitulo(titulo);

        return pedido;
    }



    public void salvarPedido(View view){




    }

}
