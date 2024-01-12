package com.example.fantapronosticiesame.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fantapronosticiesame.Database.Database;
import com.example.fantapronosticiesame.Interface.LegaAggiunta;
import com.example.fantapronosticiesame.Model.Cookie;
import com.example.fantapronosticiesame.R;
import com.example.fantapronosticiesame.databinding.ActivityCreaLegaBinding;
import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;
import java.util.Map;

public class CreaLegaActivity extends AppCompatActivity {
    protected ActivityCreaLegaBinding binding;
    protected Database database = new Database();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crea_lega);

        binding.buttonResetLega.setOnClickListener(v -> {
            binding.editMaxPartecipanti.setText("");
            binding.editNomeLega.setText("");
        });

        binding.tabView.setSelectedItemId(R.id.crea_lega);
        binding.tabView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(CreaLegaActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.crea_lega) {
                Intent intent = new Intent(CreaLegaActivity.this, CreaLegaActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.partecipa_lega) {
                Intent intent = new Intent(CreaLegaActivity.this, PartecipaActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.profilo) {
                Intent intent = new Intent(CreaLegaActivity.this, ProfiloActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        binding.buttonCreaLega.setOnClickListener(v -> {
            int maxPartecipanti = Integer.parseInt(binding.editMaxPartecipanti.getText().toString());
            String nomeLega = binding.editNomeLega.getText().toString();
            aggiungiLega(nomeLega, maxPartecipanti, new LegaAggiunta() {
                @Override
                public void onLegaInserita(boolean aggiunto) {
                    if (aggiunto) {
                        Toast.makeText(CreaLegaActivity.this, "Lega Aggiunta", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreaLegaActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CreaLegaActivity.this, "Lega non Aggiunta", Toast.LENGTH_LONG).show();
                    }
                }
            });
        });
    }

    public void aggiungiLega(String nomeLega, int maxPartecipanti, LegaAggiunta legaAggiunta) {
        CollectionReference collectionReference = this.database.getDb().collection("Leghe");
        Map<String, Object> dato = new HashMap<>();
        dato.put("nomeLega", nomeLega);
        dato.put("idAdminUtente", Cookie.getCookieId());
        dato.put("maxPartecipanti", maxPartecipanti);


        collectionReference.add(dato)
                .addOnSuccessListener(documentReference -> {
                    String idDocumento = documentReference.getId();
                    String path = "Leghe/" + idDocumento + "/listaPartecipanti";
                    Log.d("PATH", path);
                    CollectionReference collectionReference2 = this.database.getDb().collection(path);
                    Map<String, Object> dato2 = new HashMap<>();
                    dato2.put("nome", Cookie.getCookieNome());
                    dato2.put("cognome", Cookie.getCookieCognome());
                    dato2.put("username", Cookie.getCookieUsername());
                    dato2.put("password", Cookie.getCookiePassowrd());
                    collectionReference2.document(Cookie.getCookieId())
                            .set(dato2)
                            .addOnSuccessListener(documentReference2 -> {
                                Log.d("Partecipante aggiunto", "Documento partecipante aggiunto con successo");
                                legaAggiunta.onLegaInserita(true);
                            })
                            .addOnFailureListener(e -> {
                                legaAggiunta.onLegaInserita(false);
                                Log.d("Errore", "Errore durante l'aggiunta del partecipante");
                            });
                })
                .addOnFailureListener(e -> {
                    Log.d("Lega non registrata", "Errore");
                });
    }
}