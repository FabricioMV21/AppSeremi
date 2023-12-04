package com.android2023.appseremi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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

import org.w3c.dom.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

public class Activity4 extends AppCompatActivity {

    TextView txtRutTeaOut, txtNombreOut, txtGrado;
    DatabaseReference databaseReference;
    LinearLayout cGrado;
    ImageView incrementa,lectura;
    private TextToSpeech tts;
    int Contador = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);
        txtRutTeaOut = findViewById(R.id.txtRutTeaOut);
        txtNombreOut = findViewById(R.id.txtNombreOut);
        txtGrado = findViewById(R.id.txtGrado);
        cGrado = findViewById(R.id.cardGrado);

        cGrado.setBackgroundResource(R.drawable.rounded_border);

        // Recibir los rut desde la activity n°2.
        String RutPaciente = getIntent().getStringExtra("RutPaciente");

        //Pasar al siguiente activity y enviar el rutTea
        cGrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        Activity5.class);
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
                txtRutTeaOut.setTextSize(30);
                txtNombreOut.setTextSize(30);
                txtGrado.setTextSize(44);

                if(Contador == 2){
                    txtRutTeaOut.setTextSize(26);
                    txtNombreOut.setTextSize(26);
                    txtGrado.setTextSize(34);
                    Contador = 0;
                }
            }
        });

        // Asignar el rut al campo de texto.
        txtRutTeaOut.setText(RutPaciente);

        // Metodo para consultar el nombre y grado de TEA
        ConsultarNombreyGradoTEA();

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
                tts.speak("Rut "+ txtRutTeaOut.getText().toString() + " Nombre "+ txtNombreOut.getText().toString()+"." +" Grado TEA CEA "+txtGrado.getText().toString()+"."+
                        " Presione sobre el grado para ver los detalles", TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }

    public void ConsultarNombreyGradoTEA() {
        String RutPaciente = getIntent().getStringExtra("RutPaciente");
        // Obtener solo los numeros del rut, para la consulta a la BD
        String RutTeaN = obtenerSoloNumerosRut(RutPaciente);
        databaseReference = FirebaseDatabase.getInstance().getReference("PersonaTEA");
        // Obtener el nombre del cesfam
        databaseReference.child(RutTeaN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String gradoTEA = dataSnapshot.child("GradoTEA").getValue(String.class);
                    txtGrado.setText(gradoTEA);
                    String nombre = dataSnapshot.child("Nombre").getValue(String.class);
                    txtNombreOut.setText(nombre);

                    }
                 else {
                    Toast.makeText(Activity4.this, "No Existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Activity4.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public String obtenerSoloNumerosRut(String rutConFormato) {
        // Elimina caracteres no numéricos
        return rutConFormato.replaceAll("[^0-9]", "");
    }
}