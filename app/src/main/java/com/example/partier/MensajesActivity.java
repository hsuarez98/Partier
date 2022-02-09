package com.example.partier;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.partier.Clases.Mensaje;
import com.example.partier.adapters.MensajesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MensajesActivity extends AppCompatActivity {
    String idUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Chats");

    ArrayList<Mensaje> listaMensajes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logopartiername);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView lista = findViewById(R.id.listaMensajes);

        //Obtener lista de chats, buscar el que contenga el id del user actual y mostrar esos
        idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Obtener mensajes i llenar lista
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getDatosFirebase(snapshot);
                MensajesAdapter adapter = new MensajesAdapter(MensajesActivity.this, listaMensajes);
                lista.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DEBUG", "Failure");
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MensajesActivity.this, ChatActivity.class);
                intent.putExtra("idChat1", listaMensajes.get(i).getChatId());
                intent.putExtra("evento",listaMensajes.get(i).getUserCreator());
                startActivity(intent);
            }
        });

    }

    public void getDatosFirebase(@NonNull DataSnapshot snapshot) {
        //Obtener id chat para mostrar solo los del user
        String userComp = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Mensaje mensaje = null;

        for (DataSnapshot ds : snapshot.getChildren()) {
            for (DataSnapshot dSnapshot : ds.getChildren()) {
                mensaje = dSnapshot.getValue(Mensaje.class);
                if (mensaje.getUser().equals(userComp) || mensaje.getUserCreator().equals(userComp)) {
                    if (!listaMensajes.contains(mensaje.getChatId()))
                    listaMensajes.add(mensaje);
                }
            }
        }
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
}