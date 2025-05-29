package com.example.telehealth.receivers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.telehealth.Bean.NotificacionConf;
import com.example.telehealth.R;

public class MotivacionalReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String mensaje = intent.getStringExtra("mensaje");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificacionConf.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) //icono motivacional
                .setContentTitle("¡Motívate!")
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // No tiene permiso, no mostrar notificación
                return;
            }
        }

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(2001, builder.build());
    }
}
