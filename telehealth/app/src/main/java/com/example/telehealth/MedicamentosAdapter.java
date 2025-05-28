package com.example.telehealth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(position);
            }
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

    //private OnMedicamentoClickListener listener;
   /* public interface OnMedicamentoClickListener {
        void onMedicamentoClick(Medicamento medicamento);
        void onDeleteClick(Medicamento medicamento);
    }

    public MedicamentosAdapter(OnMedicamentoClickListener listener) {
        this.medicamentos = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicamento, parent, false);
        return new RecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Medicamento medicamento = medicamentos.get(position);
        holder.bind(medicamento);
    }

    @Override
    public int getItemCount() {
        return medicamentos.size();
    }

    public void updateMedicamentos(List<Medicamento> nuevaLista) {
        this.medicamentos.clear();
        this.medicamentos.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvTipoDosis, tvFrecuencia, tvFechaHora;
        private ImageView ivTipo;
        private ImageButton btnEliminar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvMedicineName);
            tvTipoDosis = itemView.findViewById(R.id.tv_tipo_dosis);
            tvFrecuencia = itemView.findViewById(R.id.tv_frecuencia);
            tvFechaHora = itemView.findViewById(R.id.tv_fecha_hora);
            ivTipo = itemView.findViewById(R.id.iv_tipo_medicamento);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onMedicamentoClick(medicamentos.get(getAdapterPosition()));
                }
            });

            btnEliminar.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(medicamentos.get(getAdapterPosition()));
                }
            });
        }

        void bind(Medicamento medicamento) {
            tvNombre.setText(medicamento.getNombre());
            tvTipoDosis.setText(medicamento.getTipo() + " - " + medicamento.getDosis());
            tvFrecuencia.setText("Cada " + medicamento.getFrecuenciaHoras() + " horas");
            tvFechaHora.setText("Desde: " + medicamento.getFechaInicio() + " " + medicamento.getHoraInicio());

            // Establecer icono según el tipo
            int iconoRes = getIconoTipo(medicamento.getTipo());
            ivTipo.setImageResource(iconoRes);
        }

        private int getIconoTipo(String tipo) {
            switch (tipo.toLowerCase()) {
                case "pastilla":
                    return R.drawable.ic_pill;
                case "jarabe":
                    return R.drawable.ic_syrup;
                case "ampolla":
                    return R.drawable.ic_injection;
                case "cápsula":
                    return R.drawable.ic_capsule;
                default:
                    return R.drawable.ic_medicine;
            }
        }
    }*/
}
