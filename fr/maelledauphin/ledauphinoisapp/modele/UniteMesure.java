package fr.maelledauphin.ledauphinoisapp.modele;

import java.io.Serializable;

public class UniteMesure implements Serializable {
    private int id;
    private String libelle;
    private String typeMesure;

    public UniteMesure(int id_e,String libelle_e, String typeMesure_e){
        this.id = id_e;
        this.libelle = libelle_e;
        this.typeMesure = typeMesure_e;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTypeMesure() {
        return typeMesure;
    }

    public void setTypeMesure(String typeMesure) {
        this.typeMesure = typeMesure;
    }

    public String toStringAll() {
        return "UniteMesure{" +
                "id=" + id +
                ", libelle='" + this.libelle + '\'' +
                ", typeMesure='" + this.typeMesure + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return this.typeMesure;
    }
}
