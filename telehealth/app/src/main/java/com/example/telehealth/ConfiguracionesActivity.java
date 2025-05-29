package com.example.telehealth;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.telehealth.Bean.NotificacionConf;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class ConfiguracionesActivity extends AppCompatActivity {

    private TextInputEditText etUserName, etMotivationalMessage, etNotificationFrequency;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuraciones);
        etUserName = findViewById(R.id.etUserName);
        etMotivationalMessage = findViewById(R.id.etMotivationalMessage);
        etNotificationFrequency = findViewById(R.id.etNotificationFrequency);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        prefs = getSharedPreferences("usuarioMedicamentsPreferences", MODE_PRIVATE);
        // Cargar valores guardados
        etUserName.setText(prefs.getString("nombre_usuario", ""));
        etMotivationalMessage.setText(prefs.getString("mensaje_motivacional", ""));
        int frecuenciaGuardada = prefs.getInt("frecuencia_motivacional", 6);
        etNotificationFrequency.setText(String.valueOf(frecuenciaGuardada));

        findViewById(R.id.btnSaveSettings).setOnClickListener(v -> {
            String nombre = etUserName.getText().toString().trim();
            String mensaje = etMotivationalMessage.getText().toString().trim();
            String frecuenciaStr = etNotificationFrequency.getText().toString().trim();

            if (nombre.isEmpty() || mensaje.isEmpty() || frecuenciaStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int frecuenciaHoras = Integer.parseInt(frecuenciaStr);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nombre_usuario", nombre);
            editor.putString("mensaje_motivacional", mensaje);
            editor.putInt("frecuencia_motivacional", frecuenciaHoras);
            editor.apply();

            NotificacionConf.programarNotificacionMotivacional(this, mensaje, frecuenciaHoras);

            Toast.makeText(this, "Configuración guardada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}