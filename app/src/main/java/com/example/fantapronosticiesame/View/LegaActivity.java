package com.example.fantapronosticiesame.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.fantapronosticiesame.Model.Lega;
import com.example.fantapronosticiesame.R;
import com.example.fantapronosticiesame.databinding.ActivityLegaBinding;

public class LegaActivity extends AppCompatActivity {
    protected ActivityLegaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lega);
        Lega lega = (Lega) getIntent().getSerializableExtra("lega");
        binding.titoloLega.setText(lega.getNomeLega());
    }
}