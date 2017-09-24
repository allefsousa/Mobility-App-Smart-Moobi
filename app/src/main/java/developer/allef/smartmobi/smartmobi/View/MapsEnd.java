package developer.allef.smartmobi.smartmobi.View;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobi.Model.LocalVaga;
import developer.allef.smartmobi.smartmobi.R;

public class MapsEnd extends AppCompatActivity {

    @BindView(R.id.btnbuscar)
    Button btnBuscar;

    @BindView(R.id.inpEnd)
    TextInputLayout inputEndere;

    @BindView(R.id.inpNumero)
    TextInputLayout inputNumero;
    @BindView(R.id.enCidade)
    TextInputLayout inpCidade;



    @BindView(R.id.edEndereco)
    EditText txtEndereco;

    @BindView(R.id.edNumero)
    EditText txtNumero;

    @BindView(R.id.listend)
    ListView listView;
    @BindView(R.id.spEstado)
    Spinner estados;
    @BindView(R.id.spstilo)
    Spinner tipoVaga;

    @BindView(R.id.edCidade)
    EditText txtCidade;


    String n;
    Geocoder gc;
    Context context;

    List<Address> list;
    LocalVaga localVaga;
    List<String> Nomerua;
    String Endereco;
    DatabaseReference db;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvagas_maps_end);
         gc = new Geocoder(this,new Locale("pt","BR"));
        ButterKnife.bind(this);
        context = MapsEnd.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEnd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout ddrawer;
        ddrawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle;

        actionBarDrawerToggle = new ActionBarDrawerToggle(MapsEnd.this,ddrawer,toolbar,R.string.drawer_close,R.string.drawer_close);
        ddrawer.setDrawerListener(actionBarDrawerToggle);

      actionBarDrawerToggle.syncState();


        DrawerArrowDrawable drawerArrowDrawable = actionBarDrawerToggle.getDrawerArrowDrawable();
        int colors = ContextCompat.getColor(context,R.color.colorAccent);
        drawerArrowDrawable.setColor(colors);
        actionBarDrawerToggle.setDrawerArrowDrawable(drawerArrowDrawable);








        db = FirebaseDatabase.getInstance().getReference().child("eendereco");



// Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<CharSequence> TipoVagaAdapter = ArrayAdapter.createFromResource(this,
                R.array.TipoOp, android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        TipoVagaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        tipoVaga.setAdapter(TipoVagaAdapter);


        Nomerua = new ArrayList<>();
        localVaga = new LocalVaga();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEndereco.getText().toString().isEmpty()) {
                    inputEndere.setError("");
                    inputEndere.setError("Endereço não pode ser Vazio !");
                } else if (txtNumero.getText().toString().isEmpty()) {
                    inputNumero.setError("");
                    inputNumero.setError("Numero Não pode ser Vazio");
                } else if (txtNumero.getText().toString().isEmpty() && txtEndereco.getText().toString().isEmpty()) {
                    inputEndere.setError("");
                    inputNumero.setError("");
                    inputEndere.setError("Endereço não pode ser Vazio !");
                    inputNumero.setError("Numero Não pode ser Vazio");
                } else if (txtCidade.getText().toString().isEmpty()) {
                    inpCidade.setError("Cidade Não Pode ser Vazia.");

                } else if (estados.getSelectedItemPosition() == 0) {
                    Toast.makeText(MapsEnd.this,"Selecione Um Estado", Toast.LENGTH_LONG).show();

                } else if (tipoVaga.getSelectedItemPosition() == 0) {
                    Toast.makeText(MapsEnd.this,"Selecione Uma Opcão de Vaga", Toast.LENGTH_LONG).show();

                }else{

                    // concatenando o endereço com o numero para formar o endereço de consulta
                    Endereco = txtEndereco.getText().toString() + "," + txtNumero.getText().toString() + txtCidade.getText().toString()+estados.getSelectedItem().toString();

                    try {


                        list = gc.getFromLocationName(Endereco.toString(), 100);// uma lista de Objetos Address recebe todos os Endereços Buscados
                        if (!list.isEmpty()) {// caso a lista nao seja vazia
                            for (int i = 0; i < list.size(); i++) {
                                // adicionando o nome da rua em um array de string
                                Nomerua.add(list.get(i).getAddressLine(0));
                            }

                        }


                    } catch (IOException e) {
                        Nomerua.add("Impossivel Trazer endereços, tente novamente");
                        e.printStackTrace();
                    }
                    n = tipoVaga.getSelectedItem().toString();
                    limpar();
                    adapter = new ArrayAdapter<String>(MapsEnd.this, android.R.layout.simple_expandable_list_item_1, Nomerua);
                    listView.setAdapter(adapter);
                    txtEndereco.requestFocus();
                }
            }
            });




     //    evento de click da listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//

                // pegando os dados do intem precionado para adicionar a nova vaga no BD
               String e= list.get(position).getAddressLine(0);
                localVaga.setLatitude(list.get(position).getLatitude());
                localVaga.setLongitude(list.get(position).getLongitude());
                localVaga.setNomeRua(list.get(position).getFeatureName()); // verificar se é nome da rua

                switch (n){

                    case "Rampa de Acessibilidade":
                        localVaga.setTipoVaga(1);
                        break;
                    case "Vaga Preferencial Deficiente Fisico":

                        localVaga.setTipoVaga(2);
                        break;
                    case "Vaga Preferencial Idoso":
                        localVaga.setTipoVaga(3);
                        break;


                }
                if(localVaga != null){
                    Toast.makeText(MapsEnd.this,"Vaga Adicionada Com Sucesso ", Toast.LENGTH_LONG).show();
                    // adicioando o endereço que foi clicado no listview
                    db.child(e).setValue(localVaga);
                    // limpando o adapter para uma nova busca
                    finish();
                    adapter.clear();
                }







            }
        });


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menuaddvagas, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.localizacao) {
//            startActivity(new Intent(MapsEnd.this,AddVagasLocalizacaoAtual.class));
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    private void limpar() {
        txtNumero.setText("");
        txtEndereco.setText("");
        estados.setSelection(0);
        txtCidade.setText("");
        tipoVaga.setSelection(0);
    }
}
