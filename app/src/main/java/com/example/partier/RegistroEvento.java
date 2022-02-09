package com.example.partier;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.partier.Clases.Evento;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Random;

public class RegistroEvento extends AppCompatActivity {
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    FirebaseDatabase database;
    DatabaseReference myRef;

    private TextInputEditText ciudadSalidaText, provinciaText, horaText, numPersonasText, sitiosText, gasolinaText,lugarFiestaText;
    ImageButton btnHora;

    private FirebaseDatabase  mDatabase = FirebaseDatabase.getInstance ();
    private DatabaseReference mDatabaseReference = mDatabase.getReference ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_evento);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logopartiername);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnGuardar = findViewById(R.id.btnGuardar);
        lugarFiestaText = findViewById(R.id.lugarFiestaText);
        ciudadSalidaText = findViewById(R.id.ciudadSalidaText);
        provinciaText = findViewById(R.id.provinciaText);
        horaText = findViewById(R.id.horaText);
        sitiosText = findViewById(R.id.sitiosText);
        gasolinaText = findViewById(R.id.gasolinaText);
        btnHora = findViewById(R.id.btnHora);

        btnHora.setOnClickListener(v -> {
            TimePickerDialog recogerHora = new TimePickerDialog(RegistroEvento.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    //Formateo el hora obtenido: antepone el 0 si son menores de 10
                    String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                    //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                    String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                    //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                    String AM_PM;
                    if(hourOfDay < 12) {
                        AM_PM = "a.m.";
                    } else {
                        AM_PM = "p.m.";
                    }
                    //Muestro la hora con el formato deseado
                    horaText.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                }
                //Estos valores deben ir en ese orden
                //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                //Pero el sistema devuelve la hora en formato 24 horas
            }, hora, minuto, true);

            recogerHora.show();
        });


        btnGuardar.setOnClickListener(view -> {

            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Evento");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();
            String userId = user.getUid();
            String lugarFiesta = lugarFiestaText.getText().toString();
            String ciudadSalida = ciudadSalidaText.getText().toString();
            String provincia = provinciaText.getText().toString();
            String horaSalida = horaText.getText().toString();
            int numPersonas = Integer.parseInt(sitiosText.getText().toString());
            int precioGasolina = Integer.parseInt(gasolinaText.getText().toString());
            Random ran = new Random();
            int id = ran.nextInt(90000);

            Evento evento = new Evento(id,email,userId,lugarFiesta,ciudadSalida,provincia,horaSalida,numPersonas,precioGasolina);
            mDatabaseReference = mDatabase.getReference().child("Evento").child(String.valueOf(id));
            mDatabaseReference.setValue (evento);

            Intent intent = new Intent(RegistroEvento.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_out,R.anim.left_in);
            this.finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.left_out,R.anim.left_in);
        super.onBackPressed();
    }
}