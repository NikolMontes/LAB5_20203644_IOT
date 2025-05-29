package com.example.telehealth;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.telehealth.Bean.CanalNotificaciones;
import com.example.telehealth.Bean.Medicamento;
import com.example.telehealth.Bean.NotificacionMedicamentoConf;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class RegistroMedicamentoActivity extends AppCompatActivity {

    private TextInputEditText etName, etDose, etFrequency, etStartDate;
    private AutoCompleteTextView spinnerType;
    private MaterialButton btnSave;
    private SharedPreferences sharedPreferences;
    private ArrayList<Medicamento> listaMedicamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_medicamento);

        etName = findViewById(R.id.etMedicineName);
        etDose = findViewById(R.id.etDose);
        etFrequency = findViewById(R.id.etFrequency);
        etStartDate = findViewById(R.id.etStartDate);
        spinnerType = findViewById(R.id.spinnerMedicineType);
        btnSave = findViewById(R.id.btnSaveMedicine);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        initSharedPreferences();
        cargarLista();
        // Spinner: tipos de medicamentos
        String[] tipos = {"Pastilla", "Jarabe", "Inyección"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tipos);
        spinnerType.setAdapter(adapter);

        // Selector de fecha
        etStartDate.setOnClickListener(v -> mostrarDatePicker());

        // Guardar medicamento
        btnSave.setOnClickListener(v -> guardarMedicamento());

    }

    public void initSharedPreferences(){
        sharedPreferences = getSharedPreferences("usuarioMedicamentsPreferences", MODE_PRIVATE);
    }
    private void mostrarDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // Al seleccionar fecha, ahora lanza el timePicker
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    mostrarTimePicker(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private void mostrarTimePicker(Calendar selectedDate) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDate.set(Calendar.MINUTE, minute);

                    // Formatear fecha y hora completa
                    String fechaHora = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm a", selectedDate).toString();
                    etStartDate.setText(fechaHora);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void cargarLista() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("lista_medicamentos", null);
        Type type = new TypeToken<ArrayList<Medicamento>>(){}.getType();
        listaMedicamentos = gson.fromJson(json, type);

        if (listaMedicamentos == null) {
            listaMedicamentos = new ArrayList<>();
        }
    }
    private void guardarMedicamento() {
        String nombre = etName.getText().toString().trim();
        String dosis = etDose.getText().toString().trim();
        String frecuenciaStr = etFrequency.getText().toString().trim();
        String tipo = spinnerType.getText().toString().trim();
        //String fechaInicio = etStartDate.getText().toString().trim();
        String fechaHoraCompleta = etStartDate.getText().toString().trim();

        String[] partes = fechaHoraCompleta.split(" ", 2);

        if (partes.length < 2) {
            Toast.makeText(this, "Selecciona fecha y hora válidas", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nombre.isEmpty() || dosis.isEmpty() || frecuenciaStr.isEmpty() || tipo.isEmpty() || fechaHoraCompleta.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int frecuenciaHoras = Integer.parseInt(frecuenciaStr);

        Medicamento nuevo = new Medicamento();
        nuevo.setNombre(nombre);
        nuevo.setTipo(tipo);
        nuevo.setDosis(dosis);
        nuevo.setFrecuenciaHoras(frecuenciaHoras);
        //nuevo.setFechaInicio(fechaInicio);
        nuevo.setFechaInicio(partes[0]); // "28/05/2025"
        nuevo.setHoraInicio(partes[1]);  // "07:15 a. m."
        //nuevo.setHoraInicio("00:00");

        listaMedicamentos.add(nuevo);

        Gson gson = new Gson();
        String json = gson.toJson(listaMedicamentos);
        sharedPreferences.edit().putString("lista_medicamentos", json).apply();
        int idUnico = nombre.hashCode();  // ID único (se puede mejorar para evitar colisiones)

        CanalNotificaciones.crearCanales(this);  // Asegura que los canales existen
        NotificacionMedicamentoConf.programarAlarma(this, nuevo, idUnico); // Programar la alarma

        Toast.makeText(this, "Medicamento guardado", Toast.LENGTH_SHORT).show();
        finish(); // volver a la lista
    }

}