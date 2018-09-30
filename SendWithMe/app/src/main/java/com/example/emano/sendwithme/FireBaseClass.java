package com.example.emano.sendwithme;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class FireBaseClass extends AppCompatActivity {

    public void inserirUsuario(Usuario usuario){
        DatabaseReference referenciaNoUsuarios = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        referenciaNoUsuarios.child(usuario.getId()).setValue(usuario);
        //criarLogin(usuario.getEmail(), usuario.getSenha());
    }

    public void criarLogin(String email, String senha, final Usuario usuario){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Log.i("Cadastro","Deu certo!");



                            FirebaseUser firebaseUser = task.getResult().getUser();
                            usuario.setId(firebaseUser.getUid());
                            inserirUsuario(usuario);


                        }else{
                            Log.i("Cadastro","Deu errado!");

                        }

                    }
                });

    }
}
