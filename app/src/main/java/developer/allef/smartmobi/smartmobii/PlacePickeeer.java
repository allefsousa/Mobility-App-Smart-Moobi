package developer.allef.smartmobi.smartmobii;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobii.Model.LocalVaga;

public class PlacePickeeer extends AppCompatActivity {
    private static final int PLACEPICKER = 1;
    private static final int REQUESTPLACEPICKER = 1;
    Place place;
    @BindView(R.id.pla)
    Button open;

    @BindView(R.id.stipovaga)
    Spinner TipVa;

    @BindView(R.id.bdd)
    Spinner localsalvar;

    @BindView(R.id.msg)
    TextView address;

    @BindView(R.id.placeBsalvar)
    Button salvarVaga;
    GoogleApiClient mApiClient;

    private LocalVaga local;

    String verificaVaga;

    private DatabaseReference database;
    String KeyvelueBd;

    String qualNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        local = new LocalVaga();

        ButterKnife.bind(this);



        ArrayAdapter<CharSequence> VagaAdapter = ArrayAdapter.createFromResource(this,
                R.array.TipoOp, android.R.layout.simple_spinner_item);
        VagaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<CharSequence> loc = ArrayAdapter.createFromResource(this,
                R.array.dev, android.R.layout.select_dialog_item);
        VagaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        TipVa.setAdapter(VagaAdapter);
        localsalvar.setAdapter(loc);


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

        salvarVaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qualNo = localsalvar.getSelectedItem().toString();
                if(qualNo != null){
                    database = FirebaseDatabase.getInstance().getReference().child(qualNo);
                    if (local.getLatitude() != null && local.getLongitude() != null) {


                        verificaVaga = TipVa.getSelectedItem().toString();
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
                        if(!verificaVaga.equals("Selecione Uma Opção.")){
                            KeyvelueBd = database.child(qualNo).push().getKey();
                            local.setId(KeyvelueBd);
                            database.child(KeyvelueBd).setValue(local);
                            Snackbar.make(findViewById(android.R.id.content), "Localização salva com sucesso.",
                                    Snackbar.LENGTH_LONG).show();
                            finish();
                        }else {
                            Snackbar.make(findViewById(android.R.id.content), "O tipo da vaga deve ser selecionado.",
                                    Snackbar.LENGTH_LONG).show();
                        }



                    }else{
                        Snackbar.make(findViewById(android.R.id.content), "Impossivel Exibir Rotas, Verifique sua Conexão com a Internet.",
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
                place = PlacePicker.getPlace(data, this);
                String adress = String.format("Endereço : %s ", place.getAddress());
                address.setText(adress);
                if(local!= null){
                    local.setNomeRua(String.valueOf(place.getAddress()));
                    local.setLatitude(place.getLatLng().latitude);
                    local.setLongitude(place.getLatLng().longitude);
                    local.setStatusVaga("Disponivel");
                }




            }

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        database = FirebaseDatabase.getInstance().getReference().child(localsalvar.getSelectedItem().toString());


    }
}
