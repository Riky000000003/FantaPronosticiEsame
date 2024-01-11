package com.example.fantapronosticiesame.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fantapronosticiesame.Database.Database;
import com.example.fantapronosticiesame.Interface.UtenteEsiste;
import com.example.fantapronosticiesame.Model.User;
import com.example.fantapronosticiesame.R;
import com.example.fantapronosticiesame.databinding.ActivityRegistraBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistraActivity extends AppCompatActivity {
    protected ActivityRegistraBinding binding;
    protected Database database = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registra);

        binding.buttonReset.setOnClickListener(v -> {
            binding.editTextNome.setText("");
            binding.editTextCognome.setText("");
            binding.editTextUsernameRegistrazione.setText("");
            binding.editTextPasswordRegistrazione.setText("");
        });

        binding.buttonRegistraAccount.setOnClickListener(v -> {
            String nome = binding.editTextNome.getText().toString();
            String cognome = binding.editTextCognome.getText().toString();
            String username = binding.editTextUsernameRegistrazione.getText().toString();
            String password = binding.editTextPasswordRegistrazione.getText().toString();

            controllaUtente(username, new UtenteEsiste() {
                @Override
                public void onUtenteEsiste(boolean esiste) {
                    if (esiste == true) {
                        registraAccount(nome,cognome,username,password);
                        Toast.makeText(RegistraActivity.this, "Account Registrato", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegistraActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegistraActivity.this, "Username esiste già", Toast.LENGTH_LONG).show();
                    }
                }
            });
        });
    }

    public void controllaUtente(String username, UtenteEsiste utenteEsiste) {
        this.database.getDb().collection("Utenti")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean esiste = false;
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                String username = document.getString("username");
                                if (username.equals(username)) {
                                    esiste = true;
                                    break;
                                }
                            }
                            utenteEsiste.onUtenteEsiste(esiste);
                        }
                    }
                });
    }

    private void registraAccount(String nome, String cognome, String username, String password) {
        CollectionReference collectionReference = this.database.getDb().collection("Utenti");
        Map<String, Object> dato = new HashMap<>();
        dato.put("nome", nome);
        dato.put("cognome", cognome);
        dato.put("username", username);
        dato.put("password", password);

        collectionReference.add(dato)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Utente Registrato", documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.d("Utente non registrato", "Errore");
                });
    }
}