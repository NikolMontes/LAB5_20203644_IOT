package com.example.telehealth.Bean;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.telehealth.receivers.MedicamentoReceiver;

public class NotificacionMedicamentoConf {
    public static void programarAlarma(Context context, Medicamento med, int idUnico) {
        Intent intent = new Intent(context, MedicamentoReceiver.class);
        intent.putExtra("nombre", med.getNombre());
        intent.putExtra("dosis", med.getDosis());
        intent.putExtra("tipo", med.getTipo());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                idUnico,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long intervaloMs = med.getFrecuenciaHoras() * 60L * 60L * 1000L;
        long tiempoInicial = med.getTimestampInicio(); //  esto se guarda como `startDate.getTime()`

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                tiempoInicial,
                intervaloMs,
                pendingIntent
        );
    }

    public static void cancelarAlarma(Context context, Medicamento med, int idUnico) {
        Intent intent = new Intent(context, MedicamentoReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                idUnico,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}
