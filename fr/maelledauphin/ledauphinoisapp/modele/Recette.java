package fr.maelledauphin.ledauphinoisapp.modele;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Recette implements Serializable {

    private long id_recette;
    private String nom;
    private Integer duree;
    private Integer nb_personne;
    private Auteur auteur;
    private ArrayList<Ingredient> lesIngredientsRecette = new ArrayList<Ingredient>();

    // Les étapes de réalisation
    private HashMap<Integer,String> lesEtapes = new HashMap<>();


    // ================================================//
    //              Constructeurs
    // ================================================//

    public Recette(long id_recette_e,String nom_e, Integer duree_e, Integer nb_personne_e, Auteur auteur_e){
        this.id_recette = id_recette_e;
        this.nom = nom_e;
        this.duree=duree_e;
        this.nb_personne=nb_personne_e;
        this.auteur = auteur_e;
    }

    public Recette(long id_recette_e,String nom_e, int duree_e, int nb_personne_e){
        this.id_recette = id_recette_e;
        this.nom = nom_e;
        this.duree=duree_e;
        this.nb_personne=nb_personne_e;
    }

    public Recette(){};


    // ================================================//
    //              Getters & Setters
    // ================================================//

    public long getId_recette() {
        return id_recette;
    }

    public void setId_recette(long id_recette) {
        this.id_recette = id_recette;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public int getNb_personne() {
        return nb_personne;
    }

    public void setNb_personne(Integer nb_personne) {
        this.nb_personne = nb_personne;
    }

    public Auteur getAuteur() {
        return auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

    public ArrayList<Ingredient> getLesIngredientsRecette() {
        return lesIngredientsRecette;
    }

    public void setLesIngredientsRecette(ArrayList<Ingredient> lesIngredientsRecette) {
        this.lesIngredientsRecette = lesIngredientsRecette;
    }

    public HashMap<Integer, String> getLesEtapes() {
        return lesEtapes;
    }

    public void setLesEtapes(HashMap<Integer, String> lesEtapes) {
        this.lesEtapes = lesEtapes;
    }

// ================================================//
    //              Autres méthodes
    // ================================================//

    // Permet de formater l'heure
    // (ex : 60 -> 1h ; 70 -> 1h10)
    public String getFormatDuree(){
        String resultat = "";
        int heures = 0;
        int minutes = 0;
        int change_value = this.duree;

        while(change_value>=60){
            heures+=1;
            change_value-=60;
        }
        minutes = change_value;
        if(heures!=0){
            resultat += heures + "h";
        }if(minutes!=0){
            resultat+= minutes;
        }
        return resultat;
    }



    // Génère le nom de l'image à partir du nom de la recette
    // (ex : "Sauté de veau à la Lisbonne" -> "img_saute_de_veau_a_la_lisbonne")
    public String genererNomImage(){
        String nom_courant = this.nom;
        StringBuffer result = new StringBuffer();
        if(nom_courant!=null && nom_courant.length()!=0) {
            int index = -1;
            char c = (char)0;
            String chars= "àâäéèêëîïôöùûüç";
            String replace= "aaaeeeeiioouuuc";
            for(int i=0; i<nom_courant.length(); i++) {
                c = this.nom.charAt(i);
                if( (index=chars.indexOf(c))!=-1 )
                    result.append(replace.charAt(index));
                else
                    result.append(c);
            }
        }
        String nomfinal = result.toString().replaceAll(" ","_").toLowerCase();
        Log.i("Générer nom","Nom généré : " + nomfinal);
        return "img_"+nomfinal;
    }







    @Override
    public String toString() {
        return "Recette{" +
                "id_recette=" + id_recette +
                ", nom='" + nom + '\'' +
                ", duree=" + duree +
                ", nb_personne=" + nb_personne +
                ", auteur=" + auteur +
                ", lesIngredientsRecette=" + lesIngredientsRecette +
                ", lesEtapes=" + lesEtapes +
                '}';
    }
}
