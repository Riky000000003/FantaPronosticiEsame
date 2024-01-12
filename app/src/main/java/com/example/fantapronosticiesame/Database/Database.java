package com.example.fantapronosticiesame.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fantapronosticiesame.Interface.UtenteEsiste;
import com.example.fantapronosticiesame.Model.Lega;
import com.example.fantapronosticiesame.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private FirebaseFirestore db;
    public Database() {
        this.db = FirebaseFirestore.getInstance();
    }
    public FirebaseFirestore getDb() {
        return db;
    }

    public List<User> getUtenti() {
        List<User> listaUtenti = new ArrayList<>();
        this.db.collection("Utenti")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.d("mio FireStore", document.getId() + " => " + document.getData());
                            String id = document.getId();
                            String nome = document.getString("nome");
                            String cognome = document.getString("cognome");
                            String username = document.getString("username");
                            String password = document.getString("password");
                            User utente = new User(id,nome, cognome, username, password);
                            listaUtenti.add(utente);
                        }
                    } else {
                        Log.w("mioFirestore", "Error getting documents.", task.getException());
                    }
                });
        return listaUtenti;
    }
}
