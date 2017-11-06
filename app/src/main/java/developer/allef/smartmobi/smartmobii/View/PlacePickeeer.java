package developer.allef.smartmobi.smartmobii.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindColor;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobii.Adapters.SpinnerAdapter;
import developer.allef.smartmobi.smartmobii.Model.LocalVaga;
import developer.allef.smartmobi.smartmobii.R;

public class PlacePickeeer extends AppCompatActivity {
    private static final int PLACEPICKER = 1;
    private static final int REQUESTPLACEPICKER = 1;

    GoogleApiClient mApiClient;
    String verificaVaga;
    String KeyvelueBd;
    String qualNo;
    Spinner tipovaga;
    //    Spinner locsalv;
    TextView endereco;
    Button btnsalvarLocal;
    private LocalVaga local;
    private DatabaseReference database;
    @BindColor(R.color.primary_text)
    int preto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        local = new LocalVaga();
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Adicionar Local");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar);

        Button open = (Button) findViewById(R.id.pla);
        tipovaga = (Spinner) findViewById(R.id.stipovaga);
//         locsalv = (Spinner) findViewById(R.id.bdd);
        endereco = (TextView) findViewById(R.id.msg);
        btnsalvarLocal = (Button) findViewById(R.id.placeBsalvar);
        int[] imagens = {0, R.mipmap.rampacinza, R.mipmap.carrozulbranco, R.mipmap.idosoroxo};
        String[] texto = {"Selecione uma Opção", "Rampa de Acessibilidade", "Vaga Preferencial Deficiente Fisico", "Vaga Preferencial Idoso"};
        SpinnerAdapter ada = new SpinnerAdapter(this, texto, imagens);
        endereco.setVisibility(View.INVISIBLE);
        btnsalvarLocal.setVisibility(View.INVISIBLE);
        tipovaga.setVisibility(View.INVISIBLE);


//        ArrayAdapter<CharSequence> VagaAdapter = ArrayAdapter.createFromResource(this,
//                R.array.TipoOp, android.R.layout.simple_spinner_item);
//        VagaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


//        ArrayAdapter<CharSequence> loc = ArrayAdapter.createFromResource(this,
//                R.array.dev, android.R.layout.select_dialog_item);
//        VagaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        locsalv.setAdapter(loc);

        tipovaga.setAdapter(ada);
        tipovaga.setEnabled(true);


        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(PlacePickeeer.this);
                    startActivityForResult(intent, REQUESTPLACEPICKER);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }


            }
        });
        tipovaga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnsalvarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qualNo = "producao";
                if (qualNo != null) {
                    database = FirebaseDatabase.getInstance().getReference().child(qualNo);
                    if (local.getLatitude() != null && local.getLongitude() != null) {


                        verificaVaga = tipovaga.getSelectedItem().toString();
                        switch (verificaVaga) {
                            case "Rampa de Acessibilidade":
                                local.setTipoVaga(1);
                                break;
                            case "Vaga Preferencial Deficiente Fisico":

                                local.setTipoVaga(2);
                                break;
                            case "Vaga Preferencial Idoso":
                                local.setTipoVaga(3);
                                break;
                        }


                        if (!verificaVaga.equals("Selecione uma Opção") && !endereco.getText().toString().isEmpty()) {
                            KeyvelueBd = database.child(qualNo).push().getKey();
                            local.setId(KeyvelueBd);
                            database.child(KeyvelueBd).setValue(local);
                            Snackbar.make(findViewById(android.R.id.content), "Localização salva com sucesso.",
                                    Snackbar.LENGTH_LONG).show();
                            finish();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "O tipo da vaga deve ser selecionado.",
                                    Snackbar.LENGTH_LONG).show();
                        }


                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Impossivel Adicionar, Selecione o local.",
                                Snackbar.LENGTH_LONG).show(); // TODO: 22/08/2017 mudar aqui
                    }
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTPLACEPICKER) {


            if (resultCode == RESULT_OK) {
                endereco.setVisibility(View.VISIBLE);
                btnsalvarLocal.setVisibility(View.VISIBLE);
                tipovaga.setVisibility(View.VISIBLE);
                Place placess = PlacePicker.getPlace(data, this);
                String adress = String.format("Endereço : %s ", placess.getAddress());

                endereco.setText(adress);
                if (local != null) {
                    local.setNomeRua(String.valueOf(placess.getAddress()));
                    local.setLatitude(placess.getLatLng().latitude);
                    local.setLongitude(placess.getLatLng().longitude);
                    local.setStatusVaga("Disponivel");
                }


            }

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        database = FirebaseDatabase.getInstance().getReference().child("producao");


    }
}
