package com.example.fantapronosticiesame.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.fantapronosticiesame.Database.Database;
import com.example.fantapronosticiesame.Model.Lega;
import com.example.fantapronosticiesame.R;
import com.example.fantapronosticiesame.databinding.ActivityPartecipaBinding;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PartecipaActivity extends AppCompatActivity {

    protected ActivityPartecipaBinding binding;
    protected List<Lega> listaLeghe = new ArrayList<>();
    protected Database database = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_partecipa);

        getLeghe();

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
                    }
                });
    }
}