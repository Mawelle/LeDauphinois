package fr.maelledauphin.ledauphinoisapp.modele;

import java.io.Serializable;

public class Auteur implements Serializable {
    int id;
    String nom;
    String prenom;

    public Auteur(int id_e,String nom_e, String prenom_e){
        this.id = id_e;
        this.nom = nom_e;
        this.prenom = prenom_e;
    }

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public String toString() {
        return "Auteur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }
}
