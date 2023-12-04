package com.android2023.appseremi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Activity6 extends AppCompatActivity {
    Button btnActividad , btnCrisis;
    TextView txtOClinica, txtFamiliar, txtOC, txtOF;
    DatabaseReference databaseReference;
    ImageView incrementa,lectura;
    private TextToSpeech tts;
    int Contador = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6);
        btnActividad = findViewById(R.id.btnActividad);
        btnCrisis = findViewById(R.id.btnCrisis);
        txtOClinica = findViewById(R.id.txtOClinica);
        txtFamiliar = findViewById(R.id.txtDetalleTea);
        txtOC = findViewById(R.id.textOC);
        txtOF = findViewById(R.id.textOF);
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
                        Activity8.class);
                intent.putExtra("RutPaciente" ,RutPaciente);
                startActivity(intent);
            }
        });
        // Incrementar el tamaño de la letra
        incrementa = findViewById(R.id.incrementa);
        incrementa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contador++;
                txtOClinica.setTextSize(30);
                txtFamiliar.setTextSize(30);
                btnActividad.setTextSize(20);
                btnCrisis.setTextSize(20);

                if(Contador == 2){
                    txtOClinica.setTextSize(20);
                    txtFamiliar.setTextSize(20);
                    btnActividad.setTextSize(15);
                    btnCrisis.setTextSize(15);
                    Contador = 0;
                }
            }
        });

        // Asignar el rut al campo de texto.
        ConsultarDetalleTEA();

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    Locale locSpanish = new Locale("spa", "ESP");
                    tts.setLanguage(locSpanish);
                } else {
                    Toast.makeText(getApplicationContext(), "Falló la inicialización", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // IMW Boton Lectura.
        lectura = findViewById(R.id.lectura);
        lectura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak("Orientación clínica: "+ txtOClinica.getText().toString() + ". Orientación Familiar: " +
                        txtFamiliar.getText().toString()+ "." + " Botón azúl "+btnActividad.getText().toString() + "." + " Botón rojo "+ btnCrisis.getText().toString()+"."+
                        " Presione sobre el botón para ver los detalles sobre actividades o control en caso de crisis", TextToSpeech.QUEUE_FLUSH,null);
            }
        });
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
                    Toast.makeText(Activity6.this, "No Existe", Toast.LENGTH_SHORT).show();
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