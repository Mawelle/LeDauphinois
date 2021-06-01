package fr.maelledauphin.ledauphinoisapp.modele;

import java.util.HashMap;

public class EtapeRealisation {
    private int id_etape_realisation;
    private long  id_recette;
    private String description;

    public EtapeRealisation(int id_etape_realisation_e, long id_recette_e, String description_e){
        this.id_etape_realisation = id_etape_realisation_e;
        this.id_recette = id_recette_e;
        this.description = description_e;
    }

    public EtapeRealisation(){};

    public int getId_etape_realisation() {
        return id_etape_realisation;
    }

    public void setId_etape_realisation(int id_etape_realisation) {
        this.id_etape_realisation = id_etape_realisation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId_recette() {
        return id_recette;
    }

    public void setId_recette(long id_recette) {
        this.id_recette = id_recette;
    }

    @Override
    public String toString() {
        return "EtapeRealisation{" +
                "id_etape_realisation=" + id_etape_realisation +
                ", id_recette=" + id_recette +
                ", description='" + description + '\'' +
                '}';
    }
}
