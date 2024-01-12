package com.example.fantapronosticiesame.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fantapronosticiesame.Adapter.LegaAdapter;
import com.example.fantapronosticiesame.Database.Database;
import com.example.fantapronosticiesame.Interface.LegaAggiunta;
import com.example.fantapronosticiesame.Interface.OnItemClickListener;
import com.example.fantapronosticiesame.Interface.UtenteEsiste;
import com.example.fantapronosticiesame.Interface.UtentePartecipa;
import com.example.fantapronosticiesame.Model.Cookie;
import com.example.fantapronosticiesame.Model.Lega;
import com.example.fantapronosticiesame.Model.User;
import com.example.fantapronosticiesame.R;
import com.example.fantapronosticiesame.databinding.ActivityPartecipaBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartecipaActivity extends AppCompatActivity {

    protected ActivityPartecipaBinding binding;
    protected List<Lega> listaLeghe = new ArrayList<>();
    protected Database database = new Database();
    protected LegaAdapter legaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_partecipa);

        getLeghe(Cookie.getCookieId());
        legaAdapter = new LegaAdapter(listaLeghe, new OnItemClickListener() {
            @Override
            public void onItem(String parola, Lega lega) {
                if (parola.equals("Unisciti")) {
                    User user = new User();
                    user.setId(Cookie.getCookieId());
                    user.setNome(Cookie.getCookieNome());
                    user.setCognome(Cookie.getCookieCognome());
                    user.setUsername(Cookie.getCookieUsername());
                    user.setPassword(Cookie.getCookiePassowrd());
                    lega.getListaPartecipanti().add(user);
                    aggiungiUtenteLegaDatabase(user, lega.getId(), lega.getMaxPartecipanti(), new UtentePartecipa() {
                        @Override
                        public void partecipa(boolean p) {
                            if (p) {
                                Intent intent = new Intent(PartecipaActivity.this, LegaActivity.class);
                                intent.putExtra("lega", lega);
                                startActivity(intent);
                            } else {
                                Toast.makeText(PartecipaActivity.this, "Errore", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        binding.lista.setAdapter(legaAdapter);

        binding.tabView.setSelectedItemId(R.id.partecipa_lega);
        binding.tabView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(PartecipaActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.crea_lega) {
                Intent intent = new Intent(PartecipaActivity.this, CreaLegaActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.partecipa_lega) {
                Intent intent = new Intent(PartecipaActivity.this, PartecipaActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.profilo) {
                Intent intent = new Intent(PartecipaActivity.this, ProfiloActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    public void getLeghe(String idUtente) {
        this.database.getDb().collection("Leghe")
                .orderBy("nomeLega")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            Log.d("lega", document.getId());
                            Lega lega = new Lega();
                            lega.setId(document.getId());
                            lega.setNomeLega(document.getString("nomeLega"));
                            lega.setIdAdminUtente(document.getString("idAdminUtente"));
                            lega.setMaxPartecipanti(Math.toIntExact(document.getLong("maxPartecipanti")));
                            getListaPartecipanti(lega,idUtente, new UtenteEsiste() {
                                @Override
                                public void onUtenteEsiste(boolean esiste) {
                                    if (esiste == false) {
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

    private void getListaPartecipanti(Lega lega,String idUtente, UtenteEsiste utenteEsiste) {
        this.database.getDb().collection("Leghe")
                .document(lega.getId())
                .collection("listaPartecipanti")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean stato = false;
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            if (document.getId().equals(idUtente)) {
                                stato = true;
                                break;
                            }
                        }
                        if (stato == false) {
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
                            utenteEsiste.onUtenteEsiste(false);
                        } else {
                            utenteEsiste.onUtenteEsiste(true);
                        }
                    }
                });
    }

    public void aggiungiUtenteLegaDatabase(User user, String idLega, int maxPartecipanti, UtentePartecipa utentePartecipa) {
        String path = "Leghe/" + idLega + "/listaPartecipanti";
        CollectionReference collectionReference = this.database.getDb().collection(path);
        Map<String, Object> dato = new HashMap<>();
        dato.put("nome", user.getNome());
        dato.put("cognome", user.getCognome());
        dato.put("username", user.getUsername());
        dato.put("password", user.getPassword());

        collectionReference.get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                int size = 0;
                                size = task.getResult().size();
                                if (size == maxPartecipanti ) {
                                    utentePartecipa.partecipa(false);
                                } else {
                                    collectionReference.document(user.getId())
                                            .set(dato)
                                            .addOnSuccessListener(documentReference -> {
                                                Log.d("Partecipante aggiunto", "Documento partecipante aggiunto con successo");
                                                utentePartecipa.partecipa(true);
                                            })
                                            .addOnFailureListener(e -> {
                                                utentePartecipa.partecipa(false);
                                                Log.d("Lega non registrata", "Errore");
                                            });
                                }
                            }
                        });
    }
}