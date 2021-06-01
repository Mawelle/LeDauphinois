package fr.maelledauphin.ledauphinoisapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.maelledauphin.ledauphinoisapp.R;
import fr.maelledauphin.ledauphinoisapp.modele.EtapeRealisation;
import fr.maelledauphin.ledauphinoisapp.modele.Ingredient;
import fr.maelledauphin.ledauphinoisapp.modele.IngredientRecette;
import fr.maelledauphin.ledauphinoisapp.modele.Recette;
import fr.maelledauphin.ledauphinoisapp.modele.UniteMesure;
import fr.maelledauphin.ledauphinoisapp.modele.bdd.BDHelper;
import fr.maelledauphin.ledauphinoisapp.modele.bdd.GestionBD;
import fr.maelledauphin.ledauphinoisapp.modele.bdd.RecupJSON;

public class DetailRecette extends AppCompatActivity {
    GestionBD sgbd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recette);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sgbd = new GestionBD(this);

        Intent extras = getIntent();
        Recette uneRecette = (Recette) extras.getSerializableExtra("uneRecette");
        Log.i("P2", "Recette récup : " + uneRecette.toString());

        sgbd.open();
        // Récupération des données : Ingrédients & étapes
        ArrayList<Ingredient> lesIngreRec = new ArrayList<>();
        for (IngredientRecette unIngreRec : sgbd.rechercherIngredientRecette(uneRecette.getId_recette(), "id_recette")) {
            Log.i("Info ", "" + unIngreRec.getId_recette());
            Ingredient currentIngre = null;
            try {
                currentIngre = (Ingredient) sgbd.rechercherIngredient(unIngreRec.getId_ingredient(), "id_ingredient").clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            currentIngre.setQuantite(unIngreRec.getQuantite());
            currentIngre.setUniteMesure(sgbd.rechercherUniteMesure(unIngreRec.getId_unite_mesure(),"id_unite_mesure"));
            lesIngreRec.add(currentIngre);
        }
        uneRecette.setLesIngredientsRecette(lesIngreRec);


        HashMap<Integer,String> uneSerieEtapes = new HashMap<>();
        for(EtapeRealisation uneEtapeRea : sgbd.rechercherListeEtapesRealisation(uneRecette.getId_recette(),"id_recette")){
                uneSerieEtapes.put(uneEtapeRea.getId_etape_realisation(), uneEtapeRea.getDescription());
        }
        uneRecette.setLesEtapes(uneSerieEtapes);

        TextView tvNom = findViewById(R.id.txtNom);
        TextView tvDuree = findViewById(R.id.tv_duree);
        TextView tvNb_personne = findViewById(R.id.tv_nb_personne);
        TextView tvAuteur = findViewById(R.id.tv_auteur);
        tvNom.setText(uneRecette.getNom());
        tvDuree.setText(uneRecette.getFormatDuree());
        tvNb_personne.setText(uneRecette.getNb_personne() + " personne(s)");
        tvAuteur.setText("Proposé par : " + uneRecette.getAuteur().getPrenom());
        ImageView imgP = findViewById(R.id.iv_photoRecette);
        int unDrawable = getResources().getIdentifier(""+uneRecette.genererNomImage(), "drawable", getPackageName());
        imgP.setImageResource(unDrawable);

        // Afficher la liste des ingrédients
        LinearLayout llIngredients = (LinearLayout) findViewById(R.id.ll_ingredients);
        for(Ingredient unIngredient : uneRecette.getLesIngredientsRecette()){
            TextView textView = new TextView(this);
            textView.setText("• " + unIngredient.formatQuantite() + " " + unIngredient.getUniteMesure().getTypeMesure() + " : "+ unIngredient.getNom());
            llIngredients.addView(textView);
        }



        //Afficher les étapes de réalisation de la recette
        LinearLayout llNumEtapes = (LinearLayout) findViewById(R.id.ll_numEtapeRecette);
        for(Map.Entry uneDonnee : uneRecette.getLesEtapes().entrySet() ){
            TextView tvNumEtape = new TextView(this);
            TextView tvDescriptionEtape = new TextView(this);
            tvNumEtape.setText("Étape " +uneDonnee.getKey() +" : ");
            tvDescriptionEtape.setText(""+uneDonnee.getValue());
            if((int)uneDonnee.getKey()!=1){
                tvNumEtape.setPadding(0,80,0,0);
            }
            tvDescriptionEtape.setPadding(80,0,0,0);
            llNumEtapes.addView(tvNumEtape);
            llNumEtapes.addView(tvDescriptionEtape);
        }
    }
}