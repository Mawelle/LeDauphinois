package fr.maelledauphin.ledauphinoisapp.ui.utilisateur;

import android.content.Context;
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
import fr.maelledauphin.ledauphinoisapp.modele.Ingredient;
import fr.maelledauphin.ledauphinoisapp.modele.UniteMesure;

public class AjouterRecetteActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llIngredients;
    Button btnAjouterIngredient;
    LinearLayout llEtape;
    Button btnAjouterEtape;
    Button btnValiderRecette;
    GestionBD sgbd;
    private Context context;
    ArrayList<Ingredient> lesIngre = new ArrayList<>();
    HashMap<Integer,String> lesEtapes = new HashMap<>();
    private Recette currentRecette = new Recette();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_recette);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sgbd = new GestionBD(this);


        llIngredients = findViewById(R.id.ajout_ll_ingredients);
        btnAjouterIngredient = findViewById(R.id.ajout_btn_ajouterIngredient);
        llEtape = findViewById(R.id.ajout_ll_numEtapeRecette);
        btnAjouterEtape = findViewById(R.id.ajout_btn_ajouterEtape);

        btnValiderRecette = findViewById(R.id.ajout_btn_valider);

        btnAjouterIngredient.setOnClickListener(this);
        btnAjouterEtape.setOnClickListener(this);
        btnValiderRecette.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ajout_btn_ajouterIngredient:
                addIngre(v);
                break;

            case R.id.ajout_btn_ajouterEtape:
                addEtape(v);
                break;

            case R.id.ajout_btn_valider:

                if (checkIfValidAndRead()){
                    currentRecette.setLesIngredientsRecette(lesIngre);
                    currentRecette.setLesEtapes(lesEtapes);
                    Toast info = Toast.makeText(this,"Création de la recette : " + currentRecette.toString(),Toast.LENGTH_SHORT);
                    info.show();

                    sgbd.open();

                    //Ajout par défaut de l'auteur
                    currentRecette.setAuteur(sgbd.rechercherAuteur(0,"id_auteur"));

                    long id = sgbd.ajoutRecette(currentRecette);
                    Log.i("ID_gen", ""+id);
                    currentRecette.setId_recette(id);

                    for(Ingredient unIngreRecette : currentRecette.getLesIngredientsRecette()){
                        IngredientRecette unIngredientRecette = new IngredientRecette(currentRecette.getId_recette(),unIngreRecette.getId(),unIngreRecette.getQuantite(),unIngreRecette.getUniteMesure().getId());
                        sgbd.ajoutListeIngredient(unIngredientRecette);
                    }

                    for(Map.Entry uneEtp : currentRecette.getLesEtapes().entrySet() ){
                        EtapeRealisation uneEtapeRealisation = new EtapeRealisation((int)uneEtp.getKey()+1,currentRecette.getId_recette(), uneEtp.getValue().toString());
                        sgbd.ajoutEtape(uneEtapeRealisation);
                    }

                    sgbd.close();
                    Log.i("Ajout recette","OK : " + currentRecette.toString());
                }else{
                    Log.i("Ajout impossible","Une erreur c'est produite");
                    Toast info = Toast.makeText(this,"Impossible de créer la recette",Toast.LENGTH_SHORT);
                    info.show();
                    }
                break;
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
            currentRecette.setNom(unTitre.getText().toString());
        }else{
            result = false;
            info = Toast.makeText(this,"Aucun titre",Toast.LENGTH_SHORT);
            info.show();
        }
        if(!unTemps.getText().toString().equals("")){
            if(Integer.parseInt(unTemps.getText().toString())>0){
               currentRecette.setDuree(Integer.parseInt(unTemps.getText().toString()));
            }
        }else{
            result = false;
            info = Toast.makeText(this,"Aucun temps",Toast.LENGTH_SHORT);
            info.show();
        }
        if(!unNbPers.getText().toString().equals("")){
            if(Integer.parseInt(unNbPers.getText().toString())>0){
                currentRecette.setNb_personne(Integer.parseInt(unNbPers.getText().toString()));
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


    private void addIngre(View v) {
            View ingredientView = getLayoutInflater().inflate(R.layout.item_add_ingredient, null, false);

            // Initialiser
            EditText etQuantiteIngredient = (EditText) ingredientView.findViewById(R.id.et_quantiteIngredient);
            AppCompatSpinner spNomIngredient = (AppCompatSpinner) ingredientView.findViewById(R.id.sp_nomIngredient);
            AppCompatSpinner spTypeQuantiteIngredient = (AppCompatSpinner) ingredientView.findViewById(R.id.sp_typeQuantiteIngredient);
            ImageView ivRemove = (ImageView) ingredientView.findViewById(R.id.iv_remove_ingredient);

            // Création de la liste des noms ======================*

            //V1 sans objets :
            /*List<String> lesNoms = new ArrayList<>();
            sgbd = new GestionBD(this);
            sgbd.open();
            //for(Ingredient unIngredient: RecupJSON.getLesIngredients()){
            for (Ingredient unIngredient : sgbd.getLesIngredients()) {
                lesNoms.add(unIngredient.getNom());
            }*/

        sgbd.open();
        ArrayList<Ingredient> lesIngredients = new ArrayList<>();
            for (Ingredient unIngredient : sgbd.getLesIngredients()) {
                lesIngredients.add(unIngredient);
            }
            //V2 avec objet ingrédient
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lesIngredients);
            spNomIngredient.setAdapter(spinnerArrayAdapter);






                // Création de la liste des unités de mesure
            /*List<String> lesTypes = new ArrayList<>();
            //for(UniteMesure uneUniteMesure: RecupJSON.getLesUnitesMesure()){
            for (UniteMesure uneUniteMesure : sgbd.getLesUnitesMesure()) {
                lesTypes.add(uneUniteMesure.getTypeMesure());
            }*/

            ArrayList<UniteMesure> lesUnitesMesure = new ArrayList<>();
            for (UniteMesure uneUnite : sgbd.getLesUnitesMesure()) {
                lesUnitesMesure.add(uneUnite);
            }
            //V2 avec objet UniteMesure
            ArrayAdapter spinnerArrayAdapterTwo = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lesUnitesMesure);
            spTypeQuantiteIngredient.setAdapter(spinnerArrayAdapterTwo);

            sgbd.close();
            //ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lesNoms);
            //ArrayAdapter arrayAdapterUnite = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lesTypes);

            //spNomIngredient.setAdapter(arrayAdapter);
            //spTypeQuantiteIngredient.setAdapter(arrayAdapterUnite);

            ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llEtape.removeView(ingredientView);
                    ;
                }
            });

            llIngredients.addView(ingredientView);

        }

        private void addEtape(View v){
            // Ajouter une étape
            View etapeView = getLayoutInflater().inflate(R.layout.item_add_etape, null, false);

            TextView etNumEtape = (TextView) etapeView.findViewById(R.id.et_numEtape);
            EditText etDescription = (EditText) etapeView.findViewById(R.id.et_description);
            ImageView ivRemoveEtape = (ImageView) etapeView.findViewById(R.id.iv_remove_etape);


            ivRemoveEtape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llEtape.removeView(etapeView);
                    ;
                }
            });

            llEtape.addView(etapeView);

    }

}