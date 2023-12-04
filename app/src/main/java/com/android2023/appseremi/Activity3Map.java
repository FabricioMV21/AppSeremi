package com.android2023.appseremi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Activity3Map extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMaps;
    private FusedLocationProviderClient fusedLocationClient;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        // Obtener datos de la activity anterior
        String nombreCesfam = getIntent().getStringExtra("NombreCesfam");
        double latitud = getIntent().getDoubleExtra("Latitud", 0.0);
        double longitud = getIntent().getDoubleExtra("Longitud", 0.0);
        // Agregar el nuevo marcador
        googleMaps = map;
        LatLng centroAtencion = new LatLng(latitud,longitud);
        map.addMarker(new MarkerOptions().position(centroAtencion).title(nombreCesfam));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(centroAtencion,16));
    }
}