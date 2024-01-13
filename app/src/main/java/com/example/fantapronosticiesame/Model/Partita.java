package com.example.fantapronosticiesame.Model;

import java.util.ArrayList;
import java.util.List;

public class Partita {
    private String id;
    private String idUtente;
    private double puntiTotalizzati;
    private List<Risultato> risultati;
    public Partita() {
        this.risultati = new ArrayList<>();
    }
    public Partita(String id, String idUtente) {
        this.id = id;
        this.idUtente = idUtente;
        this.risultati = new ArrayList<>();
    }

    public List<Risultato> getRisultati() {
        return risultati;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(String idUtente) {
        this.idUtente = idUtente;
    }

    public double getPuntiTotalizzati() {
        return puntiTotalizzati;
    }

    public void setPuntiTotalizzati(double puntiTotalizzati) {
        this.puntiTotalizzati = puntiTotalizzati;
    }
}
