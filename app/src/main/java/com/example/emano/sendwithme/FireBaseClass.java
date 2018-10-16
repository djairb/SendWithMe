package com.example.emano.sendwithme;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseClass {

    private DatabaseReference dataBaseReferencia = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usuarioReferencia = dataBaseReferencia.child("Usuarios");
    private DatabaseReference pedidosReferencia = dataBaseReferencia.child("Pedidos");
    //private DatabaseReference usuarioReferencia2;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    static Usuario usuario;
    //String uid = user.getUid();

    public void retornarUsuario(){

        String uid = user.getUid();
        DatabaseReference usuarioReferencia2 = dataBaseReferencia.child("Usuarios").child(user.getUid());
        usuarioReferencia2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                usuario = dataSnapshot.getValue(Usuario.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public Usuario montarUsuario(){
        retornarUsuario();
        String a = "12";
        String b = a;
        return usuario;
    }







}
