package developer.allef.smartmobi.smartmobi.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobi.Model.LocalVaga;
import developer.allef.smartmobi.smartmobi.R;

public class AddVagasLocalizacaoAtual extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    @BindView(R.id.spTip)
    Spinner listaop;
    @BindView(R.id.btnAddlocalização)
    Button btnadd;
    private static final int REQUEST_ERROR_PLAY_SERVICES = 1;
    private GoogleMap mgoogleMap;
    private GoogleApiClient mgoogleApiClient;
    private static final int Request_chekcar_gps = 2;
    private static final String Extra_Dialog = "dialog";
    private Handler handler;
    private boolean mdeveexibirdialog;
    private int mtentativas;
    private LatLng mOrigen = null;
    Context context;
    private int mSelectedStyleId = R.string.style_label_default;
    private LatLng OriginBD;
    private DatabaseReference db;
    ArrayList<LocalVaga> local;
    Double latitude;
    MapStyleOptions style;
    LocalVaga localV;
    String verificaVaga;
    Double La, lo;
    String hora;


    private static final String SELECTED_STYLE = "selected_style";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvagas_localizacao_atual);
        handler = new Handler();


        InicializaMapa();
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        localV = new LocalVaga();
        db = FirebaseDatabase.getInstance().getReference().child("eendereco");


        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        verificarStatusGPS();

        ArrayAdapter<CharSequence> VagaAdapter = ArrayAdapter.createFromResource(this,
                R.array.TipoOp, android.R.layout.simple_spinner_item);
        VagaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listaop.setAdapter(VagaAdapter);


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TAG = "AdicionadoPorLocalizacaoAtual";
                if (mOrigen.latitude != 0 && mOrigen.longitude != 0) {
                    localV.setLongitude(mOrigen.longitude);
                    localV.setLatitude(mOrigen.latitude);





                    verificaVaga = listaop.getSelectedItem().toString();
                    switch (verificaVaga) {
                        case "Rampa de Acessibilidade":
                            localV.setTipoVaga(1);
                            break;
                        case "Vaga Preferencial Deficiente Fisico":

                            localV.setTipoVaga(2);
                            break;
                        case "Vaga Preferencial Idoso":
                            localV.setTipoVaga(3);
                            break;
                    }
                    Snackbar.make(findViewById(android.R.id.content), "Local Adicionado Com Sucesso.",
                            Snackbar.LENGTH_LONG).show();
                    // adicioando o endereço que foi clicado no listview
                    db.push().setValue(localV);
                    finish();

                }
            }
        });






    }

    @Override
    protected void onStart() {

        super.onStart();
        mgoogleApiClient.connect();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mgoogleApiClient.connect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mgoogleApiClient.connect();

    }

    private void setSelectedStyle() {
        MapStyleOptions style;
        // Sets the night style via raw resource JSON.
        style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style);
        mgoogleMap.setMapStyle(style);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ERROR_PLAY_SERVICES && resultCode == RESULT_OK) {
            mgoogleApiClient.connect();
        } else if (requestCode == Request_chekcar_gps) {
            if (resultCode == RESULT_OK) {
                mtentativas = 0;
                handler.removeCallbacksAndMessages(null);
                // obterUltimaLocalizacao();
            } else {
                Toast.makeText(this, R.string.erro_gps, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onStop() {
        if (mgoogleApiClient != null && mgoogleApiClient.isConnected()) {
            mgoogleApiClient.disconnect();
        }
        // mhandler.removeCallbacksAndMessages(null);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(Extra_Dialog, mdeveexibirdialog);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mdeveexibirdialog = savedInstanceState.getBoolean(Extra_Dialog, true);
    }

    private void InicializaMapa() {

        if (mgoogleMap == null) {
            SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapatual);
            fragment.getMapAsync(this);
//

        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        obterUltimaLocalizacao();
        verificarStatusGPS();

    }

    private void obterUltimaLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mgoogleMap.setMyLocationEnabled(true);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }
        if (myLocation != null) {
            mOrigen = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
//            localV.setLatitude(mOrigen.latitude);
//            localV.setLongitude(mOrigen.longitude);

            mtentativas = 0;
            atualizarMapa();
//
        } else if (mtentativas < 10) {
            mtentativas++;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    obterUltimaLocalizacao();
                }
            }, 2000);
        }

    }


    private void atualizarMapa() {


        mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mOrigen, 15.0f));

//        mgoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mgoogleMap.setMapStyle(style);
        defCorMapa();
        mgoogleMap.clear();

//
        mgoogleMap.addMarker(new MarkerOptions()
                .position(mOrigen)
                .title("Local Atual"));

    }

    private void defCorMapa() {
       // hora = monitorHora.retornoDataHora();
        if (hora != null) {


            String[] obtendoHoraexata;

            obtendoHoraexata = hora.split(":");

            int horainteira = 0;
            horainteira = Integer.parseInt(String.valueOf(obtendoHoraexata[0]));


            if (horainteira > 6 && horainteira < 18) {

                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            } else {
                setSelectedStyle();
            }

        }

    }


    @Override
    public void onConnectionSuspended(int i) {
        mgoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {

            try {
                connectionResult.startResolutionForResult(this, REQUEST_ERROR_PLAY_SERVICES);

            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //inicializando o mapa para modificações
        mgoogleMap = googleMap;
        // mgoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //  mgoogleMap.setMapStyle(style);
        mgoogleMap.getUiSettings().setMapToolbarEnabled(true);
        mgoogleMap.getUiSettings().setAllGesturesEnabled(true);
        defCorMapa();
    }

    private void verificarStatusGPS() {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder locationSettingsRequest = new LocationSettingsRequest.Builder();
        locationSettingsRequest.setAlwaysShow(true);
        locationSettingsRequest.addLocationRequest(locationRequest);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mgoogleApiClient, locationSettingsRequest.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (ActivityCompat.checkSelfPermission(AddVagasLocalizacaoAtual.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddVagasLocalizacaoAtual.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the userLocal grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mgoogleMap.setMyLocationEnabled(true);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        if (mdeveexibirdialog) {
                            try {
                                status.startResolutionForResult(AddVagasLocalizacaoAtual.this, Request_chekcar_gps);
                                mdeveexibirdialog = false;
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();

                            }
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.wtf("NGVL", "iSSO NAO DEVERIA ACONTECER");
                        break;

                }
            }


        });
    }
}
