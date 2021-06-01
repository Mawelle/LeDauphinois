package fr.maelledauphin.ledauphinoisapp.ui.utilisateur;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.maelledauphin.ledauphinoisapp.R;
import fr.maelledauphin.ledauphinoisapp.modele.EtapeRealisation;
import fr.maelledauphin.ledauphinoisapp.modele.IngredientRecette;
import fr.maelledauphin.ledauphinoisapp.modele.Recette;
import fr.maelledauphin.ledauphinoisapp.modele.bdd.GestionBD;
import fr.maelledauphin.ledauphinoisapp.modele.bdd.RecupJSON;
import fr.maelledauphin.ledauphinoisapp.modele.UniteMesure;
import fr.maelledauphin.ledauphinoisapp.modele.Ingredient;

public class ModifierRecetteActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llIngredients;
    Button btnAjouterIngredient;
    LinearLayout llEtape;
    Button btnAjouterEtape;
    GestionBD sgbd;
    Recette uneRecette;
    ArrayList<Ingredient> lesIngre = new ArrayList<>();
    HashMap<Integer,String> lesEtapes = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_recette);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sgbd = new GestionBD(this);


        Intent extras = getIntent();
        uneRecette = (Recette) extras.getSerializableExtra("uneRecette");
        Log.i("P2", "Recette récup : " + uneRecette.toString());


        llIngredients = findViewById(R.id.ll_ingredients);
        btnAjouterIngredient = findViewById(R.id.btn_ajouterIngredient);
        llEtape = findViewById(R.id.ll_numEtapeRecette);
        btnAjouterEtape = findViewById(R.id.btn_ajouterEtape);

        btnAjouterIngredient.setOnClickListener(this);
        btnAjouterEtape.setOnClickListener(this);

        init();

    }


    @Override
    public void onClick(View v) {
        addView(v);
    }

    private void addView(View v) {

        if (v.getId() == R.id.btn_ajouterIngredient) {
            IngredientRecette currentIngre = new IngredientRecette();
            currentIngre.setId_ingredient(-1);
            sgbd.open();
            addIngre(currentIngre);
            sgbd.close();

        } else if (v.getId() == R.id.btn_ajouterEtape) {
            EtapeRealisation currentEtape = new EtapeRealisation();
            currentEtape.setId_etape_realisation(-1);
            sgbd.open();
            addEtape(currentEtape);
            sgbd.close();
        }else if (v.getId() == R.id.btn_valider){
            if(checkIfValidAndRead()){
                uneRecette.setLesIngredientsRecette(lesIngre);
                uneRecette.setLesEtapes(lesEtapes);
                Toast info = Toast.makeText(this,"Création de la recette : " + uneRecette.toString(),Toast.LENGTH_SHORT);
                info.show();

                sgbd.open();

                //Ajout par défaut de l'auteur
                uneRecette.setAuteur(sgbd.rechercherAuteur(0,"id_auteur"));

                long id = sgbd.ajoutRecette(uneRecette);
                Log.i("ID_gen", ""+id);
                uneRecette.setId_recette(id);

                for(Ingredient unIngreRecette : uneRecette.getLesIngredientsRecette()){
                    IngredientRecette unIngredientRecette = new IngredientRecette(uneRecette.getId_recette(),unIngreRecette.getId(),unIngreRecette.getQuantite(),unIngreRecette.getUniteMesure().getId());
                    sgbd.ajoutListeIngredient(unIngredientRecette);
                }

                for(Map.Entry uneEtp : uneRecette.getLesEtapes().entrySet() ){
                    EtapeRealisation uneEtapeRealisation = new EtapeRealisation((int)uneEtp.getKey()+1,uneRecette.getId_recette(), uneEtp.getValue().toString());
                    sgbd.ajoutEtape(uneEtapeRealisation);
                }

                sgbd.close();
                Log.i("Ajout recette","OK : " + uneRecette.toString());
            }else{
                Log.i("Ajout impossible","Une erreur c'est produite");
                Toast info = Toast.makeText(this,"Impossible de créer la recette",Toast.LENGTH_SHORT);
                info.show();
            }
        }

    }


    private boolean checkIfValidAndRead() {
        lesIngre.clear();
        lesEtapes.clear();

        boolean result=false;
        boolean resultBase = false;
        boolean resultIngre = false;
        boolean resultEtapes = false;

        resultBase =  checkLesBases();
        resultIngre = checkLesIngre();
        resultEtapes = checkLesEtapes();

        if(resultBase && resultIngre && resultEtapes){
            result = true;
        }
        return result;
    }

    private boolean checkLesBases() {
        Boolean result = true;
        Toast info;

        EditText unTitre = (EditText) findViewById(R.id.ajout_txtNom);
        EditText unTemps = (EditText) findViewById(R.id.ajout_ed_temps);
        EditText unNbPers = (EditText) findViewById(R.id.ajout_tv_nb_personne);



        if(!unTitre.getText().toString().equals("")){
            uneRecette.setNom(unTitre.getText().toString());
        }else{
            result = false;
            info = Toast.makeText(this,"Aucun titre",Toast.LENGTH_SHORT);
            info.show();
        }
        if(!unTemps.getText().toString().equals("")){
            if(Integer.parseInt(unTemps.getText().toString())>0){
                uneRecette.setDuree(Integer.parseInt(unTemps.getText().toString()));
            }
        }else{
            result = false;
            info = Toast.makeText(this,"Aucun temps",Toast.LENGTH_SHORT);
            info.show();
        }
        if(!unNbPers.getText().toString().equals("")){
            if(Integer.parseInt(unNbPers.getText().toString())>0){
                uneRecette.setNb_personne(Integer.parseInt(unNbPers.getText().toString()));
            }
        }else{
            result=false;
            info = Toast.makeText(this,"Aucun nombre de personnes",Toast.LENGTH_SHORT);
            info.show();
        }
        return result;
    }


    // Vérification des ingrédients
    private boolean checkLesIngre(){
        boolean result = true;
        for(int i=0;i<llIngredients.getChildCount();i++) {
            View ingredientView = llIngredients.getChildAt(i);

            EditText etUneQuantiteIngredient = (EditText) ingredientView.findViewById(R.id.et_quantiteIngredient);
            AppCompatSpinner spNomIngredient = (AppCompatSpinner) ingredientView.findViewById(R.id.sp_nomIngredient);
            AppCompatSpinner spTypeQuantiteIngredient = (AppCompatSpinner) ingredientView.findViewById(R.id.sp_typeQuantiteIngredient);

            Ingredient unIngredient = (Ingredient) spNomIngredient.getSelectedItem();
            unIngredient.setUniteMesure((UniteMesure)spTypeQuantiteIngredient.getSelectedItem());
            if (!etUneQuantiteIngredient.getText().toString().equals("")) {
                unIngredient.setQuantite(Integer.parseInt(etUneQuantiteIngredient.getText().toString()));

            } else {
                Toast infoIngreQuantite = Toast.makeText(this,"Aucune quantité rentrée pour un des ingrédients",Toast.LENGTH_SHORT);
                infoIngreQuantite.show();
                result = false;
                break;
            }

            lesIngre.add(unIngredient);
        }
        if(lesIngre.size() == 0 && llIngredients.getChildCount()==0){
            result=false;
            Log.i("", "Aucun ingrédient selectionné");
            Toast infoIngre = Toast.makeText(this,"Aucun ingrédient selectionné",Toast.LENGTH_SHORT);
            infoIngre.show();
        }
        return result;
    }

    // Vérification des étapes
    public boolean checkLesEtapes(){
        Boolean result = true;
        String info = "";

        for(int i=0;i<llEtape.getChildCount();i++) {
            View etapeView = llEtape.getChildAt(i);
            EditText etUneDescription = (EditText) etapeView.findViewById(R.id.et_description);


            EtapeRealisation uneEtape = new EtapeRealisation();

            if (!etUneDescription.getText().toString().equals("")) {
                uneEtape.setDescription(etUneDescription.getText().toString());

            } else {
                Toast infoDesc = Toast.makeText(this,"Aucune description pour une étape", Toast.LENGTH_SHORT);
                infoDesc.show();
                result = false;
                break;
            }

            lesEtapes.put(i,uneEtape.getDescription());
        }
        if(lesEtapes.size() == 0 && llEtape.getChildCount()==0){
            result=false;
            Log.i("", "Aucune étape selectionné");
            Toast infoNbEtapes = Toast.makeText(this, "Aucune étape selectionné",Toast.LENGTH_SHORT);
            infoNbEtapes.show();


        }


        return result;


    }

    private void init() {
        EditText unTitre = (EditText) findViewById(R.id.txtNom);
        EditText unTemps = (EditText) findViewById(R.id.ed_temps);
        EditText unNbPers = (EditText) findViewById(R.id.tv_nb_personne);
        ImageView unePhoto = (ImageView) findViewById(R.id.iv_photoRecette);

        //Initialiser titre
        unTitre.setText(uneRecette.getNom());

        //Initialiser durée de préparation
        unTemps.setText(String.valueOf(uneRecette.getDuree()));

        //Initialiser nombre de personnes
        unNbPers.setText(String.valueOf(uneRecette.getNb_personne()));

        //Initialiser photo recette
        int unDrawable = getResources().getIdentifier(""+uneRecette.genererNomImage(), "drawable", getPackageName());
        unePhoto.setImageResource(unDrawable);

        sgbd.open();
        //Initialiser les ingrédients
        afficherIngredient();

        //Initialiser les étapes
        afficherEtape();
        sgbd.close();



    }

    private void afficherIngredient() {

        ArrayList<IngredientRecette> lesIngreRecette = new ArrayList<>();
        for(IngredientRecette unIngredientRec : sgbd.rechercherIngredientRecette(uneRecette.getId_recette(),"id_recette")) {
            lesIngreRecette.add(unIngredientRec);
            addIngre(unIngredientRec);
        }

    }

    private void afficherEtape() {
        Log.i("modifier_contenu","Contenu étape : " + sgbd.rechercherListeEtapesRealisation(uneRecette.getId_recette(),"id_recette"));

        HashMap<Integer,String> lesDiffEtapes = new HashMap<>();
        for(EtapeRealisation uneEtape : sgbd.rechercherListeEtapesRealisation(uneRecette.getId_recette(),"id_recette")){
            lesDiffEtapes.put(uneEtape.getId_etape_realisation(), uneEtape.getDescription());
            addEtape(new EtapeRealisation(uneEtape.getId_etape_realisation(),uneRecette.getId_recette(),uneEtape.getDescription()));
        }

        //Affichage des étapes
        for(Map.Entry uneDonnee :lesDiffEtapes.entrySet() ){
            Log.i("Modifier_etape","nouvelle étape");

        }
    }

    private void addEtape(EtapeRealisation etapeRealisation) {
        View etapeView = getLayoutInflater().inflate(R.layout.item_add_etape, null, false);

        TextView etNumEtape = (TextView) etapeView.findViewById(R.id.et_numEtape);
        EditText etDescription = (EditText) etapeView.findViewById(R.id.et_description);
        ImageView ivRemoveEtape = (ImageView) etapeView.findViewById(R.id.iv_remove_etape);

        if(etapeRealisation.getId_etape_realisation()!=-1){
            etNumEtape.setText("Étape" + etapeRealisation.getId_etape_realisation());
            etDescription.setText(etapeRealisation.getDescription());
        }


        llEtape.addView(etapeView);

        ivRemoveEtape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llEtape.removeView(etapeView);
                Log.i("Suppr","Suppression d'une étape");
            }
        });
    }


    private void addIngre(IngredientRecette currentIngreRec){
        //Affichage des Ingrédients
        View ingredientView = getLayoutInflater().inflate(R.layout.item_add_ingredient, null, false);

        // Initialiser les composants
        EditText etQuantiteIngredient = (EditText) ingredientView.findViewById(R.id.et_quantiteIngredient);
        AppCompatSpinner spNomIngredient = (AppCompatSpinner) ingredientView.findViewById(R.id.sp_nomIngredient);
        AppCompatSpinner spTypeQuantiteIngredient = (AppCompatSpinner) ingredientView.findViewById(R.id.sp_typeQuantiteIngredient);
        ImageView ivRemove = (ImageView) ingredientView.findViewById(R.id.iv_remove_ingredient);

        // Création de la liste des noms ======================*
        ArrayList<Ingredient> lesIngredients = new ArrayList<>();
        for (Ingredient unIngredient : sgbd.getLesIngredients()) {
            lesIngredients.add(unIngredient);
        }
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lesIngredients);
        spNomIngredient.setAdapter(spinnerArrayAdapter);


        ArrayList<UniteMesure> lesUnitesMesure = new ArrayList<>();
        for (UniteMesure uneUnite : sgbd.getLesUnitesMesure()) {
            lesUnitesMesure.add(uneUnite);
        }



        //Creation de la liste des noms d'unités
        ArrayAdapter spinnerArrayAdapterTwo = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lesUnitesMesure);
        spTypeQuantiteIngredient.setAdapter(spinnerArrayAdapterTwo);

        if(currentIngreRec.getId_ingredient()>=0){
            spNomIngredient.setSelection(currentIngreRec.getId_ingredient()-1); //-1 : car l'id des ingrédients dans la BDD commence à 1
            //Initialisation de la quantité
            etQuantiteIngredient.setText(String.valueOf(currentIngreRec.getQuantite()));
            spTypeQuantiteIngredient.setSelection(currentIngreRec.getId_unite_mesure()-1); //-1 : car l'id des unites de mesure dans la BDD commence à 1

        }

        ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llIngredients.removeView(ingredientView);
                ;
            }
        });

        llIngredients.addView(ingredientView);
    }
}