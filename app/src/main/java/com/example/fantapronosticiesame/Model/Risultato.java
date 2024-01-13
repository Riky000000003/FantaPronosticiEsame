package com.example.fantapronosticiesame.Model;

public class Risultato {
    private int id;
    private String risultato;
    public Risultato () {}

    public Risultato(int id, String risultato) {
        this.id = id;
        this.risultato = risultato;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getRisultato() {
        return risultato;
    }
    public void setRisultato(String risultato) {
        this.risultato = risultato;
    }
}
