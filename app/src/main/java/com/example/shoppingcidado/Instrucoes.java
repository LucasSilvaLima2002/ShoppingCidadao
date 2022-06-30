package com.example.shoppingcidado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Instrucoes extends AppCompatActivity {
    private Button bt_voltar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucoes);
        getSupportActionBar().hide();
        IniciarComponentes();
        bt_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Instrucoes.this,Foto.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void IniciarComponentes(){
        bt_voltar = findViewById(R.id.bt_voltar);
    }
}