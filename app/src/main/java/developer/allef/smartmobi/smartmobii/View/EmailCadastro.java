package developer.allef.smartmobi.smartmobii.View;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.dd.CircularProgressButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.perf.metrics.AddTrace;

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobii.Model.Usuario;
import developer.allef.smartmobi.smartmobii.R;

public class EmailCadastro extends AppCompatActivity {

    //region Variaveis Globais
    private static final String TAG = "énois";
    @BindView(R.id.inpcadEmail)
    TextInputLayout icadEmail;

    @BindView(R.id.inpcadNome)
    TextInputLayout icadNome;

    @BindView(R.id.inpcadSenha)
    TextInputLayout icadSenha;

    @BindView(R.id.cadEmail)
    EditText cEmail;
    @BindView(R.id.cadSenha)
    EditText cSenha;

    @BindView(R.id.cadNome)
    EditText cNome;

    @BindView(R.id.btncadastr)
     CircularProgressButton btcadas;
    Usuario userLocal = new Usuario();
    FirebaseUser user;
    int flag=0;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //endregion

    @AddTrace(name = "onCreateTraceCadEmail", enabled = true/*Optional*/)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_cadastro);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();


        //region Toolbar config
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCadEmail);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion

        /**
         * quando o usuario fizer o cadastro  esse metodo Já faz o login dele no app para assim atualizar seu nome e
         * fazer o envio do email de confirmação.....
         */

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
              user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    /**
                     * flag para entrar so uma vez nesse bloco e enviar somente um email ..
                     * pois esta dentro de um Listners que é chamado o tempo .
                     */

                    if(flag == 0){
                        if (user != null) {
                                flag = 1;
                            // metodo a seguir responsavel por enviar o email de Confirmação !!
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    /**
                                     * necessario deslogar o usuario pois nesse momento ele esta autenticado
                                     * e como nas telas anteriores listners de login ele pegava a referencia da tela anterior.
                                     * e autenticava o usuario
                                     */
                                    if(task.isSuccessful()){
                                        dialogConfirmacaoEmail();
                                        mAuth.signOut();


                                    }
                                }

                            });
                        }

                    }

                    /**
                     * fazendo o update do cadastro no firebase porque para fazer o login so é necessario
                     * o emaill e a senha , e por estetica da app futuramente utilizamos o Nome e email para exibição
                     */
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(userLocal.getNome())
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mAuth.signOut();
                                    }
                                }
                            });
                } else {
                    // User is signed out
                }


                // ...
            }
        };

        btcadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recebendoDados();

                if (userLocal.getEmail()!= null && userLocal.getSenha()!=null) {

                    btcadas.setIndeterminateProgressMode(true);
                    btcadas.setProgress(70);


                    mAuth.createUserWithEmailAndPassword(userLocal.getEmail(), userLocal.getSenha()).addOnCompleteListener(EmailCadastro.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            btcadas.setProgress(0);
                            if (task.isSuccessful()) {

                                btcadas.setProgress(100);
                                limpar();

                            }
                            if(!task.isSuccessful()) {
                                btcadas.setProgress(-1);

                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    icadSenha.setError("Senha Fraca, Utilize Numeros e Letras.");
                                    cSenha.requestFocus();
                                } catch (FirebaseAuthInvalidCredentialsException c) {
                                    icadEmail.setError("Email invalido !!");
                                    cEmail.requestFocus();
                                } catch (FirebaseAuthUserCollisionException d) {
                                    icadEmail.setError("Usuario ja existe!!");
                                    cEmail.requestFocus();
                                } catch (Exception e) {

                                }
                            }
                        }


                    });
                }
            }
        });

    }

    //region CaixaDeMensagemConfirmEmail
    /**
     * criando  a caixa de  mensagem para confirmação de email !!
     */
    private void dialogConfirmacaoEmail() {


        AlertDialog.Builder confirm = new AlertDialog.Builder(EmailCadastro.this);
        confirm.setTitle("Confirmação De Conta SmartCity");
                confirm.setIcon(R.mipmap.ic)
                        .setMessage("Email de Confirmação Enviado !" )
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
        AlertDialog alerta = confirm.create();
        alerta.show();

    }
    //endregion

    //region RecebendoDadoS View
    /**
     * passando os dados da View para o Objeto usuario
     * e mostrando erro nos campos caso tenham
     */

    private void recebendoDados() {

            if(cEmail.getText().toString().isEmpty()){
                icadEmail.setError("Email não pode ser vazio");
                cEmail.requestFocus();
            }else if (cNome.getText().toString().isEmpty()){
                icadNome.setError("Nome não pode ser vazio");
                icadNome.requestFocus();
            }else if(cSenha.getText().toString().isEmpty()) {
                icadSenha.setError("Senha não pode ser vazia");
                icadSenha.requestFocus();

            } else {


            userLocal.setEmail(cEmail.getText().toString());
            userLocal.setNome(cNome.getText().toString());
            userLocal.setSenha(cSenha.getText().toString());

        }


    }
    //endregion

    private void limpar() {
        cNome.setText("");
        cEmail.setText("");
        cSenha.setText("");
        icadEmail.setError(null);
        icadNome.setError(null);
        icadSenha.setError(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
