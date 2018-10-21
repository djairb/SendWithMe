package com.example.emano.sendwithme;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroViagem extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference databaseReference;

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

         Viagem viagem = new Viagem();
         viagem.setInicio(inicio);
         viagem.setFim(fim);

         databaseReference = FirebaseDatabase.getInstance().getReference();

         databaseReference.child("Viagens").child(viagem.getId()).setValue(viagem);

         Toast.makeText(getApplicationContext(),"Viagem salva",Toast.LENGTH_LONG).show();

    }

}
