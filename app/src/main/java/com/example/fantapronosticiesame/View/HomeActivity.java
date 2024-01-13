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
        getListaLeghe(Cookie.getCookieId());
        legaAdapter = new LegaAdapter(listaLeghe, new OnItemClickListener() {
            @Override
            public void onItem(String parola, Lega lega) {
                if (parola.equals("Entra")) {
                    Intent intent = new Intent(HomeActivity.this, LegaActivity.class);
                    intent.putExtra("idLega", lega.getId());
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
                Intent intent = new Intent(HomeActivity.this, PartecipaActivity.class);
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
    public void getListaLeghe (String idUtente) {
        this.database.getDb().collection("Leghe")
                .orderBy("nomeLega")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            Lega lega = new Lega();
                            lega.setId(document.getId());
                            lega.setNomeLega(document.getString("nomeLega"));
                            lega.setIdAdminUtente(document.getString("idAdminUtente"));
                            lega.setMaxPartecipanti(Math.toIntExact(document.getLong("maxPartecipanti")));
                            getListaPartecipanti(lega, idUtente, new UtenteEsiste() {
                                @Override
                                public void onUtenteEsiste(boolean esiste) {
                                    if (esiste) {
                                        listaLeghe.add(lega);
                                        Log.d("lega aggiunta", ""+ listaLeghe.size());
                                        legaAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void getListaPartecipanti(Lega lega, String idUtente, UtenteEsiste utenteEsiste) {
        this.database.getDb().collection("Leghe")
                .document(lega.getId())
                .collection("listaPartecipanti")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean stato = false;
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            if (document.getId().equals(idUtente)) {
                                Log.d("Utente esiste", document.getId());
                                stato = true;
                                break;
                            }
                        }
                        if (stato) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                User user = new User();
                                user.setId(document.getId());
                                user.setNome(document.getString("nome"));
                                user.setCognome(document.getString("cognome"));
                                user.setUsername(document.getString("username"));
                                user.setPassword(document.getString("password"));
                                lega.getListaPartecipanti().add(user);
                                Log.d("Utente aggiunto", document.getId());
                            }
                            utenteEsiste.onUtenteEsiste(stato);
                        } else {
                            utenteEsiste.onUtenteEsiste(stato);
                        }
                    }
                });
    }
}