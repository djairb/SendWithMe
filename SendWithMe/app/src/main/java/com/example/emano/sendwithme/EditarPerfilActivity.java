package com.example.emano.sendwithme;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editEmail;
    private EditText editCpf;
    private EditText editSenha;
    private EditText editSenhaConfirma;
    private Button editBotaoAtualizar;
    private Button editBotaoCancelar;
    private Button editBotaoDeletar;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        setView();
        user = FirebaseAuth.getInstance().getCurrentUser();
        editBotaoAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    carregarInfoUsuario("1");

                }

            }
        });



        editBotaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editBotaoDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditarPerfilActivity.this);
                builder.setTitle("Deletar Conta");
                builder.setMessage("Deseja prosseguir com a exclusão de sua conta?");

                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        carregarInfoUsuario("0");

                    }
                });

                builder.setNegativeButton("Cancelar",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }



    private void setView() {
        editNome = findViewById(R.id.edtPerfilNome);
        editEmail = findViewById(R.id.edtPerfilEmail);
        editCpf = findViewById(R.id.edtPerflCpf);
        editSenha = findViewById(R.id.edtPerfilSenha);
        editSenhaConfirma = findViewById(R.id.edtPerfilSenhaCompara);
        editBotaoAtualizar = findViewById(R.id.btnEditPerfilAtualizar);
        editBotaoCancelar = findViewById(R.id.btnPerfilCancelar);
        editBotaoDeletar = findViewById(R.id.deletarContaId);
    }

    private void carregarInfoUsuario(final String dellOrUpdate) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        DatabaseReference dataBaseReferencia = FirebaseDatabase.getInstance().getReference()
                .child("Usuarios").child(id);
        dataBaseReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                //"1" for update - "0" for dell
                if(dellOrUpdate.equals("0")){
                    deletarConta(usuario.getSenha());

                }else if(dellOrUpdate.equals("1")){
                    validarEmailFireBase(usuario.getSenha());
                }else{
                    Toast.makeText(EditarPerfilActivity.this,"Erro de Else", Toast.LENGTH_LONG).show();
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        String cpfString =  editCpf.getText().toString().trim();
        if(cpfString.isEmpty()){
            erro = true;
            editCpf.setError("Campo em branco");
        }else if(cpfString.length() != 11){
            erro = true;
            editCpf.setError("Cpf não tem 11 digitos");
        }else if(!cpfString.matches("[0-9]+")){
            erro = true;
            editCpf.setError("Cpf não contem apenas numeros");
        }
        return erro;
    }

    private boolean validarNome(){
        boolean erro = false;
        String nomeString = editNome.getText().toString().trim();
        if(nomeString.isEmpty()){
            erro = true;
            editNome.setError("Campo em branco");
        }
        return erro;
    }


    private boolean validarEmail(){
        boolean erro = false;
        String emailString = editEmail.getText().toString().trim();
        if(emailString.isEmpty()){
            erro = true;
            editEmail.setError("Campo em branco");
        }else{
            String excecoes = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
            Pattern pattern = Pattern.compile(excecoes);
            Matcher matcher = pattern.matcher(emailString);

            if(!matcher.matches()){
                erro = true;
                editEmail.setError("Email informado inválido");
            }
        }
        return erro;
    }

    private boolean validarSenha(){
        boolean erro = false;
        String senhaString = editSenha.getText().toString();
        String confirmarSenhaString = editSenhaConfirma.getText().toString();
        if(senhaString.isEmpty() && confirmarSenhaString.isEmpty()){
            erro = true;
            editSenha.setError("Campo em branco");
            editSenhaConfirma.setError("Campo em branco");
        } else if(senhaString.isEmpty()){
            erro = true;
            editSenha.setError("Campo em branco");
        }else if(confirmarSenhaString.isEmpty()){
            erro = true;
            editSenhaConfirma.setError("Campo em branco");
        }else if(senhaString.length() < 6){
            erro = true;
            editSenha.setError("A senha deve conter pelo menos 6 caracteres");
        }else if(!senhaString.equals(confirmarSenhaString)){
            erro = true;
            editSenha.setError("As senhas devem ser iguais");
            editSenhaConfirma.setError("As senhas devem ser iguais");
        }
        return erro;
    }

    public void validarEmailFireBase(String senhaAtual){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), senhaAtual);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(editEmail.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            validarSenhaFireBase();

                                        }else{
                                            editEmail.setError("Email não pode ser alterado. " +
                                                    "Verifique o Email informado ou tente outro.");
                                        }

                                    }
                                });

                    }
                });


    }

    public void validarSenhaFireBase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(editSenha.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            atualizarUsuarioBanco();
                        }else{
                            editSenha.setError("Senha não pode ser alterada. " +
                                    "Verifique a senha informada ou tente outra.");

                        }

                    }
                });

    }

    public Usuario montarUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNome(editNome.getText().toString());
        usuario.setCpf(editCpf.getText().toString());
        usuario.setEmail(editEmail.getText().toString());
        usuario.setSenha(editSenha.getText().toString());
        usuario.setId(user.getUid());

        return usuario;
    }

    public void atualizarUsuarioBanco(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dataBaseReferencia = FirebaseDatabase.getInstance().getReference()
                .child("Usuarios").child(user.getUid());
        dataBaseReferencia.setValue(montarUsuario());
        startActivity(new Intent(EditarPerfilActivity.this, PerfilActivity.class));
        Toast.makeText(this,"Dados atualizados com suscesso",Toast.LENGTH_LONG).show();
        finish();

    }

    private void deletarConta(String senhaAtual) {



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), senhaAtual);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditarPerfilActivity.this,"Usuário deletado",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(EditarPerfilActivity.this, LoginActivity.class));
                                        }else{
                                            Toast.makeText(EditarPerfilActivity.this,"Erro ao deletar usuário",Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                    }
                });
    }

}
