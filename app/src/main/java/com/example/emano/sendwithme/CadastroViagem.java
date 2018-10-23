package com.example.emano.sendwithme;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

public class CadastroViagem extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_viagem);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map4);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng recife = new LatLng(-8.05428, -34.8813);

        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(10).target(recife).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }



    public void salvarViagem(View view){

        EditText tinicio = (EditText) findViewById(R.id.editInicioViagem);
        EditText tfim = (EditText) findViewById(R.id.editFimViagem);
        String inicio = tinicio.getText().toString();
        String fim = tfim.getText().toString();

        if(inicio.isEmpty()){
            tinicio.setError("Campo em branco");
        }else if(fim.isEmpty()){
            tfim.setError("Campo em branco");

        }else {

            Viagem viagem = new Viagem();
            viagem.setInicio(inicio);
            viagem.setFim(fim);
            viagem.setIdUsuario(user.getUid());

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Viagens");

            databaseReference.push().setValue(viagem);

            Intent intent = new Intent(getApplicationContext(), HomeDrawerActivity.class);
            startActivity(intent);

            Toast.makeText(getApplicationContext(), "Viagem salva", Toast.LENGTH_LONG).show();
        }

    }

    public void buscarViagem(View view){

        EditText torigem = (EditText)findViewById(R.id.editInicioViagem);
        EditText tdestino = (EditText)findViewById(R.id.editFimViagem);
        String origem = torigem.getText().toString();
        String destino = tdestino.getText().toString();


        List<Address> addressesList=null;
        List<Address>addressesList2=null;
        MarkerOptions mo = new MarkerOptions();
        MarkerOptions mo2 = new MarkerOptions();

        if(!origem.equals("")){

            Geocoder geocoder = new Geocoder(this);
            try{
                addressesList= geocoder.getFromLocationName(origem,5);
                addressesList2= geocoder.getFromLocationName(destino,5);
            }catch (IOException e){
                e.printStackTrace();
            }

            for(int i = 0;i<addressesList.size();i++){

                Address myadres = addressesList.get(i);
                Address myadres2 = addressesList2.get(i);

                LatLng latLng= new LatLng(myadres.getLatitude(),myadres.getLongitude());
                LatLng latLng2= new LatLng(myadres2.getLatitude(),myadres2.getLongitude());
                mo.position(latLng);
                mo2.position(latLng2);

                //Adicionamos um marcador no ponto de partida e no ponto de destino
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.addMarker(new MarkerOptions().position(latLng2));

                //Animação para visualizar ponto de partida
                CameraPosition updateRota = new CameraPosition(latLng, 15, 0, 0);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(updateRota), 3000, null);

            }


        }

    }


}
