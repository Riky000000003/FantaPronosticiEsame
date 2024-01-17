package com.example.fantapronosticiesame.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.fantapronosticiesame.Database.Database;
import com.example.fantapronosticiesame.Interface.GiornataCreata;
import com.example.fantapronosticiesame.Model.Classifica;
import com.example.fantapronosticiesame.Model.Giornata;
import com.example.fantapronosticiesame.Model.Lega;
import com.example.fantapronosticiesame.Model.Partita;
import com.example.fantapronosticiesame.Model.Risultato;
import com.example.fantapronosticiesame.Model.User;
import com.example.fantapronosticiesame.R;
import com.example.fantapronosticiesame.databinding.ActivityClassificaBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClassificaActivity extends AppCompatActivity {
    protected ActivityClassificaBinding binding;
    protected Lega lega = new Lega();
    protected Database database = new Database();
    protected List<Classifica> listaClassifica = new ArrayList<>();
    protected List<Giornata> giornate = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int i = 0;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_classifica);
        String idLega = getIntent().getStringExtra("idLega");

        getLegaById(idLega);
        getListaGiornate(idLega, new GiornataCreata() {
            @Override
            public void giornata(boolean completato) {
                creaClassifica(giornate);
            }
        });

        for (Classifica classifica: listaClassifica) {
            i++;
            TableRow tr = new TableRow(ClassificaActivity.this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setPadding(20,20,20,20);

            TextView pos = new TextView(ClassificaActivity.this);
            pos.setTextColor(Color.BLACK);
            pos.setTypeface(null, Typeface.BOLD);
            pos.setTextSize(16);
            pos.setText(i);
            tr.addView(pos);

            TextView utente = new TextView(ClassificaActivity.this);
            utente.setTextColor(Color.BLACK);
            utente.setTypeface(null, Typeface.BOLD);
            utente.setTextSize(16);
            utente.setText(classifica.getUsername());
            tr.addView(utente);

            TextView partiteGiocate = new TextView(ClassificaActivity.this);
            partiteGiocate.setTextColor(Color.BLACK);
            partiteGiocate.setTypeface(null, Typeface.BOLD);
            partiteGiocate.setTextSize(16);
            partiteGiocate.setText(classifica.getPartiteGiocate());
            tr.addView(partiteGiocate);

            TextView punti = new TextView(ClassificaActivity.this);
            punti.setTextColor(Color.BLACK);
            punti.setTypeface(null, Typeface.BOLD);
            punti.setTextSize(16);
            punti.setText(classifica.getUsername());
            tr.addView(punti);

            runOnUiThread(() -> {
                binding.tabella.addView(tr);
            });
        }

        binding.tabView.setSelectedItemId(R.id.classifica);
        binding.tabView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(ClassificaActivity.this, LegaActivity.class);
                intent.putExtra("idLega", lega.getId());
                startActivity(intent);
                return true;
            } else if (itemId == R.id.calendario) {
                Intent intent = new Intent(ClassificaActivity.this, CalendarioActivity.class);
                intent.putExtra("idLega", lega.getId());
                startActivity(intent);
                return true;
            } else if (itemId == R.id.classifica) {
                Intent intent = new Intent(ClassificaActivity.this, ClassificaActivity.class);
                intent.putExtra("idLega", lega.getId());            startActivity(intent);
                return true;
            }
            return false;
        });
    }

    public void getLegaById(String idLega) {
        this.database.getDb().collection("Leghe")
                .document(idLega)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String id = task.getResult().getId();
                        String nomeLega = task.getResult().getString("nomeLega");
                        String idAdminUtente = task.getResult().getString("idAdminUtente");
                        int maxParteicpanti = Math.toIntExact(task.getResult().getLong("maxPartecipanti"));
                        lega.setNomeLega(nomeLega);
                        lega.setId(id);
                        lega.setMaxPartecipanti(maxParteicpanti);
                        lega.setIdAdminUtente(idAdminUtente);
                        Log.d("Lega creata", lega.getId() +" " + lega.getNomeLega());
                        /*runOnUiThread(() -> {
                            binding.titoloLega.setText(lega.getNomeLega());
                        }); */
                        getListaPartecipantiLega(lega.getId());
                    }
                });
    }

    private void getListaPartecipantiLega(String id) {
        this.database.getDb().collection("Leghe")
                .document(id)
                .collection("listaPartecipanti")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            String idUtente = document.getId();
                            String nome = document.getString("nome");
                            String cognome = document.getString("cognome");
                            String username = document.getString("username");
                            String password = document.getString("password");
                            User user = new User(idUtente,nome,cognome, username, password);
                            lega.getListaPartecipanti().add(user);
                            Log.d("utente inserito", "" +lega.getListaPartecipanti().size());
                        }
                    }
                });
    }

    public void getListaGiornate(String idLega, GiornataCreata giornataCreata) {
        this.database.getDb().collection("Leghe")
                .document(idLega)
                .collection("Giornate")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                Giornata giornata = new Giornata();
                                giornata.setId(document.getId());
                                giornata.setNomeGiornata(document.getString("nomeGiornata"));
                                getPartite(giornata, idLega, new GiornataCreata() {
                                    @Override
                                    public void giornata(boolean completato) {
                                        if (completato) {
                                            giornate.add(giornata);
                                            Log.d("Lista", "size: " + giornate.size());
                                        }
                                    }
                                });
                            }
                            giornataCreata.giornata(true);
                        }
                    } else {
                        giornataCreata.giornata(false);
                    }
                });
    }

    private void getPartite(Giornata giornata, String idLega, GiornataCreata giornataCreata) {
        this.database.getDb().collection("Leghe")
                .document(idLega)
                .collection("Giornate")
                .document(giornata.getId())
                .collection("Partite")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                Partita partita = new Partita();
                                partita.setId(document.getId());
                                partita.setIdUtente(document.getString("utente"));
                                partita.setPuntiTotalizzati(document.getDouble("puntiTotalizzati"));
                                List<String> array = (List<String>) document.get("risultati");
                                for (String risultato: array) {
                                    Risultato ris = new Risultato(risultato);
                                    partita.getRisultati().add(ris);
                                }
                                giornata.getPartite().add(partita);
                                Log.d("Giornata creata", giornata.getId());
                            }
                            giornataCreata.giornata(true);
                        }
                    } else {
                        giornataCreata.giornata(false);
                    }
                });
    }

    public void creaClassifica(List<Giornata> giornate) {
        Log.d("Classifica", "size: " + giornate.size());
        for (Giornata giornata: giornate) {
            if (giornata.getPartite().size() > 0) {
                for (Partita partita: giornata.getPartite()) {
                    Classifica classifica = new Classifica();
                    classifica.setPartiteGiocate(giornate.size());
                    classifica.setUsername(partita.getIdUtente());
                    classifica.setPuntiTotalizzati(partita.getPuntiTotalizzati());
                    Log.d("Classifica", "utente: " + classifica.getUsername());
                    listaClassifica.add(classifica);
                }
            }
        }
    }


}