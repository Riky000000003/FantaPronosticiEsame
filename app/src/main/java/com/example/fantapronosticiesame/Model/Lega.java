package com.example.fantapronosticiesame.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Lega implements Serializable {
    private String id;
    private String nomeLega;
    private String idAdminUtente;
    private int maxPartecipanti;
    private List<User> listaPartecipanti;
    public Lega() {
        this.listaPartecipanti = new ArrayList<>();
    }
    public Lega(String id, String nomeLega, String idAdminUtente, int maxPartecipanti) {
        this.id = id;
        this.nomeLega = nomeLega;
        this.idAdminUtente = idAdminUtente;
        this.maxPartecipanti = maxPartecipanti;
        this.listaPartecipanti = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeLega() {
        return nomeLega;
    }

    public void setNomeLega(String nomeLega) {
        this.nomeLega = nomeLega;
    }

    public String getIdAdminUtente() {
        return idAdminUtente;
    }

    public void setIdAdminUtente(String idAdminUtente) {
        this.idAdminUtente = idAdminUtente;
    }

    public int getMaxPartecipanti() {
        return maxPartecipanti;
    }

    public void setMaxPartecipanti(int maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
    }

    public List<User> getListaPartecipanti() {
        return listaPartecipanti;
    }

    public void setListaPartecipanti(List<User> listaPartecipanti) {
        this.listaPartecipanti = listaPartecipanti;
    }

    public int numPartecipanti() {
        return this.listaPartecipanti.size();
    }
}
