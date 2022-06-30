package com.example.shoppingcidado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Escolha_carteira extends AppCompatActivity {
    private TextView carteira_trabalho,carteira_identidade,carteira_motorista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_carteira);
        getSupportActionBar().hide();
        IniciarComponentes();

        carteira_trabalho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser UsuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

                if(UsuarioAtual != null){
                    Toast.makeText(getBaseContext(), "Usuario esta cadastrado", Toast.LENGTH_LONG).show();}
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Escolha_carteira.this,Foto.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private void IniciarComponentes() {
        carteira_identidade = findViewById(R.id.carteira_identidade);
        carteira_trabalho = findViewById(R.id.carteira_trabalho);
        carteira_motorista = findViewById(R.id.carteira_motorista);
    }

}


