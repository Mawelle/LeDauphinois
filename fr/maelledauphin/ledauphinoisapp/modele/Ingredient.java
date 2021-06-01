package fr.maelledauphin.ledauphinoisapp.modele;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Ingredient implements Serializable, Cloneable {
    private int id;
    private String nom;
    private int quantite;
    private UniteMesure UniteMesure;

    public Ingredient(int id_e,String nom_e){
        this.id = id_e;
        this.nom = nom_e;
    }

    public Ingredient(String nom_e){
        this.nom = nom_e;
    }

    public Ingredient(){};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public UniteMesure getUniteMesure() {
        return UniteMesure;
    }

    public void setUniteMesure(UniteMesure uniteMesure) {
        UniteMesure = uniteMesure;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toStringAll() {
        return "Ingredient{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return this.nom;
    }

    // Permet de transformer les quantités en version plus lisible
    // (ex : 1.0 -> 1   ;   5.6 -> 5,5)
    public String formatQuantite(){
        String res="";
        int y;
        y = (int)this.quantite;
        if (y==this.quantite){
            res = ""+y;
        }else{
            String tmp = "" + this.quantite;
            res = tmp.replaceAll("\\.",",");
        }
        return res;
    }
}
