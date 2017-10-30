package developer.allef.smartmobi.smartmobii.View;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private final String email= "appsmartmobi@gmail.com"; // email que vai receber o email enviado
    private final String assunto= "Fale Conosco !!"; // assunto do email
    private String mensagemfinal; // personalizando a mensagem



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar_em_contato);
        ButterKnife.bind(this);
        getSupportActionBar();
        getSupportActionBar().setTitle("Entre em Contato com agente !");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = EntrarEmContatoActivity.this;
                mensagemfinal = mensa.getText().toString().concat("\n\n\t\t Nome: "+nome.getText().toString() + "\n\n\t\t Email: "+emaail.getText().toString());

                sendEmail sd = new sendEmail(context,email,assunto,mensagemfinal);
                sd.execute();

                emaail.setText("");
                mensa.setText("");
                nome.setText("");

            }
        });

    }
}
