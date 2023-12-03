package com.android2023.appseremi;

import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

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

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button btnEduc, btnSalud;
    ImageView incrementa, lectura;
    TextView txtinicio;
    int Contador = 0;
    private TextToSpeech tts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtinicio  = findViewById(R.id.txtinicio);
        // IMW Boton Tamaño letra.
        incrementa = findViewById(R.id.incrementa);
        incrementa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contador++;
                btnEduc.setTextSize(24);
                txtinicio.setTextSize(24);
                if(Contador == 2){
                    btnEduc.setTextSize(14);
                    txtinicio.setTextSize(16);
                    Contador = 0;
                }
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
                tts.speak("Informacion Integral TEA/CEA. Seleccione En Educacion", TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        // Boton Educacion.
        btnEduc = findViewById(R.id.btnCrisis);
        btnEduc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,Activity2.class));

                // Verififcar si el dispositivo tiene contraseña. Si tiene solicitarla
                /* KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                if (keyguardManager != null) {
                    if (keyguardManager.isKeyguardSecure()) {
                        Intent intent = keyguardManager.createConfirmDeviceCredentialIntent(null,null);
                        if (intent != null) {
                            startActivityForResult(intent, DEVICE_CREDENTIAL);
                        }
                    } else {

                        // El dispositivo no tiene un método de bloqueo seguro (PIN, patrón, contraseña).
                        Toast.makeText(MainActivity.this, "El dispositivo no tiene un método de bloqueo seguro", Toast.LENGTH_SHORT).show();

                    }
                } */
            }
        });
        btnSalud = findViewById(R.id.btnSalud);
        btnSalud.setEnabled(false);

    }

    // Evento que recibe el resultado del pin. Si es correcto pasa a la siguiente Activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DEVICE_CREDENTIAL) {
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(MainActivity.this,Activity2.class));
            } else {
                // La autenticación falló o el usuario canceló la operación.
                Toast.makeText(this, "Fallo la autenticacion", Toast.LENGTH_SHORT).show();
            }
        }
    }

}