package developer.allef.smartmobi.smartmobii.View;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.firebase.perf.metrics.Trace;

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobii.R;


public class SplashActivity extends AppCompatActivity {
    Thread splashTread;
    @BindView(R.id.rel)
    RelativeLayout re;
    @BindView(R.id.titlesplash)
    TextView sph;
    @BindView(R.id.imageViewlogo)
    ImageView logo;
    FirebaseUser firebaseUser;
    @BindView(R.id.sph)
    ImageView ima;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    // instancia do Firebase Performase
    @Override
    @AddTrace(name = "onCreateTrace", enabled = true/*Optional*/)


    /**
     * meto criado junto com a viem
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // annotation processor para diminuição do codigo
        ButterKnife.bind(this);


        // instacia da ferramenta de erros
        // instancia do firebase
        firebaseAuth = FirebaseAuth.getInstance();


        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "laconicregular.otf");
        sph.setTypeface(typeface);



        Trace myTrace = FirebasePerformance.getInstance().newTrace("trace_Splash_TEMPO");
        myTrace.start();


        /**
         * Listner responsavel por verificar se o usuario esta autenticado ou nao.
         */
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Pegando a instancia do usuario caso ele esteja logado
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            }
        };

        startAnimation();


    }


    // tempo de pausa da Splsh Screem
    public void startAnimation() {


        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;

                    //

                    while (waited < 3000) {

                        sleep(100);
                        waited += 100;


                    }
                    /**
                     * caso o usuario esteja logado é enviado para a tela de menu , caso nao esteja precisara fazer o login e é direcionado para tela.
                     */
                    if (firebaseUser != null) {
                        Intent i = new Intent(SplashActivity.this, MenuActivity.class); // TODO: 12/07/2017 preciso encontrar uma forma da animação para para nao pular a permisão do GPs pode ser considerado mudar a validação para a classe de login
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(SplashActivity.this, Sing_in.class); // TODO: 12/07/2017 preciso encontrar uma forma da animação para para nao pular a permisão do GPs pode ser considerado mudar a validação para a classe de login
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    }

                } catch (Exception e) {

                }


            }
        };
        splashTread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // adicionando o ouvinte de autenticação
        firebaseAuth.addAuthStateListener(authStateListener);


    }

    @Override
    protected void onResume() {
        super.onResume();
        // removendo o ouvinte de Autenticação
        firebaseAuth.addAuthStateListener(authStateListener);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startAnimation();
        firebaseAuth.addAuthStateListener(authStateListener);


    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }


}
