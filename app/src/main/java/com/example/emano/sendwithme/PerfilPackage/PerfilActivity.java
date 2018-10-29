package com.example.emano.sendwithme.PerfilPackage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.emano.sendwithme.R;
import com.example.emano.sendwithme.UsuarioPackage.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilActivity extends AppCompatActivity {

    private TextView nomePerfil;
    private TextView emailPerfil;
    private Button botaoEditarPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setView();
        setarDadosPerfil();



        botaoEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PerfilActivity.this, "IR pra a tela de edicao", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PerfilActivity.this, EditarPerfilActivity.class));
            }
        });





    }

    private void setarDadosPerfil() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dataBaseReferencia = FirebaseDatabase.getInstance().getReference()
                .child("Usuarios").child(user.getUid());
        dataBaseReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                nomePerfil.setText(usuario.getNome());
                emailPerfil.setText(usuario.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void setView() {
        nomePerfil = findViewById(R.id.nomePerfilId);
        emailPerfil = findViewById(R.id.emailPerfilId);
        botaoEditarPerfil = findViewById(R.id.botaoEditarPerfilId);
    }
}
