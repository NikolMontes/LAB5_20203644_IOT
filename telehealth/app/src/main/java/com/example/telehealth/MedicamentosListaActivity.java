package com.example.telehealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telehealth.Bean.Medicamento;
import com.example.telehealth.Bean.NotificacionMedicamentoConf;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MedicamentosListaActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMedicines;
    private MedicamentosAdapter adapter;
    private ArrayList<Medicamento> listaMedicamentos;
    private SharedPreferences sharedPreferences;
    private FloatingActionButton fabAdd;

    private TextView tvEmptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicamentos_lista);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Inicializar vistas
        recyclerViewMedicines = findViewById(R.id.rvMedicines);
        fabAdd = findViewById(R.id.fabAddMedicine);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        // Cargar medicamentos desde SharedPreferences
        sharedPreferences = getSharedPreferences("usuarioMedicamentsPreferences", Context.MODE_PRIVATE);
        cargarMedicamentos();

        // Configurar RecyclerView
        adapter = new MedicamentosAdapter(listaMedicamentos, position -> {
            Medicamento med = listaMedicamentos.get(position);
            //se cancela la notificacion
            int idUnico = med.getNombre().hashCode();
            NotificacionMedicamentoConf.cancelarAlarma(this, med, idUnico);

            listaMedicamentos.remove(position);
            guardarMedicamentos(); // Persistencia tras eliminar
            adapter.notifyItemRemoved(position);
        });
        recyclerViewMedicines.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMedicines.setAdapter(adapter);

        // Acción del FAB
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroMedicamentoActivity.class);
            startActivity(intent);
        });

    }

    private void cargarMedicamentos() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("lista_medicamentos", null);
        Type type = new TypeToken<ArrayList<Medicamento>>() {}.getType();
        listaMedicamentos = gson.fromJson(json, type);

        if (listaMedicamentos == null|| listaMedicamentos.isEmpty()) {
            listaMedicamentos = new ArrayList<>();
            tvEmptyMessage.setVisibility(View.VISIBLE); // Mostrar mensaje si no hay nada
        }else {
            tvEmptyMessage.setVisibility(View.GONE); // Ocultar si sí hay elementos
        }
    }
    private void guardarMedicamentos() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listaMedicamentos);
        editor.putString("lista_medicamentos", json);
        editor.apply();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Recargar lista actualizada al volver de la vista de registro
        cargarMedicamentos();
        adapter = new MedicamentosAdapter(listaMedicamentos, position -> {
            Medicamento med = listaMedicamentos.get(position);

            int idUnico = med.getNombre().hashCode();
            NotificacionMedicamentoConf.cancelarAlarma(this, med, idUnico);

            listaMedicamentos.remove(position);
            guardarMedicamentos();
            adapter.notifyItemRemoved(position);

            if (listaMedicamentos.isEmpty()) {
                tvEmptyMessage.setVisibility(View.VISIBLE);
            }
        });
        recyclerViewMedicines.setAdapter(adapter);
    }
}