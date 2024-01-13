package com.example.fantapronosticiesame.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.fantapronosticiesame.Model.Lega;
import com.example.fantapronosticiesame.R;
import com.example.fantapronosticiesame.databinding.ActivityClassificaBinding;

public class ClassificaActivity extends AppCompatActivity {
    protected ActivityClassificaBinding binding;
    protected Lega lega = new Lega();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_classifica);
        String idLega = getIntent().getStringExtra("idLega");

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
}