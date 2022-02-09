package com.example.partier;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    private Button signUpButton, logInButton;
    private TextInputEditText emailEditText, passwordEditText;
    private ImageView imgLogo;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        signUpButton = findViewById(R.id.signUpButton);
        logInButton = findViewById(R.id.logInButton);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        imgLogo = findViewById(R.id.imgLogo);

        getSupportActionBar().hide();

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                imgLogo.setImageResource(R.drawable.logopartierblack);
                imgLogo.setVisibility(View.VISIBLE);
                NotificationCompat.Builder mBuilderDark = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logopartierblack)
                        .setContentTitle("Title");
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                imgLogo.setImageResource(R.drawable.logopartier);
                imgLogo.setVisibility(View.VISIBLE);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logopartiername);
                break;
        }

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserAccount();
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });


    }

    private void loginUserAccount() {
        // Take the value of two edit texts in Strings
        String email, password;
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Introduce un email!!",
                    Toast.LENGTH_LONG)
                    .show();
            emailEditText.setError("Correo invalido");
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Introduce una contraseña!!",
                    Toast.LENGTH_LONG)
                    .show();
            emailEditText.setError("Contraseña incorrrecta");
            return;
        }

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Inicio de session correcto!!",
                                        Toast.LENGTH_LONG)
                                        .show();

                                // if sign-in is successful
                                // intent to home activity
                                Intent intent = new Intent(AuthActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                emailEditText.setText("");
                                passwordEditText.setText("");
                                overridePendingTransition(R.anim.top_in, R.anim.top_out);
                            } else {
                                emailEditText.setError("Contraseña incorrecta");
                            }
                        });
    }

    private void registerNewUser() {
        // Take the value of two edit texts in Strings
        String email, password, name, surname;
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Campo vacio");
            return;
        } else if (TextUtils.isEmpty(password)) {
            emailEditText.setError("Campo vacio");
            return;
        }

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),
                                "Registrado con exito!",
                                Toast.LENGTH_LONG)
                                .show();

                        // if the user created intent to login activity
                        Intent intent
                                = new Intent(AuthActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                    } else {
                        // Registration failed
                        if (task.getException().getMessage().contains("already in use")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
                            builder.setTitle("Partier");
                            builder.setMessage(R.string.request_login);
                            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    loginUserAccount();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.show();
                        } else if (task.getException().getMessage().contains("badly formatted")) {
                            emailEditText.setError("Correo no valido");
                        } else {
                            emailEditText.setError(task.getException().getMessage());
                        }
                    }
                });
    }
}