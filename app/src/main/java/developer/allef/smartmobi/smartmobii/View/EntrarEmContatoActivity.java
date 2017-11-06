package developer.allef.smartmobi.smartmobii.View;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobii.Helper.sendEmail;
import developer.allef.smartmobi.smartmobii.R;

public class EntrarEmContatoActivity extends AppCompatActivity {

    @BindView(R.id.mensagem)
    TextInputEditText mensa;
    @BindView(R.id.email)
    TextInputEditText emaail;
    @BindView(R.id.nome)
    TextInputEditText nome;
    @BindView(R.id.enviar)
    Button send;

    @BindView(R.id.inpEmaiil)
    TextInputLayout iEmail;

    @BindView(R.id.inpmens)
    TextInputLayout iMens;

    @BindView(R.id.inpNomee)
    TextInputLayout iNome;
    private final String email= "appsmartmobi@gmail.com"; // email que vai receber o email enviado
    private final String assunto= "Fale Conosco !!"; // assunto do email
    private String mensagemfinal; // personalizando a mensagem



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar_em_contato);
        ButterKnife.bind(this);
        getSupportActionBar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar);
        getSupportActionBar().setTitle("Entrar em contato");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = EntrarEmContatoActivity.this;
                boolean validaEma;
                validaEma = validaEmail(emaail.getText().toString());
               if (!validaEma){
                   iEmail.setError("Email Invalido ");

               }else if(nome.getText().toString().isEmpty()){
                   iNome.setError("Nome não pode ser Vazio.");

               }else if(mensa.getText().toString().isEmpty()){
                   iMens.setError("Mensagem nao pode ser vazia.");
               }else{
                   mensagemfinal = mensa.getText().toString().concat("\n\n\t\t Nome: "+nome.getText().toString() + "\n\n\t\t Email: "+emaail.getText().toString());
                   sendEmail sd = new sendEmail(context,email,assunto,mensagemfinal);
                   sd.execute();
                   emaail.setText("");
                   mensa.setText("");
                   nome.setText("");

               }


            }
        });



    }
    private  boolean validaEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
