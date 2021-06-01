package fr.maelledauphin.ledauphinoisapp.modele;

public class IngredientRecette {
    private long id_recette;
    private int id_ingredient;
    private int quantite;
    private int id_unite_mesure;


    public IngredientRecette(long id_recette_e, int id_ingredient_e, int quantite_e, int id_unite_mesure_e){
        this.id_recette = id_recette_e;
        this.id_ingredient = id_ingredient_e;
        this.quantite = quantite_e;
        this.id_unite_mesure = id_unite_mesure_e;
    }

    public IngredientRecette(){}

    public long getId_recette() {
        return id_recette;
    }

    public void setId_recette(long id_recette) {
        this.id_recette = id_recette;
    }

    public int getId_ingredient() {
        return id_ingredient;
    }

    public void setId_ingredient(int id_ingredient) {
        this.id_ingredient = id_ingredient;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getId_unite_mesure() {
        return id_unite_mesure;
    }

    public void setId_unite_mesure(int id_unite_mesure) {
        this.id_unite_mesure = id_unite_mesure;
    }

    @Override
    public String toString() {
        return "IngredientRecette{" +
                "id_recette=" + id_recette +
                ", id_ingredient=" + id_ingredient +
                ", quantite=" + quantite +
                ", id_unite_mesure=" + id_unite_mesure +
                '}';
    }
}
