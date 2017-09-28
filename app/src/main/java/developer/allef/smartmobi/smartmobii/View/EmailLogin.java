package developer.allef.smartmobi.smartmobii.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobii.Model.Usuario;
import developer.allef.smartmobi.smartmobii.R;

public class EmailLogin extends AppCompatActivity {

    @BindView(R.id.inpEmail)
    TextInputLayout iEmai;
    @BindView(R.id.inpSenha)
    TextInputLayout iSenh;
    @BindView(R.id.edtEmail)
    EditText temail;
    @BindView(R.id.edtSenha)
    EditText tsenha;

    @BindView(R.id.criarcontaEmail)
    TextView criarConta;

    @BindView(R.id.btnlogin)
    CircularProgressButton btLoga;
    int a = 0;
    FirebaseAuth fire;

    private FirebaseAuth.AuthStateListener mAuthListener;
    Usuario localUsuer = new Usuario();
    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    Thread aguardaProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        ButterKnife.bind(this);
        mAuth= FirebaseAuth.getInstance();




        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {

                    awaitAnimation();

                } else if (user != null && user.isEmailVerified() == false){

                    Toast.makeText(EmailLogin.this,"E Necessario a confirmação do email para proseguir !! ", Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                }
                // ...
            }
        };
        criarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailLogin.this, EmailCadastro.class));
            }
        });
        btLoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recdados();

                if (localUsuer.getEmail()!= null && localUsuer.getSenha()!=null) {
                    btLoga.setIndeterminateProgressMode(true);
                    btLoga.setProgress(70);




                    mAuth.signInWithEmailAndPassword(localUsuer.getEmail().toString().trim(), localUsuer.getSenha().toString().trim())
                            .addOnCompleteListener(EmailLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        btLoga.setProgress(100);
                                    } else {
                                        Toast.makeText(EmailLogin.this, " ERROO " + task.getException() + localUsuer.getEmail().toString().trim(), Toast.LENGTH_LONG).show();


                                        try {
                                           throw  task.getException();
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            Snackbar.make(findViewById(android.R.id.content), "Email e Senha Invalidos.",
                                                    Snackbar.LENGTH_SHORT).show();
                                            temail.requestFocus();
                                        }catch (FirebaseNetworkException a){

                                        }catch (FirebaseAuthInvalidUserException b){

                                        }catch (FirebaseAuthUserCollisionException c){

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        btLoga.setProgress(-1);

                                    }

                                }

                            });
                }


                }



        });


    }
    private void awaitAnimation() {

        aguardaProgressButton = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;

                    //

                    while (waited < 2800) {

                        sleep(100);
                        waited += 100;


                    }

                    Intent i = new Intent(EmailLogin.this, MenuActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    finish();



                } catch (Exception e) {

                }


            }
        };
        aguardaProgressButton.start();
    }

    private void recdados() {


        if (temail.getText().toString().equals(null)){
            iEmai.setError("Email Não pode ser vazio.");
        } else if (tsenha.getText().toString().equals(null)){
            iSenh.setError("Senha Não pode ser vazia.");
        } else if (tsenha.getText().toString().isEmpty() && tsenha.getText().toString().isEmpty()){
            iSenh.setError("Senha Não pode ser vazia.");
            iEmai.setError("Email Não pode ser vazio.");
        }else {
            localUsuer.setEmail(temail.getText().toString().trim());
            localUsuer.setSenha(tsenha.getText().toString().trim());
        }

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
