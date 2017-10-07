package developer.allef.smartmobi.smartmobii.View;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaouan.revealator.Revealator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import developer.allef.smartmobi.smartmobii.Helper.monitorHora;
import developer.allef.smartmobi.smartmobii.Model.LocalVaga;
import developer.allef.smartmobi.smartmobii.Model.Usuario;
import developer.allef.smartmobi.smartmobii.PlaceAdress;
import developer.allef.smartmobi.smartmobii.PlacePickeeer;
import developer.allef.smartmobi.smartmobii.R;


public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, DirectionCallback {

    //region Variaveis Globais
    private static final String TAG = "AllefS";
    private static final int Request_chekcar_gps = 2;
    private static final int REQUEST_ERROR_PLAY_SERVICES = 1;
    private static final String Extra_Dialog = "dialog";
    private static final String SELECTED_STYLE = "selected_style";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int NadaFiltrado = 800;
    private final String noBanco = "teste";
    Marker LocAtual;
    int verificaCor;
    BottomSheetBehavior bottomSheetBehavior;
    NestedScrollView nestedScrollView;
    MenuItem menuItemEnd, menuItemLocAtual, sair;
    Context context;
    Info distancia, duracao;
    Leg leg;
    @BindView(R.id.quilometragem)
    TextView km;
    @BindView(R.id.enddescri)
    TextView enderecoloc;
    @BindView(R.id.duracao)
    TextView tempoPercurso;
    @BindView(R.id.establishment_progress)
    ProgressBar progressBar;
    @BindView(R.id.tipov)
    TextView tipoV;
    @BindView(R.id.switch1)
    Switch StatusVaga;
    @BindView(R.id.routePrincipal)
    TextView t1;
    @BindView(R.id.routeAlternativa)
    TextView t2;
    @BindView(R.id.radioPreferencialIdoso)
    CheckBox radipreferencialIdoso;
    @BindView(R.id.radioPreferencialDeficiente)
    CheckBox radideficiente;
    @BindView(R.id.radiorampaa)
    CheckBox radirampa;
    DrawerLayout drawer;
    int ValidadeFiltro = 0;
    Menu myMenu;
    private GoogleMap mgoogleMap;
    private GoogleApiClient mgoogleApiClient;
    private Handler mhandler;
    private boolean mdeveexibirdialog;
    private int mtentativas;
    private LatLng mOrigen;
    private FirebaseAuth mAuth;
    private LatLng OriginBD;
    private DatabaseReference bd;
    private LocalVaga local;
    private String[] colors = {"#d50000", "#7f31c7c5", "#7fff8a00"};
    private int hora;
    private int flagRealtime = 0;
    private FloatingActionButton fab;
    private int corBranco = Color.parseColor("#FFFFFF");
    private int corPreto = Color.parseColor("#000000");
    private int color1 = Color.parseColor("#d50000");
    private Toolbar toolbar;
    private LatLng positionMarker;
    private String keyupdateVaga;
    private String keyUsuarioLogado;
    private Intent getFiltro;
    private Typeface typeface;
    private ArrayList<LocalVaga> arrayLocal = new ArrayList<>();
    private ArrayList<LocalVaga> arrayUpdateStatus = new ArrayList<>();
    private View theAwesomeView;


    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // referencia do Nó do firebase
        bd = FirebaseDatabase.getInstance().getReference().child(noBanco);
        typeface = Typeface.createFromAsset(this.getAssets(), "laconicregular.otf");
        hora = monitorHora.retornoDataHora();


        //region OnCreate  Cores Dia/Noite
        if (hora >= 6 && hora < 18) {
            setTheme(R.style.AppTheme_OverlayToolbar);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu);
            configFabClaro();
            configToolbarClaro();

        } else {
            // mapa escuro icones brancos
            setTheme(R.style.AppTheme_OverlayToolbar_);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu);
            configFabEscuro();
            configToolbarClaro();

        }
        //endregion


        context = MenuActivity.this;
        mAuth = FirebaseAuth.getInstance();

        ButterKnife.bind(this);


        bd = FirebaseDatabase.getInstance().getReference().child(noBanco);
        bd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    local = d.getValue(LocalVaga.class);
                    arrayLocal.add(local);

                    if (flagRealtime > 0) {
                        mgoogleMap.clear();
                        addmMarker();

                    }
                }


                flagRealtime += 1;


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        if (savedInstanceState != null) {
            int mSelectedStyleId = savedInstanceState.getInt(SELECTED_STYLE);
        }
        mdeveexibirdialog = savedInstanceState == null;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        //region Onclick CheckBox Filtro
        radideficiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxClick(view);
            }
        });
        radipreferencialIdoso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxClick(view);
            }
        });

        radirampa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxClick(view);
            }
        });
        //endregion

        //region Filtro Layout/ Efeitos
        theAwesomeView = findViewById(R.id.the_awesome_view);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.reveal(theAwesomeView)
                        .from(fab)
                        .withCurvedTranslation()
                        .withChildsAnimation()
                        .start();
                fab.hide();
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (myMenu != null) {
                    myMenu.findItem(R.id.action_search).setEnabled(false);
                }


            }
        });

        final View theWonderfulButton = findViewById(R.id.the_wonderful_button);
        theWonderfulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.unreveal(theAwesomeView)
                        .to(fab)
                        .withCurvedTranslation().start();
//                        .withUnrevealDuration(...)
//                        .withTranslateDuration(...)
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                //fab.show();
//                                Snackbar.make(fab, "What a beautiful snackbar !", Snackbar.LENGTH_LONG).show();
//                            }
//                        })
//                        .start();

                atualizarMapa();
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                myMenu.findItem(R.id.action_search).setEnabled(true);
                fab.show();

            }
        });
        //endregion


        //region AuthGoogleAPI
        /**
         *opções que voce deseja trazer do usuario junto ao logar na app
         * neste caso apenas o email
         */
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        //endregion


        mhandler = new Handler();

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        fragment.getMapAsync(this);

        InicializaMapa();
        atualizaDadosOffline();


        verificarStatusGPS();


        drawer.setEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /**
         * Listener para gerenciar a abertura e o fechamaneto da Navigation Drawer
         * nesse caso utilizado para esconder e exibir o botao de Filtrar
         */
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                fab.hide();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                fab.show();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // get menu from navigationView
        Menu menu = navigationView.getMenu();

        menuItemEnd = menu.findItem(R.id.nav_camera);
        menuItemLocAtual = menu.findItem(R.id.navmapslvagas);
        sair = menu.findItem(R.id.singout);


        /**
         * Callback para iniciar a Buttomsheet
         */
        initBottomSheet();

    }


    private void checkBoxClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {

            case R.id.radioPreferencialIdoso:
                if (checked) {

                    ValidadeFiltro = ValidadeFiltro + 100;

                } else {
                    ValidadeFiltro = ValidadeFiltro - 100;


                }
                break;

            case R.id.radioPreferencialDeficiente:
                if (checked) {

                    ValidadeFiltro = ValidadeFiltro + 200;

                } else {
                    ValidadeFiltro = ValidadeFiltro - 200;


                }
                break;

            case R.id.radiorampaa:
                if (checked) {
                    ValidadeFiltro = ValidadeFiltro + 500;

                } else {
                    ValidadeFiltro = ValidadeFiltro - 500;

                }
                break;

        }
    }


    private void configFabClaro() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_filterbranco);
        fab.setBackgroundTintList(ColorStateList.valueOf(corPreto));
        fab.setRippleColor(color1);
    }

    private void configFabEscuro() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_filter);
        fab.setBackgroundTintList(ColorStateList.valueOf(corBranco));
        fab.setRippleColor(color1);
    }

    private void configToolbarClaro() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Smart Mobi");
        ((TextView) toolbar.getChildAt(0)).setTypeface(typeface); // passando o typeface da font para a toolbar assim mudando a font
        if (hora >= 6 && hora < 18) {
            toolbar.setTitleTextColor(corPreto);
        } else {
            toolbar.setTitleTextColor(corBranco);
        }

    }


    private void disponibilidadeVagaDataBase() {
        DatabaseReference data;


        if (keyupdateVaga != null) {
            // FIXME: 27/08/2017 mudar referencia do no quando mudar o banco 
            data = FirebaseDatabase.getInstance().getReference().child(noBanco).child(keyupdateVaga);
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    local = dataSnapshot.getValue(LocalVaga.class);
                    arrayUpdateStatus.add(local);


                    if (local.getStatusVaga().equals(keyUsuarioLogado) || local.getStatusVaga().equals("Disponivel")) {
                        StatusVaga.setEnabled(true);
                        // FIXME: 10/09/2017 AGORAAAA mgoogleMap.addMarker(new MarkerOptions()

                    } else {
                        StatusVaga.setEnabled(false);
                    }


//                    Toast.makeText(context, "Nos1 " + local.getStatusVaga() + "  " + arrayUpdateStatus.size(), Toast.LENGTH_SHORT).show();
                    // TODO: 26/08/2017 funcionando em tempo real com certo delay da rede.


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            // TODO: 27/08/2017  se for igual a do usuario logado deixa visivel
            /**
             * se for em br54anco ou nulo deixa visivel , agora se for diferente de nulo e do usuario logado disabilita
             */


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        myMenu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu, menu);

        if (hora >= 6 && hora < 18) {

            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_search));
        } else {

            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_searchbranco));


        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:

                openPlacesAddress();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openPlacesAddress() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services Não Esta Disponivel " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }


    private void initBottomSheet() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollView);

        // altura inicial do botão de detales
        bottomSheetBehavior.setPeekHeight(0);
        // dizendo que o botão nao pode iniciar estando aberto
        bottomSheetBehavior.setHideable(false);


        if (bottomSheetBehavior != null) {
            // listner de estado do botão de detales (Aberto Fechado oculto)
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    // passando a altura novamente para zero para que ele feche por completo
                    bottomSheetBehavior.setPeekHeight(0);


                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:

                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            disponibilidadeVagaDataBase();


                            StatusVaga.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    // TODO: 11/09/2017 criar uma nova instancia de bd para ver se otimiza o tempo de marcação da vaga 

                                    if (b) {
                                        for (LocalVaga l : arrayLocal) {

                                            if (l.getId().equals(keyupdateVaga)) {


                                                l.setStatusVaga(keyUsuarioLogado);


                                                Map<String, Object> updateVaga = new HashMap<>();
                                                updateVaga.put(l.getId(), l);
                                                // mudar foto dizendo que a vaga esta ocupada


                                                bd.updateChildren(updateVaga, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        vagaIndisponivel();
                                                    }
                                                });

                                            }
                                        }

                                    } else {
                                        for (LocalVaga l : arrayLocal) {


                                            if (l.getId().equals(keyupdateVaga)) {

                                                l.setStatusVaga("Disponivel");
                                                // voltar a foto dizendo que esta disponivel


                                                Map<String, Object> updateVaga = new HashMap<>();
                                                updateVaga.put(l.getId(), l);


                                                bd.updateChildren(updateVaga).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        atualizarMapa();
                                                    }
                                                });


                                            }
                                        }

                                    }

                                }
                            });


                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:

                            fab.show(); // exibindo o botão de pesquisa

                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:


                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }


                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }

            });

        }


    }

    private void vagaIndisponivel() {

        for (LocalVaga v : arrayLocal){
            if(v.getId() == keyupdateVaga){

            }
        }

    }


    /**
     * metodo responsavel por fazer a requisição das direções para posteriormente
     * serem exibidas no mapa.
     * Utilizando uma Api de terceiros onde a documentação se encontra no
     * SITE:http://www.akexorcist.com/2015/12/google-direction-library-for-android-en.html ou no
     * GITHUB: https://github.com/akexorcist/Android-GoogleDirectionLibrary
     * Para obter as direções é necessarios a chave do servidor da google para backEnd , duas localizações nesse caso a
     * que o usuario esta  que é o mOrigem e que o usuario que saber a distancia e ver as rotas que é a OrigenBD
     * e necessario passar a unidade de medida ,idioma ,tipo de transporte (Carro ,onibus ,bicilceta, etc) para que
     * gere a rota e o tempo adequado e por ultimo se você quer exibir rotas alternativas.
     */
    public void requestDirection() {


        if (mOrigen.latitude == OriginBD.latitude && mOrigen.longitude == OriginBD.longitude) {
        } else {

            GoogleDirection.withServerKey("AIzaSyAJpvp5z8zwkAYrkooT_0-5iUwEfBQUNtE")
                    .from(mOrigen)
                    .to(OriginBD)
                    .unit(Unit.METRIC)
                    .language(Language.PORTUGUESE_BRAZIL)
                    .transportMode(TransportMode.DRIVING)
                    .alternativeRoute(true)
                    .execute(this);
        }


    }


    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {


            for (Route a : direction.getRouteList()) {
                a.getLegList().get(0).getDirectionPoint();
            }

            for (int i = 0; i < direction.getRouteList().size(); i++) {
                Route route = direction.getRouteList().get(i);
                String color = colors[i % colors.length];
                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                leg = route.getLegList().get(0);
                distancia = leg.getDistance();
                duracao = leg.getDuration();
                mgoogleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.parseColor(color)));
            }

            progressBar.setProgress(100);
            progressBar.setVisibility(View.GONE);
            km.setText("Distancia :" + distancia.getText());
            tempoPercurso.setText("Tempo Duração:" + duracao.getText());


        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(findViewById(android.R.id.content), "Impossivel Exibir Rotas, Verifique sua Conexão com a Internet.",
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {
            drawer.closeDrawers();
            // chamando outra tela quando um botão do menu é precionado
            startActivity(new Intent(context, PlacePickeeer.class));

        } else if (id == R.id.navmapslvagas) {
            startActivity(new Intent(context, FeedActivity.class));

        } else if (id == R.id.config) {
            startActivity(new Intent(context, PlaceAdress.class));

        } else if (id == R.id.entrarcontato) {

        } else if (id == R.id.sobre) {


        } else if (id == R.id.politica) {
            Intent i = new Intent(context, webViewPoliticaActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

        } else if (id == R.id.singout) {
            startActivity(new Intent(context, Sing_in.class));
            mAuth.signOut();
            finish();

            Auth.GoogleSignInApi.signOut(mgoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {

                    } else {
                        //mensagem de erro ao disconectar e revogar
                    }
                }
            });
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void verificarStatusGPS() {

        LocationRequest locationRequest = LocationRequest.create();
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
                        //            obterUltimaLocalizacao(); // descomentei 04/06


                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        if (mdeveexibirdialog) {
                            try {
                                status.startResolutionForResult(MenuActivity.this, Request_chekcar_gps);
                                mdeveexibirdialog = true;

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


    @Override
    protected void onStart() {

        super.onStart();
        mgoogleApiClient.connect();
        mAuth = FirebaseAuth.getInstance();
        getFiltro = getIntent();


        /**
         * pegando os objetos visuais foto nome e email para pesonalizar com o login
         */
        NavigationView navigationVieww = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationVieww.getHeaderView(0);
        TextView nav_user = hView.findViewById(R.id.navName);
        TextView nav_email = hView.findViewById(R.id.navemail);
        CircleImageView perfil = hView.findViewById(R.id.profile_image);


        hora = monitorHora.retornoDataHora();

        if (hora >= 6 && hora < 18) {
            //  mapa claro Icones pretos
            hView.setBackgroundResource(R.drawable.side_nav_bar_branco);
            nav_user.setTextColor(corPreto);
            nav_email.setTextColor(corPreto);
        } else {
            // mapa escuro icones brancos
            hView.setBackgroundResource(R.drawable.side_nav_bar);

        }


        // objetos para verificar provedor que se logou
        List<String> providers;
        if (mAuth.getCurrentUser() != null) {
            providers = mAuth.getCurrentUser().getProviders();
            keyUsuarioLogado = mAuth.getCurrentUser().getUid();

            /**
             * Salvando usuario
             */
            DatabaseReference dataUser;
            dataUser = FirebaseDatabase.getInstance().getReference().child("usuarios");
            Usuario userLogado = new Usuario();
            userLogado.setIdUsuario(mAuth.getCurrentUser().getUid());
            userLogado.setNome(mAuth.getCurrentUser().getDisplayName());
            userLogado.setEmail(mAuth.getCurrentUser().getEmail());
            userLogado.setUrlPhto(mAuth.getCurrentUser().getPhotoUrl().toString());
            dataUser.child(keyUsuarioLogado).setValue(userLogado);

            /**
             * percorrendo a lista de provedores e mudando conforme cada metodo.
             */
            for (String pro : providers) {
                if (pro.equals("phone")) {
                    nav_user.setText(mAuth.getCurrentUser().getDisplayName());
                    nav_email.setText(mAuth.getCurrentUser().getPhoneNumber());
                    perfil.setImageResource(R.drawable.facebook);
                } else if (pro.equals("password")) {
                    nav_user.setText(mAuth.getCurrentUser().getDisplayName());
                    nav_email.setText(mAuth.getCurrentUser().getEmail());
                    perfil.setImageResource(R.drawable.facebook);

                } else {
                    Picasso.with(MenuActivity.this).load(mAuth.getCurrentUser().getPhotoUrl()).into(perfil);
                    nav_user.setText(mAuth.getCurrentUser().getDisplayName());
                    nav_email.setText(mAuth.getCurrentUser().getEmail());
                }
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mgoogleApiClient.connect();
        mAuth = FirebaseAuth.getInstance();


    }


    private void setSelectedStyle(int i) {
        MapStyleOptions style = null;
        // Sets the night style via raw resource JSON.
        /**
         * selecionando o tipo de mapa a ser exibido as cores dos mapas com base nos arquivos JSON
         */

        switch (i) {
            case 1:
                style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_stylebranco);

                break;
            case 0:
                style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_dark);

                break;
        }
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
                mhandler.removeCallbacksAndMessages(null);
                obterUltimaLocalizacao();
            } else {
                Toast.makeText(this, R.string.erro_gps, Toast.LENGTH_LONG).show();
                finish();
            }
        }

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16.0f));
                mgoogleMap.addMarker(new MarkerOptions().position(place.getLatLng())
                        .title("Endereço Pesquisado"));


//                // Format the place's details and display them in the TextView.
//                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
//                        place.getId(), place.getAddress(), place.getPhoneNumber(),
//                        place.getWebsiteUri()));
//
//                // Display attributions if required.
//                CharSequence attributions = place.getAttributions();
//                if (!TextUtils.isEmpty(attributions)) {
//                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    mPlaceAttribution.setText("");
//                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }


    }

    @Override
    protected void onStop() {
        if (mgoogleApiClient != null && mgoogleApiClient.isConnected()) {
            mgoogleApiClient.disconnect();
        }
        mhandler.removeCallbacksAndMessages(null);

        super.onStop();
    }


    private void InicializaMapa() {

        if (mgoogleMap == null) {
            SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
            fragment.getMapAsync(this);


        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        obterUltimaLocalizacao();

    }

    private void obterUltimaLocalizacao() { // TODO: 12/08/2017  CORRIGIR VERIFICAÇÃO DE GPS QUANDO INICIA O APP 
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mgoogleMap.setMyLocationEnabled(true);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    if (location.getLatitude() != mOrigen.latitude && location.getLongitude() != mOrigen.longitude) {
                        if (LocAtual != null) {
                            LocAtual.remove();
                        }

                        mOrigen = new LatLng(location.getLatitude(), location.getLongitude());
                        LocAtual = mgoogleMap.addMarker(new MarkerOptions().position(mOrigen)
                                .title("Local Atual"));

                    }


                }

            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }
        });
        Location myLocation;
        myLocation = mgoogleMap.getMyLocation();

        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }
        if (myLocation != null) {
            mOrigen = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

            mtentativas = 0;
            atualizarMapa();
        } else if (mtentativas < 10) {
            mtentativas++;
            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    obterUltimaLocalizacao();
                }
            }, 2000);
        }


    }


    private void atualizarMapa() {
        defCorMapa();
        mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mOrigen, 16.0f));
        mgoogleMap.clear();
        addmMarker();
//        mgoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                Toast.makeText(context, "Olha noia akii", Toast.LENGTH_SHORT).show();
//            }
//        });

        mgoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                atualizarMapa();
                positionMarker = marker.getPosition();

                // RECUPERANDO O ID  DA VAGA CLICADA PARA VERIFICAR A DISPONIBILIDADE
                for (LocalVaga aa : arrayLocal) {
                    LatLng cordenadas = new LatLng(aa.getLatitude(), aa.getLongitude());
                    if (cordenadas.equals(positionMarker)) {
                        keyupdateVaga = aa.getId();
                        Toast.makeText(context, "keeey" + keyupdateVaga, Toast.LENGTH_LONG).show();

                    }
                }

                if (marker.getSnippet() != null) { // validando o clik no  indicador da Localização

                    switch (marker.getSnippet()) {
                        case "idoso":
                            tipoV.setText("Vaga Preferencial Idoso");
                            StatusVaga.setVisibility(View.VISIBLE);
                            break;
                        case "rampa":
                            tipoV.setText("Rampa Acessivel");
                            StatusVaga.setVisibility(View.INVISIBLE);
                            break;
                        case "vaga":
                            tipoV.setText("Vaga Automovel Preferencial");
                            StatusVaga.setVisibility(View.VISIBLE);
                            break;
                    }

                    boolean isOn;
                    isOn = verificaConexao();

                    if (isOn) {
                        OriginBD = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(50);
                        km.setText("Distancia :");
                        tempoPercurso.setText("Tempo Duração :");
                        fab.hide();
                        bottomSheetBehavior.setPeekHeight(115);
                        requestDirection();
                        return true;
                    } else {


                    }
                }


                return false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread aguardaProgressButton = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;

                    //

                    while (waited < 2800) {

                        sleep(100);
                        waited += 100;


                    }

                    obterUltimaLocalizacao();


                } catch (Exception e) {

                }


            }
        };
        aguardaProgressButton.start();


    }


    private void addmMarker() {
        int retorno = ValidadeFiltro; // retorna valor do filtro quando selecionado

        if (!arrayLocal.isEmpty()) {
            mgoogleMap.clear();


            for (int i = 0; i < arrayLocal.size(); i++) {


                if (arrayLocal.get(i).getTipoVaga() == 1) {

                    if (retorno == 500 || retorno == 600 || retorno == NadaFiltrado || retorno == 700 || retorno == 0) {
                        mgoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(arrayLocal.get(i).getLatitude(), arrayLocal.get(i).getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.rampacinza))
                                .title("Rampa Acessivel").snippet("rampa"));

                    }


                } else if (arrayLocal.get(i).getTipoVaga() == 2) {
                    if (retorno == 200 || retorno == 300 || retorno == NadaFiltrado || retorno == 700 || retorno == 0) {

                        if (!arrayLocal.get(i).getStatusVaga().equals("Disponivel")) {
                            mgoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(arrayLocal.get(i).getLatitude(), arrayLocal.get(i).getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.vagaindisponivel))
                                    .title("Vaga Automovel Preferencial").snippet("vaga"));
                        } else {
                            mgoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(arrayLocal.get(i).getLatitude(), arrayLocal.get(i).getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.carrozulbranco))
                                    .title("Vaga Automovel Preferencial").snippet("vaga"));
                        }


                    }


                } else if (arrayLocal.get(i).getTipoVaga() == 3) {
                    if (retorno == 100 || retorno == 600 || retorno == NadaFiltrado || retorno == 300 || retorno == 0) {


                        if (!arrayLocal.get(i).getStatusVaga().equals("Disponivel")) {
                            mgoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(arrayLocal.get(i).getLatitude(), arrayLocal.get(i).getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.idosoindisponivel))
                                    .title("Vaga Preferencial Idoso").snippet("idoso"));

                        } else {

                            mgoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(arrayLocal.get(i).getLatitude(), arrayLocal.get(i).getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.idosoroxo))
                                    .title("Vaga Preferencial Idoso").snippet("idoso"));
                        }
                    }

                }


            }


        }
    }


    public boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    private void atualizaDadosOffline() {
        boolean connectionStatus;
        connectionStatus = verificaConexao();
        if (connectionStatus) {
            DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference().child(noBanco);
            scoresRef.keepSynced(true);
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
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;
        mgoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mgoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mgoogleMap.getUiSettings().setCompassEnabled(false);

        mgoogleMap.getUiSettings().setAllGesturesEnabled(true);

        defCorMapa();


    }

    private void defCorMapa() {
        hora = monitorHora.retornoDataHora();


        if (!verificaConexao()) { // verificando a conexao com a internet caso nao tenha ..
            // exibe o ultimo mapa visto online
            switch (verificaCor) {
                case 1:
                    setSelectedStyle(verificaCor);
                    break;
                case 0:
                    setSelectedStyle(verificaCor);
                    break;
            }
        } else {


            if (hora >= 6 && hora < 18) {

                verificaCor = 1;
                setSelectedStyle(verificaCor);

            } else {

                verificaCor = 0;
                setSelectedStyle(verificaCor);

            }
        }


    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mOrigen = new LatLng(location.getLatitude(), location.getLongitude());
            atualizarMapa();
        }

    }

    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(Extra_Dialog, mdeveexibirdialog);
    }


}
