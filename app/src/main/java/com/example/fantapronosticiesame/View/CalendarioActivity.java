package com.example.fantapronosticiesame.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.fantapronosticiesame.Model.Lega;
import com.example.fantapronosticiesame.R;
import com.example.fantapronosticiesame.databinding.ActivityCalendarioBinding;

public class CalendarioActivity extends AppCompatActivity {
    protected ActivityCalendarioBinding binding;
    protected Lega lega = new Lega();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(CalendarioActivity.this, R.layout.activity_calendario);
        String idLega = getIntent().getStringExtra("idLega");

        binding.tabView.setSelectedItemId(R.id.calendario);
        binding.tabView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(CalendarioActivity.this, LegaActivity.class);
                intent.putExtra("lega", lega);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.calendario) {
                Intent intent = new Intent(CalendarioActivity.this, CalendarioActivity.class);
                intent.putExtra("lega", lega);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.classifica) {
                Intent intent = new Intent(CalendarioActivity.this, ClassificaActivity.class);
                intent.putExtra("lega", lega);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}