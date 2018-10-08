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

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText cpf;
    private EditText senha;
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
                if(validaCadastro()){
                    //validaCPF(cpf.getText().toString()) &&
                    //FireBaseClass fireBaseClass = new FireBaseClass();
                    Usuario usuario = montarUsuario();
                    cadastrarUsuario(usuario.getEmail(), usuario.getSenha(), usuario);

                }else{
                    Toast.makeText(CadastroActivity.this, "Algum campo informado esta incorreto.", Toast.LENGTH_SHORT).show();
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

        botaoCadastrar = findViewById(R.id.btnCadastro);
        botaoCancelar = findViewById(R.id.btnCancelar);

    }

    public void inserirUsuarioNoBanco(Usuario usuario){
        DatabaseReference referenciaNoUsuarios = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        referenciaNoUsuarios.child(usuario.getId()).setValue(usuario);
        //cadastrarUsuario(usuario.getEmail(), usuario.getSenha());
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
                            Log.i("Cadastro","Deu errado!");
                            Toast.makeText(CadastroActivity.this, "Falha ao cadastrar usuário", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }



    public boolean validaCPF(String cpf){

        if (cpf.equals("00000000000") ||
                cpf.equals("11111111111") || cpf.equals("22222222222") || cpf.equals("33333333333") ||
                cpf.equals("44444444444") || cpf.equals("55555555555") || cpf.equals("66666666666") ||
                cpf.equals("77777777777") || cpf.equals("88888888888") || cpf.equals("99999999999") ||
                (cpf.length() != 11)) {
            return (false);
        }

        char digito10, digito11;
        int soma,i, r, num, peso;

        try {

            soma = 0;
            peso = 10;

            for (i=0; i<9; i++) {
                num = (int)(cpf.charAt(i) - 48);
                soma = soma + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (soma % 11);

            if ((r == 10) || (r == 11))
                digito10 = '0';
            else digito10 = (char)(r + 48);

            soma = 0;
            peso = 11;

            for(i=0; i<10; i++) {
                num = (int)(cpf.charAt(i) - 48);
                soma = soma + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (soma % 11);

            if ((r == 10) || (r == 11))
                digito11 = '0';
            else digito11 = (char)(r + 48);

            if ((digito10 == cpf.charAt(9)) && (digito11 == cpf.charAt(10)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }

    public boolean validaCadastro(){

        /*EditText nome = (EditText) findViewById(R.id.edtNome);
        EditText email = (EditText) findViewById(R.id.edtEmail);
        EditText cpf = (EditText) findViewById(R.id.edtCPF);
        EditText senha = (EditText) findViewById(R.id.edtSenha);*/

        boolean validade = true;

        if (nome.getText().equals(" ")) {
            nome.setText("");
        } else if (nome.getText().toString().equals("") ||nome.getText().toString() == null) {
            Toast.makeText(getApplicationContext(), "Nome inválido", Toast.LENGTH_LONG).show();
            validade = false;
        } else if (cpf.getText().toString().length() != 11 || cpf.getText().toString().
                contains("^[a-zA-ZÁÂÃÀÇÉÊÍÓÔÕÚÜáâãàçéêíóôõúü]*$")){
            Toast.makeText(getApplicationContext(), "CPF inválido", Toast.LENGTH_LONG).show();
            validade = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher((CharSequence) email.getText().toString()).matches()){
            Toast.makeText(getApplicationContext(), "Email inválido", Toast.LENGTH_LONG).show();
            validade = false;
        } else if(senha.getText().length()<8){
            Toast.makeText(getApplicationContext(),
                    "Senha muito curta(Tamanho mínimo: 8 caracteres)",
                     Toast.LENGTH_LONG).show();
            validade=false;
        }


        return validade;

    }

}
