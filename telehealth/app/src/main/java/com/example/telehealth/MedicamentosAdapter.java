package com.example.telehealth;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telehealth.Bean.Medicamento;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MedicamentosAdapter extends RecyclerView.Adapter<MedicamentosAdapter.ViewHolder>{
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
    private List<Medicamento> medicamentos;
    private OnDeleteClickListener deleteClickListener;

    public MedicamentosAdapter(List<Medicamento> listaMedicamentos, OnDeleteClickListener listener) {
        this.medicamentos = listaMedicamentos;
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicina, parent, false);
        return new ViewHolder(vista);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicamento med = medicamentos.get(position);
        // Asignar nombre
        holder.tvNombre.setText(med.getNombre());
        // Concatenar dosis + tipo + frecuencia
        String dosisFrecuencia = med.getDosis() + " " + med.getTipo() +
                " • Cada " + med.getFrecuenciaHoras() + " horas";
        holder.tvDosisFrecuencia.setText(dosisFrecuencia);
        // Concatenar fecha + hora de inicio
        String fechaHora = "Desde: " + med.getFechaInicio() + " " + med.getHoraInicio();
        holder.tvFechaHoraInicio.setText(fechaHora);
        // Click en eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Estás seguro de que deseas eliminar este medicamento?")
                    .setPositiveButton("Sí", (dialogInterface, which) -> {
                        if (deleteClickListener != null) {
                            deleteClickListener.onDeleteClick(position);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(context, R.color.red)
                );
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(context, R.color.blue)
                );
            });

            dialog.show();

        });

    }

    @Override
    public int getItemCount() {
        return medicamentos.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDosisFrecuencia, tvFechaHoraInicio;
        MaterialButton btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvMedicineName);
            tvDosisFrecuencia = itemView.findViewById(R.id.tvDosisFrecuencia);
            tvFechaHoraInicio = itemView.findViewById(R.id.tvFechaHoraInicio);
            btnEliminar = itemView.findViewById(R.id.btnDelete);
        }
    }
}
