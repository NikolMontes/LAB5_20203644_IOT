package com.example.telehealth.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.telehealth.Bean.CanalNotificaciones;
import com.example.telehealth.R;

public class MedicamentoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String nombre = intent.getStringExtra("nombre");
        String dosis = intent.getStringExtra("dosis");
        String tipo = intent.getStringExtra("tipo");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        String canalId = CanalNotificaciones.obtenerIdPorTipo(tipo);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, canalId)
                .setSmallIcon(R.drawable.admin_meds_24px) // Usa un ícono diferente al de la app
                .setContentTitle("Hora de tomar: " + nombre)
                .setContentText("Acción sugerida: Tomar " + dosis)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
