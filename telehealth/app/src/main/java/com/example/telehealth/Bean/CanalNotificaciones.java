package com.example.telehealth.Bean;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;
public class CanalNotificaciones {
    public static final Map<String, String> canales = new HashMap<>();

    static {
        canales.put("Pastilla", "canal_pastilla");
        canales.put("Jarabe", "canal_jarabe");
        canales.put("Ampolla", "canal_ampolla");
        canales.put("Cápsula", "canal_capsula");
    }
    public static void crearCanales(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);

            for (Map.Entry<String, String> entry : canales.entrySet()) {
                String nombre = entry.getKey();
                String id = entry.getValue();

                NotificationChannel canal = new NotificationChannel(
                        id,
                        "Canal " + nombre,
                        NotificationManager.IMPORTANCE_HIGH
                );
                canal.setDescription("Recordatorios para " + nombre);
                canal.enableVibration(true);

                manager.createNotificationChannel(canal);
            }
        }
    }

    public static String obtenerIdPorTipo(String tipo) {
        return canales.getOrDefault(tipo, "canal_pastilla");
    }
}
