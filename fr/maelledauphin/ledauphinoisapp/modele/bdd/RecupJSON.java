package fr.maelledauphin.ledauphinoisapp.modele.bdd;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import fr.maelledauphin.ledauphinoisapp.R;
import fr.maelledauphin.ledauphinoisapp.modele.Auteur;
import fr.maelledauphin.ledauphinoisapp.modele.EtapeRealisation;
import fr.maelledauphin.ledauphinoisapp.modele.Ingredient;
import fr.maelledauphin.ledauphinoisapp.modele.IngredientRecette;
import fr.maelledauphin.ledauphinoisapp.modele.Recette;
import fr.maelledauphin.ledauphinoisapp.modele.UniteMesure;

public class RecupJSON {
    static ArrayList<Recette> lesRecettes = new ArrayList<Recette>();
    static ArrayList<IngredientRecette> lesRecettesIngredients = new ArrayList<IngredientRecette>();
    static ArrayList<EtapeRealisation> lesRecettesEtapes = new ArrayList<EtapeRealisation>();
    static ArrayList<Ingredient> lesIngredients = new ArrayList<Ingredient>();
    static ArrayList<UniteMesure> lesUnitesMesure = new ArrayList<UniteMesure>();
    static ArrayList<Auteur> lesAuteurs = new ArrayList<Auteur>();
    JSONObject jObj = null;
    private Context context;
    private GestionBD sgbd;

    // ================================================//
    //              Récupération JSON
    // ================================================//

    public RecupJSON(Context current){
        context = current;
        lesRecettes.clear();
        lesIngredients.clear();
        lesUnitesMesure.clear();
        lesAuteurs.clear();
        sgbd = new GestionBD(context);
        init();
    }


    private void init() {
        sgbd.open();
        sgbd.videEtapeRealisation();
        sgbd.videListeIngredients();
        sgbd.videIngre();
        sgbd.videUniteMesure();
        sgbd.videRecette();
        sgbd.videAuteur();

        //sgbd.videIngre();
        String lesDatas = lectureFichierLocal();
        Log.i("Recup fichier JSON", "Étape 1 : Le fichier contient : " + lesDatas);
        JSONObject jsonDatas = parseDatas(lesDatas);
        recupDatas(jsonDatas);
        sgbd.close();
    }

    private void recupDatas(JSONObject jsonRecette) {
        JSONArray lesRecettesJSON = null;



        //Récupération Ingrédients
        try {
            JSONArray lesIngredientsJSON = jsonRecette.getJSONArray("ingredients");
            for (int i = 0; i < lesIngredientsJSON.length(); i++) {
                JSONObject pJson = null;
                Ingredient unIngredient;
                pJson = lesIngredientsJSON.getJSONObject(i);
                unIngredient = new Ingredient(pJson.getInt("id"),pJson.getString("nom"));
                //lesIngredients.add(unIngredient);
                sgbd.ajoutIngred(unIngredient);
                lesIngredients = sgbd.getLesIngredients();

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        //Récupération unite_mesure
        try {
            JSONArray lesUnitesMesureJSON = jsonRecette.getJSONArray("unites_mesure");
            for (int i = 0; i < lesUnitesMesureJSON.length(); i++) {
                JSONObject pJson = null;
                UniteMesure uneUniteMesure;
                pJson = lesUnitesMesureJSON.getJSONObject(i);
                uneUniteMesure = new UniteMesure(pJson.getInt("id"),pJson.getString("libelle"),pJson.getString("type_mesure"));
                //Log.i("Unite_mesure json", "Détail :" + uneUniteMesure.toString());
                //lesUnitesMesure.add(uneUniteMesure);
                sgbd.ajoutUniteMesure(uneUniteMesure);
                lesUnitesMesure = sgbd.getLesUnitesMesure();

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }


        //Récupération auteur
        try {
            JSONArray lesAuteursJSON = jsonRecette.getJSONArray("auteur");
            for (int i = 0; i < lesAuteursJSON.length(); i++) {
                JSONObject pJson = null;
                Auteur unAuteur;
                pJson = lesAuteursJSON.getJSONObject(i);
                unAuteur = new Auteur(pJson.getInt("id"),pJson.getString("nom"),pJson.getString("prenom"));
                //lesAuteurs.add(unAuteur);
                sgbd.ajoutAuteur(unAuteur);
                lesAuteurs = sgbd.getLesAuteurs();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        // Récupération recettes
        try {
            lesRecettesJSON = jsonRecette.getJSONArray("recettes");
            JSONArray lesIngredientsRecetteJSON = jsonRecette.getJSONArray("liste_ingredients");
            JSONArray lesEtapesRecetteJSON = jsonRecette.getJSONArray("etape_realisation");
            for(int i=0; i<lesRecettesJSON.length();i++) {
                ArrayList<Ingredient> lesIngredientsRecette = new ArrayList<Ingredient>();
                HashMap<Integer,String> lesEtapesRecette = new HashMap<>();
                JSONObject pJson = null;
                Recette laRecette;
                pJson = lesRecettesJSON.getJSONObject(i);
                laRecette = new Recette(pJson.getLong("id"),pJson.getString("nom"), pJson.getInt("duree"), pJson.getInt("nb_personne"));
                // On récupère l'auteur de la recette
                //for(Auteur unAuteur : lesAuteurs){
                for(Auteur unAuteur : sgbd.getLesAuteurs()){
                    if(unAuteur.getId() == pJson.getInt("id_auteur")){
                        laRecette.setAuteur(unAuteur);
                    }
                }
                sgbd.ajoutRecette(laRecette);
                lesRecettes = sgbd.getLesRecettes();

                // Récupère la liste des ingrédients pour une recette
                IngredientRecette currentIngreRec = new IngredientRecette();
                currentIngreRec.setId_recette(laRecette.getId_recette());
                for(int y=0; y<lesIngredientsRecetteJSON.length();y++) {
                    JSONObject oJson = null;
                    oJson = lesIngredientsRecetteJSON.getJSONObject(y);
                    //for (Ingredient unIngredient : lesIngredients){
                    for (Ingredient unIngredient : sgbd.getLesIngredients()){
                        //Log.i("Test Ingred", "Recette courante : " + laRecette.getId_recette() + " , Recette JSON : " + oJson.getInt("id_recette"));
                        //Log.i("Test 0 : ", "Ingredient courant : " + unIngredient.getId() +" , ingredient JSON : " + oJson.getInt("id_ingredient"));
                        if(laRecette.getId_recette()==oJson.getInt("id_recette") && unIngredient.getId()==oJson.getInt("id_ingredient")){
                            Log.i("Test 1", "OK");
                            if(unIngredient.getId()==oJson.getInt("id_ingredient")) {
                               // Log.i("Test 2", "OKAY");
                                //Ingredient currentIngredient = (Ingredient) unIngredient.clone();
                                currentIngreRec.setId_ingredient(unIngredient.getId());
                                currentIngreRec.setQuantite(oJson.getInt("quantite"));
                                for (UniteMesure uneUniteMesure : sgbd.getLesUnitesMesure()) {
                                    if (uneUniteMesure.getId() == oJson.getInt("id_unite_mesure")) {
                                        //currentIngredient.setUniteMesure(uneUniteMesure);
                                        currentIngreRec.setId_unite_mesure(uneUniteMesure.getId());
                                        sgbd.ajoutListeIngredient(currentIngreRec);

                                    }
                                }
                                //lesIngredientsRecette.add(currentIngredient);
                            }
                        }
                    }
                }
                //laRecette.setLesIngredientsRecette(lesIngredientsRecette);
                lesRecettesIngredients = sgbd.getListeDesIngredients();
                // Récupère les étapes de réalisation d'une recette
                /*for(int z=0; z<lesEtapesRecetteJSON.length();z++) {
                    JSONObject eJson = null;
                    eJson = lesEtapesRecetteJSON.getJSONObject(z);
                    if (laRecette.getId_recette() == eJson.getInt("id_recette")) {
                        lesEtapesRecette.put(z+1,eJson.getString("description"));
                    }
                }*/
                for (int z = 0; z < lesEtapesRecetteJSON.length(); z++) {
                    EtapeRealisation uneEtapeRealisation;
                    JSONObject eJson = null;
                    eJson = lesEtapesRecetteJSON.getJSONObject(z);
                    if(laRecette.getId_recette() == eJson.getInt("id_recette")){
                        uneEtapeRealisation = new EtapeRealisation(eJson.getInt("id_etape_realisation"),eJson.getLong("id_recette"),eJson.getString("description"));
                        Log.i("Qui ?","Recette : " +eJson.getInt("id_recette") + " , description : " + eJson.getString("description"));
                        sgbd.ajoutEtape(uneEtapeRealisation);
                    }
                }
                Log.i("Recette créée", "Détail :" + laRecette.toString());
                //lesRecettes.add(laRecette);
            }
            lesRecettesEtapes = sgbd.getEtapesRealisation();



        } catch (JSONException /*| CloneNotSupportedException*/ e) {
            e.printStackTrace();
        }


    }



    private JSONObject parseDatas(String chaineJson) {
        try {
            jObj = new JSONObject(chaineJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jObj;

    }

    private String lectureFichierLocal() {
        String ligne = null;
        InputStream is = context.getResources().openRawResource(R.raw.datas);
        StringBuilder builder = new StringBuilder();
        // Stocker temporairement ce qui est lu dans le fichier
        BufferedReader bfr = null;
        try {
            bfr = new BufferedReader(new InputStreamReader(is,"UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                if(!((ligne=bfr.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            builder.append(ligne);
        }
        return builder.toString();
    }


    public static ArrayList<Recette> getLesRecettes() {
        return lesRecettes;
    }
    public static ArrayList<IngredientRecette> getLesRecettesIngredients() {
        return lesRecettesIngredients;
    }
    public static ArrayList<EtapeRealisation> getLesRecettesEtapes() {
        Log.i("Ici","yes");
        return lesRecettesEtapes;
    }
    public static void setLesRecettes(ArrayList<Recette> lesRecettes) {
        RecupJSON.lesRecettes = lesRecettes;
    }

    public static ArrayList<Ingredient> getLesIngredients() {
        return lesIngredients;
    }

    public static void setLesIngredients(ArrayList<Ingredient> lesIngredients) {
       RecupJSON.lesIngredients = lesIngredients;
    }

    public static ArrayList<UniteMesure> getLesUnitesMesure() {
        return lesUnitesMesure;
    }

    public static void setLesUnitesMesure(ArrayList<UniteMesure> lesUnitesMesure) {
        RecupJSON.lesUnitesMesure = lesUnitesMesure;
    }

    public static ArrayList<Auteur> getLesAuteurs() {
        return lesAuteurs;
    }

    public static void setLesAuteurs(ArrayList<Auteur> lesAuteurs) {
        RecupJSON.lesAuteurs = lesAuteurs;
    }
}
