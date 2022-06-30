package com.example.shoppingcidado;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.okhttp.internal.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.Permission;
import java.util.ArrayList;
import java.util.UUID;

public class Foto extends AppCompatActivity implements View.OnClickListener {


        private Button button_Enviar;
        private ImageView imageView;
        private Uri uri_imagem;
        private String urlimagem;
        private Button bt_deslogar;
        private TextView bt_instrução;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        private FirebaseStorage storage;
        ActionBar actionBar;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_foto);
            actionBar = getSupportActionBar();
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
            actionBar.setTitle("");


            imageView = (ImageView) findViewById(R.id.imageView_StorageUpload);
            button_Enviar = (Button) findViewById(R.id.button_StorageUpload_Enviar);

            button_Enviar.setOnClickListener(this);
            storage = FirebaseStorage.getInstance();
            storage = FirebaseStorage.getInstance();
            IniciarComponentes();


            permissao();
bt_deslogar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Foto.this,FormLogin.class);
        startActivity(intent);
        finish();
    }
});
bt_instrução.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Foto.this,Instrucoes.class);
        startActivity(intent);
        finish();
    }
});

        }






        private void permissao() {
            String permissoes[] = new String[]{
                    Manifest.permission.CAMERA
            };
            Permissao.permissao(this, 0, permissoes);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_StorageUpload_Enviar:
                   button_upload();
                    break;


            }

            }
    private void button_upload() {
        if (imageView.getDrawable() != null) {
            upload_Imagem1();
            //upload_imagem2();
        }
        else {
            Toast.makeText(getBaseContext(), "Insira uma imagem", Toast.LENGTH_LONG).show();
        }
    }



        private void upload_Imagem1() {
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            StorageReference reference = storage.getReference().child("Fotos");
            StorageReference nome_imagem = reference.child(email + System.currentTimeMillis() + ".jpg");
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress((Bitmap.CompressFormat.JPEG), 100, bytes);
            UploadTask uploadTask = nome_imagem.putBytes(bytes.toByteArray());
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getBaseContext(), "Sucesso ao realizar upload", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getBaseContext(), "Erro ao relizar upload", Toast.LENGTH_LONG).show();

                    }

                }
            });


            }



        private void upload_imagem2() {
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            StorageReference reference = storage.getReference().child("Carteira de motorista");
            StorageReference nome_imagem = reference.child(email + System.currentTimeMillis() + ".jpg");



            Glide.with(getBaseContext()).asBitmap().load(uri_imagem).apply(new RequestOptions().override(1024, 768)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    Toast.makeText(getBaseContext(), "Erro ao transformar a imagem", Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    resource.compress((Bitmap.CompressFormat.JPEG), 70, bytes);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes.toByteArray());
                    try {
                        bytes.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UploadTask uploadTask = nome_imagem.putStream(inputStream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            return nome_imagem.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri uri = task.getResult();
                                String url_imagem = uri.toString();
                                DialogAlerta alerta = new DialogAlerta("URL IMAGEM", url_imagem);
                                alerta.show(getSupportFragmentManager(), "3");
                                Toast.makeText(getBaseContext(), "Sucesso ao realizar o upload", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Falha ao realizar o upload", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    return false;

                }
            }).submit();


        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_storage_upload, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.item_galeria:

                    obterImagem_Galeria();


                    break;
                case R.id.item_camera:

                    obterImagem_camera();
                    break;
            }

            return super.onOptionsItemSelected(item);
        }


        private void obterImagem_camera() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String nomeImagem = diretorio.getPath() + "/" + "Foto Shopping Cidadão" + System.currentTimeMillis() + ".jpg";
            File file = new File(nomeImagem);
            String autorização = "com.example.shoppingcidado";
            uri_imagem = FileProvider.getUriForFile(getBaseContext(), autorização, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_imagem);
            startActivityForResult(intent, 1);
        }


        private void obterImagem_Galeria() {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

            startActivityForResult(Intent.createChooser(intent, "Escolha uma opção"), 0);


        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            for (int result : grantResults) {
                if (result == getPackageManager().PERMISSION_DENIED) {
                    Toast.makeText(getBaseContext(), "Aceite as permissões do aplicativo", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                }
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (resultCode == RESULT_OK) {
                    if (requestCode == 0) {
                        if (data != null) {
                            uri_imagem = data.getData();
                            Glide.with(getBaseContext()).asBitmap().load(uri_imagem).listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    Toast.makeText(getBaseContext(), "Falha ao selecionar imagem", Toast.LENGTH_LONG).show();

                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).into(imageView);

                        } else {
                            Toast.makeText(getBaseContext(), "Falha ao selecionar imagem", Toast.LENGTH_LONG).show();


                        }

                    } else if (requestCode == 1) {
                        if (uri_imagem != null) {
                            Glide.with(getBaseContext()).asBitmap().load(uri_imagem).listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    Toast.makeText(getBaseContext(), "Falha ao selecionar imagem", Toast.LENGTH_LONG).show();

                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).into(imageView);
                        } else {
                            Toast.makeText(getBaseContext(), "Falha ao tirar foto", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }


        }
    private void IniciarComponentes(){

        bt_deslogar = findViewById(R.id.bt_deslogar);
        bt_instrução = findViewById(R.id.bt_instrução);

    }


}




