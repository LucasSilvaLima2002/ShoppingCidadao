package com.example.shoppingcidado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class FormCadastro extends AppCompatActivity {
    private EditText edit_nome,edit_email,edit_senha,edit_cpf,edit_telefone,edit_rg;
    private Button bt_cadastrar;
    String[] mensagens = {"Preencha todos os campos","Por favor, acesse o link de confirmação pelo e-mail"};
    String UsuarioId;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        getSupportActionBar().hide();
        IniciarComponentes();
        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telefone = edit_telefone.getText().toString();
                String cpf = edit_cpf.getText().toString();
                String nome = edit_nome.getText().toString();
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();
                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || cpf.isEmpty() || telefone.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_LONG);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.show();
                } else{ CadastrarUsuario(v);

                }
            }
        });
    }


    private void CadastrarUsuario(View v) {

        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                SalvarDadosUsuario();
                                Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_LONG);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.show();
                            }

                        }
                    });
                }else {
                    String erro;
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha com no minimo 6 caracteres";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Esta conta ja foi cadastrada";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "E-mail inválido";
                    } catch (Exception e) {
                        erro = "Erro ao cadastrar usuário";
                    }
                    Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_LONG);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.show();
                }

            }
        });
    }

    private void SalvarDadosUsuario(){
        String email = edit_email.getText().toString();
        String nome = edit_nome.getText().toString();
        String senha = edit_senha.getText().toString();
        String telefone = edit_telefone.getText().toString();
        String cpf = edit_cpf.getText().toString();
        String rg = edit_rg.getText().toString();

        FirebaseFirestore db= FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();

        usuarios.put("Nome",nome);
        usuarios.put("Telefone",telefone);
        usuarios.put("CPF",cpf);
        usuarios.put("E-mail",email);
        usuarios.put("RG",rg);
        UsuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DocumentReference documentReference = db.collection("Usuarios").document(UsuarioId);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","Sucesso ao salvar os dados");


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db_error","Erro ao salvar os dados"+ e.toString());

                    }
                });



    }

    private void IniciarComponentes(){
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
        edit_cpf = findViewById(R.id.edit_cpf);
        edit_telefone = findViewById(R.id.edit_telefone);
        edit_rg = findViewById(R.id.edit_RG);
    }
}