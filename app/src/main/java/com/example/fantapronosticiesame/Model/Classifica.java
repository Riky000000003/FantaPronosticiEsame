package com.example.fantapronosticiesame.Model;

public class Classifica {
    private String username;
    private int partiteGiocate;
    private double puntiTotalizzati;

    public Classifica() {}
    public Classifica(String username, int partiteGiocate, double puntiTotalizzati) {
        this.username = username;
        this.partiteGiocate = partiteGiocate;
        this.puntiTotalizzati = puntiTotalizzati;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPartiteGiocate() {
        return partiteGiocate;
    }

    public void setPartiteGiocate(int partiteGiocate) {
        this.partiteGiocate = partiteGiocate;
    }

    public double getPuntiTotalizzati() {
        return puntiTotalizzati;
    }

    public void setPuntiTotalizzati(double puntiTotalizzati) {
        this.puntiTotalizzati = puntiTotalizzati;
    }
}
