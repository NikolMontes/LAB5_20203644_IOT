package com.example.telehealth;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private MaterialTextView tvsaludo, tvfrase;
    private ImageView ivImageup;
    private MaterialButton btnlistamedicamentos, btnconfig;
    private Uri imageUri;
    private SharedPreferences sharedPreferences;
    private ActivityResultLauncher<Intent> galeriaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.green_main)); // Tu color deseado
        }
        setContentView(R.layout.activity_main);

        tvsaludo = findViewById(R.id.tvsaludo);
        tvfrase = findViewById(R.id.tvfrase);
        ivImageup = findViewById(R.id.ivImageup);
        Log.d("CHECK_IV", "ivImageup es null? " + (ivImageup == null));
        btnlistamedicamentos = findViewById(R.id.btnlistamedicamentos);
        btnconfig = findViewById(R.id.btnconfig);

        initSharedPreferences();
        setupGaleria();
        setupClickListeners();
        loadUserData();
        loadProfileImage();

        // Solicitar permisos de notificación si es necesario
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
        }

    }

    private void initSharedPreferences(){
        sharedPreferences = getSharedPreferences("usuarioMedicamentsPreferences",MODE_PRIVATE);
    }

    private void setupGaleria(){
        galeriaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        //sharedPreferences.edit().putString("rutaImagen", imageUri.toString()).apply();
                        saveDataToInternalStorage(imageUri);
                    }
                }
        );
    }

    private void saveDataToInternalStorage(Uri imageUri){
        try{
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            File file = new File(getFilesDir(),"imagen_medicina.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();

            ivImageup.setImageBitmap(bitmap);
            Toast.makeText(this, "Imagen guardada", Toast.LENGTH_SHORT).show();
            Log.d("IMAGEN", "Imagen guardada en: " + file.getAbsolutePath());
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
            Log.e("IMAGEN", "Error al guardar imagen", e);
        }
    }

    private void loadUserData() {
        String nombre = sharedPreferences.getString("nombre_usuario", "Usuario");
        String mensaje = sharedPreferences.getString("mensaje_motivacional",
                "¡Hoy es un buen día para cuidar tu salud!");

        tvsaludo.setText("¡Hola, " + nombre + "!");
        tvfrase.setText(mensaje);
    }
    private void setupClickListeners() {
        ivImageup.setOnClickListener(v -> openGallery());

        btnlistamedicamentos.setOnClickListener(v -> {
            Intent intent = new Intent(this, MedicamentosListaActivity.class);
            startActivity(intent);
        });

        btnconfig.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConfiguracionesActivity.class);
            startActivity(intent);
        });
    }
    private void openGallery(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // API 28 o menos, necesita permiso
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
                return; // no abrir la galería aún
            }
        }
        //luego de tener el permiso o en caso el api sea mayor a 28
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galeriaLauncher.launch(intent);
    }
    private void loadProfileImage() {
        File file = new File(getFilesDir(), "imagen_medicina.jpg");
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ivImageup.setImageBitmap(bitmap);
                inputStream.close();
                Log.d("IMAGEN CARGADA", "Imagen cargada desde: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("IMAGEN NO CARGADA", "No se encontró imagen guardada");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData(); // Recargar datos al volver de configuraciones
    }
}