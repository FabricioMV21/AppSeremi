package com.android2023.appseremi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity6 extends AppCompatActivity {
    Button btnActividad , btnCrisis;
    TextView txtOClinica, txtFamiliar, txtRutTeaOut;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6);
        btnActividad = findViewById(R.id.btnActividad);
        btnCrisis = findViewById(R.id.btnCrisis);
        txtOClinica = findViewById(R.id.txtOClinica);
        txtFamiliar = findViewById(R.id.txtDetalleTea);
        txtRutTeaOut = findViewById(R.id.txtRutTeaOut);
        // Recibir los rut desde la activity n°2.
        String RutPaciente = getIntent().getStringExtra("RutPaciente");

        btnActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        Activity7.class);
                intent.putExtra("RutPaciente", RutPaciente);
                startActivity(intent);


            }
        });

        btnCrisis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        Activity7.class);
                intent.putExtra("RutPaciente" ,RutPaciente);
                startActivity(intent);

            }
        });

        // Asignar el rut al campo de texto.
        //txtRutTeaOut.setText(RutPaciente);
        ConsultarDetalleTEA();
    }

    public void ConsultarDetalleTEA() {
        String RutPaciente = getIntent().getStringExtra("RutPaciente");
        // Obtener solo los numeros del rut, para la consulta a la BD
        String RutTeaN = obtenerSoloNumerosRut(RutPaciente);
        databaseReference = FirebaseDatabase.getInstance().getReference("PersonaTEA");
        // Obtener el nombre del cesfam
        databaseReference.child(RutTeaN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String oClinica = dataSnapshot.child("OrientacionClinica").getValue(String.class);
                    txtOClinica.setText(oClinica);
                    String oFamiliar = dataSnapshot.child("OrientacionFamiliar").getValue(String.class);
                    txtFamiliar.setText(oFamiliar);
                }
                else {
                    Toast.makeText(Activity6.this, "Rut Incorrecto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Activity6.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public String obtenerSoloNumerosRut(String rutConFormato) {
        // Elimina caracteres no numéricos
        return rutConFormato.replaceAll("[^0-9]", "");
    }
}