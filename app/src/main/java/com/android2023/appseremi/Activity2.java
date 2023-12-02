package com.android2023.appseremi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Activity2 extends AppCompatActivity {
    EditText rutTeaIn, rutTutorIn;
    Button buscar;
    TextView txtinicio, txtNtea, txtNtutor;
    ImageView incrementa, lectura;
    DatabaseReference databaseReference;
    int Contador = 0;
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        txtinicio  = findViewById(R.id.txtinicio);
        txtNtutor  = findViewById(R.id.txtNTutor);
        txtNtea    = findViewById(R.id.txtNTea);
        buscar     = findViewById(R.id.btnBuscar);
        rutTeaIn   = findViewById(R.id.txtRutTeaIn);
        rutTutorIn = findViewById(R.id.txtRutTutorIn);
        incrementa = findViewById(R.id.incrementa);
        // Formatear a RUT CHILENO
        rutTeaIn.addTextChangedListener(new RutTextWatcher(rutTeaIn));
        rutTutorIn.addTextChangedListener(new RutTextWatcher(rutTutorIn));

        incrementa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contador++;
                txtinicio.setTextSize(20);
                buscar.setTextSize(24);
                txtNtea.setTextSize(22);
                txtNtutor.setTextSize(22);

                if(Contador == 2){
                    txtinicio.setTextSize(16);
                    buscar.setTextSize(16);
                    txtNtea.setTextSize(16);
                    txtNtutor.setTextSize(16);
                    Contador = 0;
                }
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CesfamPorRut();
            }
        });

        // Configuracion del altavoz.
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
                tts.speak("Ingrese el RUT De La Persona TEA. " +
                        "Luego El RUT Del Tutor. Como Ultimo Paso En Buscar", TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }

    public void CesfamPorRut(){
        String RutTea = rutTeaIn.getText().toString();
        String RutTutor = rutTutorIn.getText().toString();
        String RutTeaN = obtenerSoloNumerosRut(RutTea);
        // Validar los campos antes de la consulta
        if(RutTea.isEmpty() || RutTutor.isEmpty()){
            Toast.makeText(this, "Ingrese los RUT", Toast.LENGTH_SHORT).show();
        }else{
            databaseReference = FirebaseDatabase.getInstance().getReference("PersonaTEA");
            // Obtener el nombre del cesfam
            databaseReference.child(RutTeaN).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String nombreCesfam = dataSnapshot.child("Cesfam").child("Nombre").getValue(String.class);
                        Intent intent = new Intent(Activity2.this, Activity3.class);
                        intent.putExtra("NombreCesfam",nombreCesfam);
                        intent.putExtra("RutTEA", RutTea);
                        intent.putExtra("RutTutor", RutTutor);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Activity2.this, "Rut Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Activity2.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public String obtenerSoloNumerosRut(String rutConFormato) {
        // Elimina caracteres no numéricos
        return rutConFormato.replaceAll("[^0-9]", "");
    }

}