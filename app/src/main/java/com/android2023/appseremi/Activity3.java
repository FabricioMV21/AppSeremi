package com.android2023.appseremi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity3 extends AppCompatActivity {
    TextView txtRutTea, txtRutTutor, txtNombreCen;
    ImageView incrementa;
    DatabaseReference databaseReference;
    ImageView ubicacion;
    LinearLayout nCentro;
    int Contador = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        txtRutTea    = findViewById(R.id.txtRutTeaOut);
        txtRutTutor  = findViewById(R.id.txtNombreOut);
        txtNombreCen = findViewById(R.id.txtNomCentro);
        ubicacion    = findViewById(R.id.imgMap);
        nCentro = findViewById(R.id.cardCentro);

        // Recibir los rut desde la activity n°2.
        String RutPaciente = getIntent().getStringExtra("RutTEA");
        String RutTutor = getIntent().getStringExtra("RutTutor");
        String NombreCesfam = getIntent().getStringExtra("NombreCesfam");

        // Mostrar Datos en el activity
        txtRutTea.setText(RutPaciente);
        txtRutTutor.setText(RutTutor);
        txtNombreCen.setText("Ficha Clinica " + NombreCesfam);

        nCentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity4.class);
                intent.putExtra("RutPaciente", RutPaciente);
                startActivity(intent);
            }
        });

        // Incrementar el tamaño de la letra
        incrementa = findViewById(R.id.incrementa);
        incrementa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contador++;
                txtRutTea.setTextSize(36);
                txtRutTutor.setTextSize(36);
                txtNombreCen.setTextSize(32);

                if(Contador == 2){
                    txtRutTea.setTextSize(24);
                    txtRutTutor.setTextSize(24);
                    txtNombreCen.setTextSize(24);
                    Contador = 0;
                }
            }
        });

        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObtenerCordenadas();
            }
        });

        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.YELLOW);
        border.setStroke(2,Color.GRAY);
        nCentro.setBackground(border);
    }
    public void ObtenerCordenadas() {
        String RutPaciente = getIntent().getStringExtra("RutTEA");
        String RutTeaN = obtenerSoloNumerosRut(RutPaciente);
        databaseReference = FirebaseDatabase.getInstance().getReference("PersonaTEA");

        databaseReference.child(RutTeaN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Acceder a la LATITUD Y LONGITUD
                    String NombreCesfam = getIntent().getStringExtra("NombreCesfam");
                    Double Latitud = dataSnapshot.child("Cesfam").child("Latitud").getValue(Double.class);
                    Double Longitud = dataSnapshot.child("Cesfam").child("Longitud").getValue(Double.class);
                    // Pasar las cordenadas a la siguiente Activity
                    Intent intent = new Intent(Activity3.this, Activity3Map.class);
                    intent.putExtra("Latitud", Latitud);
                    intent.putExtra("Longitud", Longitud);
                    intent.putExtra("NombreCesfam", NombreCesfam);
                    startActivity(intent);

                } else {
                    Toast.makeText(Activity3.this, "No Existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Activity3.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public String obtenerSoloNumerosRut(String rutConFormato) {
        return rutConFormato.replaceAll("[^0-9]", "");
    }
}