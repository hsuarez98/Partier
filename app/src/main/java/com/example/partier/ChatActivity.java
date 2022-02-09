package com.example.partier;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.partier.Clases.Evento;
import com.example.partier.Clases.Mensaje;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private FirebaseListAdapter<Mensaje> adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Chats");

    String userCreador, chatUser;
    private Mensaje mensaje;
    ArrayList<Mensaje> listaMensajes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String idChat1 = intent.getStringExtra("idChat1");
        userCreador = intent.getStringExtra("evento");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logopartiername);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listOfMessages = findViewById(R.id.listOfMessages);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        String userActual = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String[] user = userActual.split("@");
        String[] userFinal = user[0].split("\\.");
        chatUser = userFinal[0];

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                if (input.equals("")){
                    input.setError("Campo vacio");
                }else{
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Chats")
                            .child(idChat1)
                            .push()
                            .setValue(new Mensaje(idChat1, input.getText().toString(), userActual, userCreador)
                            );

                    input.setText("");
                }

            }
        });

        displayChatMessages(listOfMessages, idChat1);
    }

    private void displayChatMessages(ListView listOfMessages, String idChat1) {

        Query query = FirebaseDatabase.getInstance().getReference().child("Chats").child(idChat1);

        FirebaseListOptions<Mensaje> options = new FirebaseListOptions.Builder<Mensaje>()
                .setQuery(query, Mensaje.class)
                .setLayout(R.layout.mensajes_layout)
                .build();

        adapter = new FirebaseListAdapter<Mensaje>(options) {
            @Override
            protected void populateView(View v, Mensaje model, int position) {
                // Bind the Chat to the view
                // Get references to the views of message.xml
                TextView messageText = (TextView) v.findViewById(R.id.txtMensaje);
                TextView messageUser = (TextView) v.findViewById(R.id.txtUser);

                // Set their text
                if (model.getUser().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    params.weight = 1.0f;
                    params.gravity = Gravity.END;
                    GradientDrawable shape =  new GradientDrawable();
                    shape.setCornerRadius(20);
                    messageText.setBackground(shape);
                    messageText.setBackgroundColor(getResources().getColor(R.color.yellow));
                    messageText.setTextColor(getResources().getColor(R.color.black));
                    messageText.setPadding(10, 10, 10,10);
                    messageText.setLayoutParams(params);
                    messageUser.setLayoutParams(params);
                    messageUser.setText("");
                }else{
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    params.weight = 1.0f;
                    params.gravity = Gravity.START;
                    GradientDrawable shape =  new GradientDrawable();
                    shape.setCornerRadius(20);
                    messageText.setBackground(shape);
                    messageText.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    messageText.setTextColor(getResources().getColor(R.color.black));
                    messageText.setPadding(10, 10, 10,10);
                    messageText.setLayoutParams(params);
                    messageUser.setLayoutParams(params);
                    messageUser.setText("");
                }
                messageText.setText(model.getMensaje());
                String[] user = model.getUser().split("@");
                messageUser.setText(user[0]);
                //listOfMessages.smoothScrollToPosition(listaMensajes.size()-1);
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.left_out, R.anim.left_in);
        this.finish();
        super.onBackPressed();
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
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}