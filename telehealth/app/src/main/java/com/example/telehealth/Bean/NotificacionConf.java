package com.example.telehealth.Bean;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.telehealth.receivers.MotivacionalReceiver;

public class NotificacionConf {
    public static final String CHANNEL_ID = "canal_motivacional";

    public static void programarNotificacionMotivacional(Context context, String mensaje, int cadaHoras) {
        // Cancelar alarmas previas
        cancelarNotificacionMotivacional(context);

        // Crear canal de notificación si es necesario
        crearCanal(context);

        // Configurar Intent
        Intent intent = new Intent(context, MotivacionalReceiver.class);
        intent.putExtra("mensaje", mensaje);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long intervalo = cadaHoras * 60L * 60L * 1000L;
        long triggerAtMillis = System.currentTimeMillis() + intervalo;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                intervalo,
                pendingIntent
        );
    }
    public static void cancelarNotificacionMotivacional(Context context) {
        Intent intent = new Intent(context, MotivacionalReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private static void crearCanal(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificaciones Motivacionales";
            String description = "Canal para mensajes motivacionales cada X horas";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.enableLights(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
