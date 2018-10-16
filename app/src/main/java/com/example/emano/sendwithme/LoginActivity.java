package com.example.emano.sendwithme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button botaoCadastrarHome;
    private Button botaoLogar;
    private EditText email;
    private EditText senha;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        setView();
        botaoCadastrarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,CadastroActivity.class));

            }
        });

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fazerLogin(email.getText().toString().trim(), senha.getText().toString().trim());

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            startActivity(new Intent(LoginActivity.this,HomeDrawerActivity.class));
        }
        //updateUI(currentUser);

    }

    public void setView(){
        botaoCadastrarHome = findViewById(R.id.botaoCadastrarHome);
        botaoLogar = findViewById(R.id.botaoEntrarId);
        email = findViewById(R.id.emailPerfilId);
        senha = findViewById(R.id.senhaId);

    }

    public void fazerLogin(String email, String senha){
        if (!email.isEmpty()&&!senha.isEmpty()){
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {//sucesso ao logar
                                Log.i("Login","Deu certo!");
                                startActivity(new Intent(LoginActivity.this, HomeDrawerActivity.class));
                            }else{
                                Toast.makeText(LoginActivity.this, "Erro ao fazer login", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }else{
            Toast.makeText(LoginActivity.this,"Campos inválidos", Toast.LENGTH_LONG).show();
        }



    }


}
