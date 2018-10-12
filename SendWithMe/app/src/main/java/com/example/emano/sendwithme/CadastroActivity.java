package com.example.emano.sendwithme;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText cpf;
    private EditText senha;
    private EditText senhaConfirma;
    private Button botaoCadastrar;
    private Button botaoCancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        setView();
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    //validaCPF(cpf.getText().toString()) &&
                    //FireBaseClass fireBaseClass = new FireBaseClass();
                    Usuario usuario = montarUsuario();
                    cadastrarUsuario(usuario.getEmail(), usuario.getSenha(), usuario);

                }
            }
        });

        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public Usuario montarUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNome(nome.getText().toString());
        usuario.setCpf(cpf.getText().toString());
        usuario.setEmail(email.getText().toString());
        usuario.setSenha(senha.getText().toString());

        return usuario;
    }



    public void setView(){

        nome = findViewById(R.id.edtNome);
        email = findViewById(R.id.edtEmail);
        cpf = findViewById(R.id.edtCPF);
        senha = findViewById(R.id.edtSenha);
        senhaConfirma = findViewById(R.id.edtSenhaCompara);
        botaoCadastrar = findViewById(R.id.btnCadastro);
        botaoCancelar = findViewById(R.id.btnCancelar);

    }

    public void inserirUsuarioNoBanco(Usuario usuario){
        DatabaseReference referenciaNoUsuarios = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        referenciaNoUsuarios.child(usuario.getId()).setValue(usuario);
    }

    public void cadastrarUsuario(String email, String senha, final Usuario usuario){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Log.i("Cadastro","Deu certo!");
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            usuario.setId(firebaseUser.getUid());
                            inserirUsuarioNoBanco(usuario);
                            Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();
                            finish();


                        }else{
                            Toast.makeText(CadastroActivity.this, "Falha ao cadastrar usuário. Verifique os dados informados e tente novamente", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }


    private boolean validarCampos() {
        boolean erro = true;
        if (validarCpf()) {
            erro = false;
        }
        if (validarNome()) {
            erro = false;
        }
        if (validarEmail()) {
            erro = false;
        }
        if (validarSenha()) {
            erro = false;
        }
        return erro;
    }

    private boolean validarCpf(){
        boolean erro = false;
        String cpfString =  cpf.getText().toString().trim();
        if(cpfString.isEmpty()){
            erro = true;
            cpf.setError("Campo em branco");
        }else if(cpfString.length() != 11){
            erro = true;
            cpf.setError("Cpf não tem 11 digitos");
        }else if(!cpfString.matches("[0-9]+")){
            erro = true;
            cpf.setError("Cpf não contem apenas numeros");
        }
        return erro;
    }

    private boolean validarNome(){
        boolean erro = false;
        String nomeString = nome.getText().toString().trim();
        if(nomeString.isEmpty()){
            erro = true;
            nome.setError("Campo em branco");
        }
        return erro;
    }


    private boolean validarEmail(){
        boolean erro = false;
        String emailString = email.getText().toString().trim();
        if(emailString.isEmpty()){
            erro = true;
            email.setError("Campo em branco");
        }else{
            String excecoes = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
            Pattern pattern = Pattern.compile(excecoes);
            Matcher matcher = pattern.matcher(emailString);

            if(!matcher.matches()){
                erro = true;
                email.setError("Email informado inválido");
            }
        }
        return erro;
    }

    private boolean validarSenha(){
        boolean erro = false;
        String senhaString = senha.getText().toString();
        String confirmarSenhaString = senhaConfirma.getText().toString();
        if(senhaString.isEmpty() && confirmarSenhaString.isEmpty()){
            erro = true;
            senha.setError("Campo em branco");
            senhaConfirma.setError("Campo em branco");
        } else if(senhaString.isEmpty()){
            erro = true;
            senha.setError("Campo em branco");
        }else if(confirmarSenhaString.isEmpty()){
        erro = true;
        senhaConfirma.setError("Campo em branco");
        }else if(senhaString.length() < 6){
            erro = true;
            senha.setError("A senha deve conter pelo menos 6 caracteres");
        }else if(!senhaString.equals(confirmarSenhaString)){
            erro = true;
            senha.setError("As senhas devem ser iguais");
            senhaConfirma.setError("As senhas devem ser iguais");
        }
        return erro;
    }



}
