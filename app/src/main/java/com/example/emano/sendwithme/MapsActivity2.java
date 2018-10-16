package com.example.emano.sendwithme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LatLng recife = new LatLng(-8.05428, -34.8813);

    public void setRecife(LatLng recife) {
        this.recife = recife;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    private Marker posicaoAtual;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.actvity_cadastro_pedido);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map3);
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
        LatLng sydney = recife;

        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(sydney).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }


    public void onLocationChanged(Location location) {
        if (posicaoAtual != null) {
            posicaoAtual.remove();
        }
        //Add marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Localização atual");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        posicaoAtual = mMap.addMarker(markerOptions);

        //Move to new location
        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(5).target(latLng).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    public void onStatusChanged(String s, int i, Bundle bundle) {

    }


    public void onProviderEnabled(String s) {

    }


    public void onProviderDisabled(String s) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS desativado!");
        alertDialog.setMessage("Ativar GPS?");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    private void startGettingLocations() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean canGetLocation = true;
        int ALL_PERMISSIONS_RESULT = 101;
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;// Distance in meters
        long MIN_TIME_BW_UPDATES = 1000 * 10;// Time in milliseconds

        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsToRequest;

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        //Check if GPS and Network are on, if not asks the user to turn on
        if (!isGPS && !isNetwork) {
            showSettingsAlert();
        } else {
            // check permissions

            // check permissions for later versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    canGetLocation = false;
                }
            }
        }


        //Checks if FINE LOCATION and COARSE Location were granted
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
            return;
        }

        //Starts requesting location updates
        if (canGetLocation) {
            if (isGPS) {
                lm.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

            } else if (isNetwork) {
                // from Network Provider

                lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

            }
        } else {
            Toast.makeText(this, "Não é possível obter a localização", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMarkers() {


    }

    private void getAllLocations(Map<String, Object> locations) {


        for (Map.Entry<String, Object> entry : locations.entrySet()) {

            Date newDate = new Date(Long.valueOf(entry.getKey()));
            Map singleLocation = (Map) entry.getValue();
            LatLng latLng = new LatLng((Double) singleLocation.get("latitude"), (Double) singleLocation.get("longitude"));
            addGreenMarker(newDate, latLng);

        }


    }

    private void addGreenMarker(Date newDate, LatLng latLng) {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(dt.format(newDate));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(markerOptions);
    }

    private String montarURLRotaMapa(double latOrigen, double lngOrigen, double latDestino, double lngDestino) {
        //Base da URL
        String url = "http://maps.googleapis.com/maps/api/directions/json?origin=";
        //Local de origem
        url += latOrigen + "," + lngOrigen;
        url += "&destination=";
        //Local de destino
        url += latDestino + "," + lngDestino;
        //Outros parametros
        url += "&sensor=false&mode=driving&alternatives=true";

        return url;
    }

    public JSONObject requisicaoHTTP(String url) {
        JSONObject resultado = null;

        try {
            //Criamos um cliente HTTP para que possamos realizar a
            //requisição a um Servidor HTTP
            DefaultHttpClient httpClient = new DefaultHttpClient();
            //Definimos o método de requisição como sendo POST
            HttpPost httpPost = new HttpPost(url);
            //Recuperamos a resposta do Servidor HTTP
            HttpResponse httpResponse = httpClient.execute(httpPost);
            //Recuperamos o conteúdo enviado do Servidor HTTP
            HttpEntity httpEntity = httpResponse.getEntity();
            //Transformamos tal conteúdo em 'String'
            String conteudo = EntityUtils.toString(httpEntity);

            //Transformamos a 'String' do conteúdo em Objeto JSON
            resultado = new JSONObject(conteudo);

        } catch (UnsupportedEncodingException e) {
            Log.e("ProjetoMapas", e.getMessage());
        } catch (ClientProtocolException e) {
            Log.e("ProjetoMapas", e.getMessage());
        } catch (IOException e) {
            Log.e("ProjetoMapas", e.getMessage());
        } catch (JSONException e) {
            Log.e("ProjetoMapas", e.getMessage());
        }

        //Retornamos o conteúdo em formato JSON
        return resultado;
    }

    private List<LatLng> extrairLatLngDaRota(String pontosPintar) {
        List<LatLng> listaResult = new ArrayList<LatLng>();
        int index = 0, len = pontosPintar.length();
        int lat = 0, lng = 0;

        while (index < len) {

            int b, shift = 0, result = 0;
            do {
                b = pontosPintar.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = pontosPintar.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            listaResult.add(p);
        }

        return listaResult;
    }

    public void pintarCaminho(JSONObject json) {
        try {
            //Recupera a lista de possíveis rotas
            JSONArray listaRotas = json.getJSONArray("routes");
            //Para efeito de aprendizado iremos utilizar apenas a primeira opção
            JSONObject rota = listaRotas.getJSONObject(0);
            //Recuperamos os pontos a serem pintados para que surga a 'linha' no mapa
            String pontosPintar = rota.getJSONObject("overview_polyline").getString("points");
            //Recuperamos a lista de latitudes e longitudes para sabermos exatamente onde pintar
            List<LatLng> listaCordenadas = extrairLatLngDaRota(pontosPintar);

            //Percorremos por cada cordenada obtida
            for (int ponto = 0; ponto < listaCordenadas.size() - 1; ponto++) {
                //Definimos o ponto atual como origem
                LatLng pontoOrigem = listaCordenadas.get(ponto);
                //Definimos o próximo ponto como destino
                LatLng pontoDestino = listaCordenadas.get(ponto + 1);
                //Criamos um objeto do tipo PolylineOption para adicionarmos os pontos de origem e destino
                PolylineOptions opcoesDaLinha = new PolylineOptions();
                //Adicionamos os pontos de origem e destino da linha que vamos traçar
                opcoesDaLinha.add(new LatLng(pontoOrigem.latitude, pontoOrigem.longitude),
                        new LatLng(pontoDestino.latitude, pontoDestino.longitude));
                //Criamos a linha de acordo com as opções que configuramos acima e adicionamos em nosso mapa
                Polyline line = mMap.addPolyline(opcoesDaLinha);
                //Mudamos algumas propriedades da linha que acabamos de adicionar em nosso mapa
                line.setWidth(5);
                line.setColor(Color.BLUE);
                line.setGeodesic(true);
            }
        } catch (JSONException e) {
            Log.e("ProjetoMapas", e.getMessage());
        }
    }

    public void buscarRota(View view) {

        EditText torigem = (EditText)findViewById(R.id.editOrigemPedido);
        EditText tdestino = (EditText)findViewById(R.id.edtDestinoPedido);
        String origem = torigem.getText().toString();
        String destino = tdestino.getText().toString();


        List<Address>addressesList=null;
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

                //Limpamos quaisquer configurações anteriores em nosso mapa
                 mMap.setInfoWindowAdapter(null);
                 mMap.setOnMyLocationChangeListener(null);
                 mMap.clear();

                //Recuperamos a URL passando as cordenadas de origem como sendo a cordenada que definimos
                //para a nossa residência e as coordenadas de destino como sendo a do escritório da Google em SP.
                String url = montarURLRotaMapa(latLng.latitude, latLng.longitude, latLng2.latitude, latLng2.longitude);
                //Criamos uma instância de nossa AsyncTask (para cada requisição deverá ser criada uma nova instância).
                MinhaAsyncTask tarefa = new MinhaAsyncTask();

                //Se a versão de SDK do Android do aparelho que está executando o aplicativo for menor que a HONEYCOMB (11)
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB){
                    //Executa a tarefa passando a URL recuperada
                    tarefa.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
                }else{
                    //Executa a tarefa passando a URL recuperada
                    tarefa.execute(url);
                }

                //Adicionamos um marcador no ponto de partida e no ponto de destino
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.addMarker(new MarkerOptions().position(latLng2));

                //Animação para visualizar ponto de partida
                CameraPosition updateRota = new CameraPosition(latLng, 15, 0, 0);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(updateRota), 3000, null);

                /*mo.title(origem);
                mo2.title(destino);
                mMap.addMarker(mo);
                mMap.addMarker(mo2);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng2));
                */

            }


        }


    }


    public void fazerPedido(View view){
        EditText  edt_pegaLoc =(EditText)findViewById(R.id.editOrigemPedido);
        EditText  edt_destino =(EditText)findViewById(R.id.edtDestinoPedido);
        String origem= edt_pegaLoc.getText().toString();
        String destino= edt_destino.getText().toString();


        Intent intent = new Intent(getApplicationContext(),ConfirmarPedidoActivity.class);
        intent.putExtra("origem",origem);
        intent.putExtra("destino",destino);
        startActivity(intent);



    }

    private class MinhaAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject resultJSON = requisicaoHTTP(params[0]);

            return resultJSON;
        }

        @Override
        protected void onPostExecute(JSONObject resultadoRequisicao) {
            super.onPostExecute(resultadoRequisicao);

            if (resultadoRequisicao != null) {
                pintarCaminho(resultadoRequisicao);
            }
        }

    }

}


