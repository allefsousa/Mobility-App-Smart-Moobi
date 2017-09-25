package developer.allef.smartmobi.smartmobi.View;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobi.R;


public class Sing_in extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth firebaseAuth;
    private GoogleApiClient mGoogleapi;
    public static final int SING_IN_CODE = 777;

    // vetro de permissoes para verificalas em tempo de execução
    private String[] arrayPer = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };


    @BindView(R.id.btnsms)
    Button loginsms;
    @BindView(R.id.btnfacebook)
    Button faceLogin;

    @BindView(R.id.googlebutton)
    SignInButton gmaillogin;

//    @BindView(R.id.btnEmail)
//    Button loginemail;

    AuthCredential authCredential;

    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);
        ButterKnife.bind(this);

        /**
         *opções que voce deseja trazer do usuario junto ao logar na app
         * neste caso apenas o email
         */
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.id_Auth_GoogleServerDEBUGg))
                .requestEmail()
                .build();


        /**
         * a função  abaixo se conecta ao serviço do google para fazer a autenticação
         * e ja passando junto em seu construtor as opções que devem ser trazidas.
         *
         */
        mGoogleapi = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
                .build();


        /**
         * recuperando a instancia do firebase
         */
        firebaseAuth = FirebaseAuth.getInstance();

        /**
         * informando quais dados devem ser trazidos do usuario quando ele
         * logar com o facebook , neste caso trazendo email, dados relacionados ao usuario
         */
    //    faceLogin.setReadPermissions("email");
      //  faceLogin.setReadPermissions(Arrays.asList("email","user_about_me"));

        faceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Sing_in.this, Arrays.asList("email", "public_profile"));

            }
        });

        /**
         * evento de click do Botão de sing in com a conta google
         */
        gmaillogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent para abrir activity para selecionar a conta google a ser usada.
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleapi);
                startActivityForResult(intent,SING_IN_CODE);
            }
        });


        /**
         *
         * rotina realiza o sing out da conta do facebook quando a activity Sing in é chamada
         */
        LoginManager.getInstance().logOut(); // TODO: 16/07/2017  comentado nesta data para evitar estar logado e aparecer deslogado


        /**
         * chamada de callBack para verificar autenticidade do login com facebook
         */
        callbackManager = CallbackManager.Factory.create();
        // chamada de calBack para buscar o token do facebook
LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback< LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handlerFacebookToken(loginResult); // metodo para fazer o login no firebase

            }

            @Override
            public void onCancel() {
                Snackbar.make(findViewById(android.R.id.content), "Erro na Autenticação",
                        Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {


                // TODO: 29/08/2017 tratar exception de erros api facebook 
                
                Snackbar.make(findViewById(android.R.id.content), "Falha na Autenticação" +error.getMessage(),
                        Snackbar.LENGTH_LONG).show();
            }

        }
    );


        /**
         * ouvinte para verificar se o usuario se logou no Firebase
         */
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if(firebaseUser != null){
                    AuthSucess();
//                    Toast.makeText(Sing_in.this,"Usuario logado numero: "+
//                    firebaseUser.getPhoneNumber() +"Nome "+firebaseUser.getDisplayName()+
//                    "email "+firebaseUser.getEmail(),Toast.LENGTH_LONG).show();
                }
            }
        };





        loginsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sing_in.this,PhoneAuthActivity.class));
                finish();
            }
        });

//        loginemail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Sing_in.this,EmailLogin.class));
//                finish();
//            }
//        });

    }

    private void SingInFirebase(AuthCredential cred) {
        firebaseAuth.signInWithCredential(cred).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Snackbar.make(findViewById(android.R.id.content), "Login Realizado Com Sucesso",
                        Snackbar.LENGTH_LONG).show();

            }
        });

            }


    private void AuthSucess() {
        Intent i = new Intent(Sing_in.this,IntroActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    /**
     *
     * @param loginResult
     */
    private void handlerFacebookToken(LoginResult loginResult) {
        authCredential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
        SingInFirebase(authCredential);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

        // verificando  o codigo recebido do GoogleSing
        if(requestCode == SING_IN_CODE ){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handlerGoogleSingResult(result);
        }
    }

    /**
     * metodo responsavel por determinar oque sera feito com o resultado do login
     * @param result
     */
    private void handlerGoogleSingResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            //startActivity(new Intent(Sing_in.this, TestSingInGoogle.class));
            //AuthSucess();
            firebaseAuthsignInGoogle(result.getSignInAccount());

        }else {
            Toast.makeText(Sing_in.this,"Falha ao Iniciar Sessão", Toast.LENGTH_LONG).show();

        }
    }

    private void firebaseAuthsignInGoogle(GoogleSignInAccount signInAccount) {

        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        SingInFirebase(credential);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        /**
         * mensagem de erro ao usuario
         */
    }
}