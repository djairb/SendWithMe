package com.example.emano.sendwithme.HomePackage;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.security.PublicKey;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private static final int TAG_CODE_PERMISSION_LOCATION = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
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
        //ADICIONADO MARCADOR PARA UM LUGAR ESPECIFICO BASEADO NA LATITUDE E LONGITUDE PASSADOS
        mMap = googleMap;


        //chamada do click no mapa
        //mMap.setOnMapClickListener(this);

        mMap.getUiSettings().setZoomControlsEnabled(true);


        LatLng tracunhaem = new LatLng(-7.8031351, -35.2372257);

        //MarkerOptions markerOptions = new MarkerOptions();
        //markerOptions.position(tracunhaem);
        //markerOptions.title("Tracunhaém");

        //mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tracunhaem));

        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            //Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.INTERNET
                    },
                    TAG_CODE_PERMISSION_LOCATION);

        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //recupera lat and lng do lugar clicado no mapa
        Toast.makeText(getContext(),"Lat, Lng: " + latLng.toString(),
                Toast.LENGTH_SHORT).show();

    }

    public void setarLocalizacao(LatLng latLng){



        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("New Position");

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


    }

    public void clear(){
        mMap.clear();
    }

    public void addMarker(MarkerOptions markerOptions){
        mMap.addMarker(markerOptions);
    }

    public void  moveCameraTo(LatLng latLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    public void addPolyline(PolylineOptions polylineOptions){
        mMap.addPolyline(polylineOptions);

    }

}