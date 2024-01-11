package com.example.fantapronosticiesame.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.fantapronosticiesame.Adapter.LegaAdapter;
import com.example.fantapronosticiesame.Database.Database;
import com.example.fantapronosticiesame.Interface.OnItemClickListener;
import com.example.fantapronosticiesame.Interface.UtenteEsiste;
import com.example.fantapronosticiesame.Model.Cookie;
import com.example.fantapronosticiesame.Model.Lega;
import com.example.fantapronosticiesame.Model.User;
import com.example.fantapronosticiesame.R;
import com.example.fantapronosticiesame.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    protected ActivityHomeBinding binding;
    protected Database database = new Database();
    protected List<Lega> listaLeghe = new ArrayList<>();
    protected LegaAdapter legaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        getLeghe();
        legaAdapter = new LegaAdapter(listaLeghe, new OnItemClickListener() {
            @Override
            public void onItem(String parola, Lega lega) {
                if (parola.equals("Entra")) {
                    Intent intent = new Intent(HomeActivity.this, LegaActivity.class);
                    intent.putExtra("lega", lega);
                    startActivity(intent);
                }
            }
        });
        binding.listaLeghe.setAdapter(legaAdapter);

        binding.tabView.setSelectedItemId(R.id.home);
        binding.tabView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.crea_lega) {
                Intent intent = new Intent(HomeActivity.this, CreaLegaActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.partecipa_lega) {
                Intent intent = new Intent(HomeActivity.this, CreaLegaActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.profilo) {
                Intent intent = new Intent(HomeActivity.this, ProfiloActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });


    }

    public void getLeghe() {
        this.database.getDb().collection("Leghe")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("FireStore", "Errore");
                            return;
                        }
                        listaLeghe.clear();
                        for (DocumentSnapshot documentSnapshot: value) {
                            Lega lega = new Lega();
                            lega.setNomeLega(documentSnapshot.getString("nomeLega"));
                            lega.setIdAdminUtente(documentSnapshot.getString("idAdminUtente"));
                            lega.setId(documentSnapshot.getId());
                            lega.setMaxPartecipanti(Math.toIntExact(documentSnapshot.getLong("maxPartecipanti")));
                            getListaPartecipantiForLega(lega, database.getDb(), new UtenteEsiste() {
                                @Override
                                public void onUtenteEsiste(boolean esiste) {
                                    if (esiste == true) {
                                        listaLeghe.add(lega);
                                    }
                                }
                            });
                        }
                        legaAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void getListaPartecipantiForLega(Lega lega, FirebaseFirestore db, UtenteEsiste utenteEsiste) {
        db.collection("Leghe").document(lega.getId())
                .collection("listaPartecipanti")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean stato = false;
                            lega.getListaPartecipanti().clear();
                            for (QueryDocumentSnapshot partecipanteSnapshot : task.getResult()) {
                                if (partecipanteSnapshot.getId().equals(Cookie.getCookieId())) {
                                    stato = true;
                                    User utente = new User();
                                    utente.setId(partecipanteSnapshot.getId());
                                    utente.setNome(partecipanteSnapshot.getString("nome"));
                                    utente.setCognome(partecipanteSnapshot.getString("cognome"));
                                    utente.setUsername(partecipanteSnapshot.getString("username"));
                                    utente.setPassword(partecipanteSnapshot.getString("password"));
                                    lega.getListaPartecipanti().add(utente);
                                }
                            }
                            utenteEsiste.onUtenteEsiste(stato);
                            legaAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("FireStore", "Errore durante l'ottenimento della listaPartecipanti", task.getException());
                        }
                    }
                });
    }
}