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

public class Activity7 extends AppCompatActivity {
    Button volverActividades;
    TextView txtActividades, textoA;
    DatabaseReference databaseReference;
    ImageView incrementa,lectura;
    private TextToSpeech tts;
    int Contador = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7);

        volverActividades = findViewById(R.id.btnVolverActividad);
        txtActividades = findViewById(R.id.txtActividades);
        textoA = findViewById(R.id.textActividades);
        // Recibir los rut desde la activity n°2.
        String RutPaciente = getIntent().getStringExtra("RutPaciente");

        volverActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity7.this,Activity6.class);
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
                textoA.setTextSize(26);
                txtActividades.setTextSize(30);
                volverActividades.setTextSize(25);

                if(Contador == 2){
                    textoA.setTextSize(24);
                    txtActividades.setTextSize(20);
                    volverActividades.setTextSize(20);
                    Contador = 0;
                }
            }
        });
        ConsultarActividades();
        // Implementar lectura
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
                tts.speak(textoA.getText().toString() + ".  "+ txtActividades.getText().toString()+ "." + " Botón azul "+volverActividades.getText().toString()+
                        "." + " Presione sobre el botón para volver a la pantalla anterior", TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }

    public void ConsultarActividades() {
        String RutPaciente = getIntent().getStringExtra("RutPaciente");
        // Obtener solo los numeros del rut, para la consulta a la BD
        String RutTeaN = obtenerSoloNumerosRut(RutPaciente);
        databaseReference = FirebaseDatabase.getInstance().getReference("PersonaTEA");
        // Obtener el nombre del cesfam
        databaseReference.child(RutTeaN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String actividadesTea = dataSnapshot.child("Actividades").getValue(String.class);
                    txtActividades.setText(actividadesTea);
                }
                else {
                    Toast.makeText(Activity7.this, "No Existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Activity7.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public String obtenerSoloNumerosRut(String rutConFormato) {
        // Elimina caracteres no numéricos
        return rutConFormato.replaceAll("[^0-9]", "");
    }
}