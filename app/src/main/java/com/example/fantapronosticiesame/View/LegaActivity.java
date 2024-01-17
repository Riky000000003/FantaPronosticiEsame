package com.example.fantapronosticiesame.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.fantapronosticiesame.Database.Database;
import com.example.fantapronosticiesame.Model.Lega;
import com.example.fantapronosticiesame.Model.User;
import com.example.fantapronosticiesame.R;
import com.example.fantapronosticiesame.databinding.ActivityLegaBinding;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LegaActivity extends AppCompatActivity {
    protected ActivityLegaBinding binding;
    protected Database database = new Database();
    protected Lega lega = new Lega();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lega);
        String idLega = getIntent().getStringExtra("idLega");
        getLegaById(idLega);

        binding.tabView.setSelectedItemId(R.id.home);
        binding.tabView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(LegaActivity.this, LegaActivity.class);
                intent.putExtra("idLega", lega.getId());
                startActivity(intent);
                return true;
            } else if (itemId == R.id.calendario) {
                Intent intent = new Intent(LegaActivity.this, CalendarioActivity.class);
                intent.putExtra("idLega", lega.getId());
                startActivity(intent);
                return true;
            } else if (itemId == R.id.classifica) {
                Intent intent = new Intent(LegaActivity.this, ClassificaActivity.class);
                intent.putExtra("idLega", lega.getId());
                startActivity(intent);
                return true;
            }
            return false;
        });

        binding.buttonLega.setOnClickListener(v -> {
            Intent intent = new Intent(LegaActivity.this, HomeActivity.class);
            startActivity(intent);
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
                        runOnUiThread(() -> {
                            binding.titoloLega.setText(lega.getNomeLega());
                        });
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

}