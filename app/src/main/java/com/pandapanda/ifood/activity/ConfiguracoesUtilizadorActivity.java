package com.pandapanda.ifood.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pandapanda.ifood.R;
import com.pandapanda.ifood.helper.ConfiguracaoFirebase;
import com.pandapanda.ifood.helper.UtilizadorFirebase;
import com.pandapanda.ifood.model.Utilizador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ConfiguracoesUtilizadorActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private EditText editUtilizadorNome, editUtilizadorEndereco;
    private String idUtilizador;
    private DatabaseReference firebaseRef;

    //address+", "+area+", "+city+", "+country+", "+postalcode
    //GeoCode
    TextView textView, textView_Address, textView_Area, textView_City, textView_Country, textView_PostalCode;
    Geocoder geocoder;
    List<Address> addresses;

    //ESTG coordenadas: 41.367147, -8.194802
    //TOURAL GUIMARAES: 41.450795, -8.288537
    Double latitude = 41.450795;                      // -------- isto sem valor inicial pode dar problemas
    Double longitude = -8.288537;                     // -------- isto sem valor inicial pode dar problemas

    //---------------------------------------------------------------------------------------------------------------------------------------- GPS
    private Location location;
    private TextView locationTv;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    //List for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permission results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    //--------------------------------------------------    gps update
    private FusedLocationProviderClient fusedLocationClient;
    //-- testing
    private TextView latitude_string ;
    private TextView longitude_string ;


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_utilizador);

        //-------------------------------------------------------------------------------------------------------------------------------------- GPS.start
        locationTv = findViewById(R.id.textView_location);
        // we add permissions we need to ask users location
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(permissionsToRequest.size() > 0){
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),ALL_PERMISSIONS_RESULT);
            }
        }
        // we create google api cliente
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        //------------------------------------------------------------- GPS update
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //-------------------------------------------------------------------------------------------------------------------------------------- GPS.over

        // Configuracoes Iniciais
        inicializarComponentes();
        idUtilizador = UtilizadorFirebase.getIdUtilizador();
        firebaseRef = ConfiguracaoFirebase.getFirebase();


        //Configuracoes tooblbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar dados do utilizador
        recuperarDadosUtilizador();

        //------------------------------------------------------------------------- GeoCoder
        textView = (TextView) findViewById(R.id.textView_Morada);

        textView_Address = findViewById(R.id.textView_Address);
        textView_Area = findViewById(R.id.textView_Area);
        textView_City = findViewById(R.id.textView_City);
        textView_Country = findViewById(R.id.textView_Country);
        textView_PostalCode = findViewById(R.id.textView_PostalCode);




        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1);
            String address = addresses.get(0).getAddressLine(0);
            String area = addresses.get(0).getLocality();
            String city = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalcode = addresses.get(0).getPostalCode();

            String fullAddress = address+", "+area+", "+city+", "+country+", "+postalcode;
            textView.setText(fullAddress);

            textView_Address.setText(address);
            textView_Area.setText(area);
            textView_City.setText(city);
            textView_Country.setText(country);
            textView_PostalCode.setText(postalcode);



        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //---------------------------------------------------------------------------------------------- GPS.start





    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions){
        ArrayList<String> result = new ArrayList<>();

        for (String perm: wantedPermissions){
            if(!hasPermission(perm)){
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null){
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!checkPlayServices()){
            locationTv.setText("Precisas installar Google Play Services");

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop location updates
        if(googleApiClient != null && googleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, (com.google.android.gms.location.LocationListener) this); //---- AQUI POSSIVEL ERROR
            googleApiClient.disconnect();
        }




    }

    public boolean checkPlayServices(){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if(resultCode != ConnectionResult.SUCCESS){
            if(apiAvailability.isUserResolvableError(resultCode)){
                apiAvailability.getErrorDialog(this,resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            }else{
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            locationTv.setText("Latitude: " + location.getLatitude() + "\nLongitude: "+ location.getLongitude());
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //                                                                                          E AQUI QUE DOU O VALOR DAS MINHAS COORDENADAS
            //-------------------------------------------------------------------------------------------------------------------------------------------- ToDo:
            //-------------------------------------------------------------------------------------------------------------------------------------------- 1- GeoCode relancar
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //-------------------------------------------------------------------------------------------------------------------------------------------- 2- Butao user isto tudo
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------




            // 1- RELANCAR AQUI O GEO CODER COM AS COORDENADAS UPDATED
            // 2- CRIAR BOTAO PARA ESTA OPCAO SER LANCADA








            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------
            //--------------------------------------------------------------------------------------------------------------------------------------------

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case ALL_PERMISSIONS_RESULT:
                for(String perm: permissionsToRequest){
                    if (!hasPermission(perm)){
                        permissionsRejected.add(perm);
                    }
                }
                if(permissionsRejected.size() > 0 ){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if(shouldShowRequestPermissionRationale(permissionsRejected.get(0))){
                            new AlertDialog.Builder(ConfiguracoesUtilizadorActivity.this).setMessage("Estas permissoes sao cruciais. Precisa ceder asp ermissoes localizacao")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]),ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel",null).create().show();
                            return;
                        }
                    }
                }else{
                    if(googleApiClient != null){
                        googleApiClient.connect();
                    }
                }

                break;

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return ;
        }

        // Permissions ok, we get las location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if(location != null){
            locationTv.setText("Latitude: " + location.getLatitude() + "\nLongitude: "+ location.getLongitude());
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude,longitude,1);
                String address = addresses.get(0).getAddressLine(0);
                String area = addresses.get(0).getLocality();
                String city = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalcode = addresses.get(0).getPostalCode();

                String fullAddress = address+", "+area+", "+city+", "+country+", "+postalcode;
                textView.setText(fullAddress);

                textView_Address.setText(address);
                textView_Area.setText(area);
                textView_City.setText(city);
                textView_Country.setText(country);
                textView_PostalCode.setText(postalcode);



            } catch (IOException e) {
                e.printStackTrace();
            }

            //latitude_string.setText("A LATITUDE E "+location.getLatitude());
           // Toast.makeText(this, "ENTROU AQUI",Toast.LENGTH_SHORT).show();
          //  Toast.makeText(this, latitude.toString(),Toast.LENGTH_SHORT).show();
          //  Toast.makeText(this, latitude.toString(),Toast.LENGTH_SHORT).show();

            latitude = location.getLatitude();
        //    longitude = location.getLongitude();
            Toast.makeText(this, latitude.toString(),Toast.LENGTH_SHORT).show();


        }
        startLocationUpdates();
    }

    private void startLocationUpdates(){
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Precisa de ceder as permissoes de localizacao GPS!",Toast.LENGTH_SHORT).show();
        }
        //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest, (com.google.android.gms.location.LocationListener) this);

        //------------------------------------------------------------------------------- GPS tryout start
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });
        //------------------------------------------------------------------------------- gps tryout over


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //---------------------------------------------------------------------------------------------- GPS.over

    public void validarDadosUtilizador(View view){
        // Valida se os campos foram preenchidos
        String nome = editUtilizadorNome.getText().toString();
        String endereco = editUtilizadorEndereco.getText().toString();

        if(!nome.isEmpty()){
            if(!endereco.isEmpty()){
                //Aqui vamos ter que criar uma class com nome e endereco do user la no firebase
                //1- criar model Utilizador (campos: id,nome,endereco + construtor vazio + getrs&setrs
                //2- criar aqui uma instancia nova de utilizador
                //3- criar um metodo gravar(); onde vamos ao model e criamos esse mesmo metodo
                //4- adicionar aqui ainda o exibir mensagem de sucesso + finish();
                //5- AGORA VAMOS FAZER LOAD DOS DADOS PARA O FORMULARIO ATRAVEZ DO FIREBASE (DATABASE-RealTimeDatabase)
                //6- Criar depois de "Configs toolbar" recuperarDadosUtilizador();

                Utilizador utilizador = new Utilizador();
                utilizador.setIdUtilizador(idUtilizador); // em configuracoes iniciais ja demos valor a este id
                utilizador.setNome(nome);
                utilizador.setEndereco(endereco);
                utilizador.gravar();

                exibirMensagem("Dados atualizados com sucesso!");
                finish();



            }else{
                exibirMensagem("Introduza um endereco de entrega!");
            }
        }else{
            exibirMensagem("Introduza um nome de utilizador");
        }



    }

    private void exibirMensagem(String texto){
        Toast.makeText(this,texto,Toast.LENGTH_SHORT)
                .show();
    }


    private void inicializarComponentes(){
        editUtilizadorNome = findViewById(R.id.editUtilizadorNome);
        editUtilizadorEndereco = findViewById(R.id.editUtilizadorEndereco);
    }


    private void recuperarDadosUtilizador(){

        DatabaseReference utilizadorRef = firebaseRef
                .child("utilizadores")
                .child(idUtilizador);

        utilizadorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null ){
                    Utilizador utilizador = dataSnapshot.getValue(Utilizador.class);
                    editUtilizadorNome.setText( utilizador.getNome());
                    editUtilizadorEndereco.setText(utilizador.getEndereco());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

}
