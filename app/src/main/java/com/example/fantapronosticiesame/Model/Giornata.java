package com.example.fantapronosticiesame.Model;

import java.util.ArrayList;
import java.util.List;

public class Giornata {
    private String id;
    private String nomeGiornata;
    private List<Partita> partite;
    public Giornata() {
        this.partite = new ArrayList<>();
    }
    public Giornata(String id, String nomeGiornata) {
        this.id = id;
        this.nomeGiornata = nomeGiornata;
        this.partite = new ArrayList<>();
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNomeGiornata() {
        return nomeGiornata;
    }
    public void setNomeGiornata(String nomeGiornata) {
        this.nomeGiornata = nomeGiornata;
    }
    public List<Partita> getPartite() {
        return partite;
    }
}
