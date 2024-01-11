package com.example.fantapronosticiesame.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.fantapronosticiesame.Database.Database;
import com.example.fantapronosticiesame.Model.Cookie;
import com.example.fantapronosticiesame.Model.User;
import com.example.fantapronosticiesame.R;
import com.example.fantapronosticiesame.databinding.ActivityProfiloBinding;

public class ProfiloActivity extends AppCompatActivity {
    protected ActivityProfiloBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profilo);

        binding.profiloNome.setText(Cookie.getCookieNome());
        binding.profiloCognome.setText(Cookie.getCookieCognome());
        binding.profiloUsername.setText(Cookie.getCookieUsername());
        binding.profiloPassword.setText(Cookie.getCookiePassowrd());

        binding.buttonDisconettiti.setOnClickListener(v -> {
            Cookie.setCookieNome("");
            Cookie.setCookieCognome("");
            Cookie.setCookieUsername("");
            Cookie.setCookiePassowrd("");
            Cookie.setCookieId("");
            Intent intent = new Intent(ProfiloActivity.this, MainActivity.class);
            startActivity(intent);
        });


        binding.tabView.setSelectedItemId(R.id.crea_lega);
        binding.tabView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(ProfiloActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.crea_lega) {
                Intent intent = new Intent(ProfiloActivity.this, CreaLegaActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.partecipa_lega) {
                Intent intent = new Intent(ProfiloActivity.this, CreaLegaActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.profilo) {
                String nome = Cookie.getCookieId();
                String cognome = Cookie.getCookieCognome();
                String username = Cookie.getCookieUsername();
                String password = Cookie.getCookiePassowrd();
                User user = new User(nome, cognome, username, password);
                Intent intent = new Intent(ProfiloActivity.this, ProfiloActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}