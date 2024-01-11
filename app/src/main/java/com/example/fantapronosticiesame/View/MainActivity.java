package com.example.fantapronosticiesame.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fantapronosticiesame.Database.Database;
import com.example.fantapronosticiesame.Model.Cookie;
import com.example.fantapronosticiesame.Model.User;
import com.example.fantapronosticiesame.R;
import com.example.fantapronosticiesame.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected ActivityMainBinding binding;
    protected Database database = new Database();
    protected List<User> listaUtenti = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        listaUtenti = database.getUtenti();

        binding.buttonRegistra.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistraActivity.class);
            startActivity(intent);
        });

        binding.buttonLogin.setOnClickListener(v -> {
            String username = binding.editTextUsername.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            if (login(username,password,listaUtenti)) {
                Toast.makeText(this, "Utente Loggato", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Utente non Loggato", Toast.LENGTH_LONG).show();
            }
        });

    }

    public boolean login(String username, String password, List<User> utenti) {
        for (User user: utenti) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                Cookie.setCookieId(user.getId());
                Cookie.setCookieNome(user.getNome());
                Cookie.setCookieCognome(user.getCognome());
                Cookie.setCookieUsername(user.getUsername());
                Cookie.setCookiePassowrd(user.getPassword());
                return true;
            }
        }
        return false;
    }
}