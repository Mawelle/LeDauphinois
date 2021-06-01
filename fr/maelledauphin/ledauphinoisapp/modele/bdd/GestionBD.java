package fr.maelledauphin.ledauphinoisapp.modele.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import fr.maelledauphin.ledauphinoisapp.modele.Auteur;
import fr.maelledauphin.ledauphinoisapp.modele.EtapeRealisation;
import fr.maelledauphin.ledauphinoisapp.modele.Ingredient;
import fr.maelledauphin.ledauphinoisapp.modele.IngredientRecette;
import fr.maelledauphin.ledauphinoisapp.modele.Recette;
import fr.maelledauphin.ledauphinoisapp.modele.UniteMesure;

public class GestionBD {
    private static SQLiteDatabase maBase;
    private BDHelper bdHelper;

    public GestionBD(Context context){
        this.bdHelper = new BDHelper(context, "ledauphinois", null, 1);
    }

    // ================================================//
    //              Méthodes principales
    // ================================================//

    // Méthodes de gestion des données (CRUD)
    public void open(){
        maBase = bdHelper.getWritableDatabase();
    }

    public void close(){
        maBase.close();
    }




    // ================================================//
    //              Rechercher dans la BDD
    // ================================================//

    // Table AUTEUR  ----------------
    public static Auteur rechercherAuteur(int condition, String colonne){
        Auteur currentAuteur = null;
        String req = "select id_auteur,nom,prenom from auteur where " + colonne + "=" + condition ;
        Cursor cursor = maBase.rawQuery(req,null,null);
        while(cursor.moveToNext()){
            currentAuteur = new Auteur(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
        }
        return currentAuteur;
    }


    // Table RECETTE  ----------------
    public static Recette rechercherRecette (int condition, String colonne){
        Recette currentRecette = null;
        String req = "select id_recette,nom,duree,nb_personne,id_auteur from recette where " + colonne + "=" + condition ;
        Cursor cursor = maBase.rawQuery(req,null,null);
        while(cursor.moveToNext()){
             currentRecette = new Recette(cursor.getLong(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),rechercherAuteur(cursor.getInt(4),"id_auteur"));
        }
        return currentRecette;
    }

    // Table INGREDIENT  ----------------
    public static Ingredient rechercherIngredient (int condition, String colonne){
        Ingredient currentIngredient = null;
        String req = "select id_ingredient,nom from ingredient where " + colonne + "=" + condition ;
        Cursor cursor = maBase.rawQuery(req,null,null);
        while(cursor.moveToNext()){
            currentIngredient = new Ingredient(cursor.getInt(0),cursor.getString(1));
        }
        return currentIngredient;
    }

    //Rechercher ingreRecette
    public static ArrayList<IngredientRecette> rechercherIngredientRecette (Long condition, String colonne) {
        ArrayList<IngredientRecette> lesIngreRecette = new ArrayList<>();
        IngredientRecette currentIngreRec = null;
        String req = "select id_recette,id_ingredient,quantite,id_unite_mesure from liste_ingredients where " + colonne + "=" + condition;
        Cursor cursor = maBase.rawQuery(req, null, null);
        while (cursor.moveToNext()) {
            lesIngreRecette.add(new IngredientRecette(cursor.getLong(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)));
        }
        return lesIngreRecette;
    }

    // Table UNITE_MESURE  ----------------
    public static UniteMesure rechercherUniteMesure (int condition, String colonne){
        UniteMesure currentUnite = null;
        String req = "select id_unite_mesure,libelle,type_mesure from unite_mesure where " + colonne + "=" + condition ;
        Cursor cursor = maBase.rawQuery(req,null,null);
        while(cursor.moveToNext()){
            currentUnite = new UniteMesure(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
        }
        return currentUnite;
    }


    //Rechercher etapesRealisation
    public static ArrayList<EtapeRealisation> rechercherListeEtapesRealisation (Long condition, String colonne) {
        ArrayList<EtapeRealisation> lesEtapes = new ArrayList<>();
        String req = "select id_etape_realisation,id_recette,description from etape_realisation where " + colonne + "=" + condition;
        Cursor cursor = maBase.rawQuery(req, null, null);
        while (cursor.moveToNext()) {
            lesEtapes.add(new EtapeRealisation(cursor.getInt(0), cursor.getLong(1), cursor.getString(2)));
        }
        return lesEtapes;
    }

    // ================================================//
    //              Setter de la BDD
    // ================================================//



    // Table AUTEUR  ----------------
    public long ajoutAuteur (Auteur unAuteur){
        ContentValues cv = new ContentValues();
        cv.put("id_auteur",unAuteur.getId());
        cv.put("nom",unAuteur.getNom());
        cv.put("prenom",unAuteur.getPrenom());
        Long id = maBase.insert("auteur",null,cv);
        Log.i("Auteur créé", "Détail :" + unAuteur.toString());
        return id;
    }


    // Table INGREDIENT ----------------
    public void ajoutIngred (Ingredient unIngredient){
        ContentValues cv = new ContentValues();
        cv.put("id_ingredient",unIngredient.getId());
        cv.put("nom",unIngredient.getNom());
        maBase.insert("ingredient",null,cv);
        Log.i("Ingrédient créé", "Détail :" + unIngredient.toString());
    }


    // Table UNITE_MESURE  ----------------
    public void ajoutUniteMesure (UniteMesure uneUnite){
        ContentValues cv = new ContentValues();
        cv.put("id_unite_mesure",uneUnite.getId());
        cv.put("libelle",uneUnite.getLibelle());
        cv.put("type_mesure",uneUnite.getTypeMesure());
        maBase.insert("unite_mesure",null,cv);
        Log.i("Unité de mesure créé", "Détail :" + uneUnite.toString());
    }


    // Table RECETTE  ----------------
    public long ajoutRecette (Recette uneRecette){
        ContentValues cv = new ContentValues();
        if(uneRecette.getId_recette()!=0){
            cv.put("id_recette",uneRecette.getId_recette());
        }
        cv.put("nom",uneRecette.getNom());
        cv.put("duree",uneRecette.getDuree());
        cv.put("nb_personne",uneRecette.getNb_personne());
        cv.put("id_auteur",uneRecette.getAuteur().getId());
        long id = maBase.insert("recette","",cv);
        Log.i("Recette_créé", "Détail :" + uneRecette.toString());
        return id;
    }


    // Table LISTE_INGREDIENT  ----------------
    public void ajoutListeIngredient (IngredientRecette unIngredientRecette) {
        Log.i("IngreRecDetail", unIngredientRecette.toString());
        int cpt = 0;
        //Log.i("Les Ingrédients", "Liste : " + uneRecette.getLesIngredientsRecette());
        ContentValues cv = new ContentValues();
        cv.put("id_recette", unIngredientRecette.getId_recette());
        cv.put("id_ingredient", unIngredientRecette.getId_ingredient());
        cv.put("quantite", unIngredientRecette.getQuantite());
        cv.put("id_unite_mesure", unIngredientRecette.getId_unite_mesure());
        maBase.insert("liste_ingredients", null, cv);
    }



    // Table ETAPE_REALISATION  ----------------
    public void ajoutEtape (EtapeRealisation uneEtape) {
            ContentValues cv = new ContentValues();
            cv.put("id_etape_realisation", ""+uneEtape.getId_etape_realisation());
            cv.put("id_recette", uneEtape.getId_recette());
            cv.put("description", ""+ uneEtape.getDescription());
            maBase.insert("etape_realisation", null, cv);
            //Log.i("Liste étapes créés", "Recette :" + uneRecette.getNom() + " | Etape ajouté : " + nEtape + " , description : " + uneDonnee.getValue());

        //Log.i("=== Nb étapes", " Recette :" + uneRecette.getNom() + " , Nb_étapes : " + cpt);
    }






    // ================================================//
    //               Getter de la BDD
    // ================================================//

    // Table AUTEUR  ----------------
    public static ArrayList<Auteur> getLesAuteurs() {
        ArrayList<Auteur> lesAuteurs = new ArrayList<>();
        String req = "select id_auteur,nom,prenom from auteur";
        Cursor cursor = maBase.rawQuery(req,null,null);
        while(cursor.moveToNext()){
            Auteur currentAuteur = new Auteur(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
            lesAuteurs.add(currentAuteur);
        }
        return lesAuteurs;
    }




    // Table INGREDIENT ----------------------------------

    public static ArrayList<Ingredient> getLesIngredients() {
        ArrayList<Ingredient> lesIngredients = new ArrayList<>();
        String req = "select id_ingredient, nom from ingredient";
        Cursor cursor = maBase.rawQuery(req,null,null);
        while(cursor.moveToNext()){
            Ingredient currentIngredient = new Ingredient(cursor.getInt(0), cursor.getString(1));
            lesIngredients.add(currentIngredient);
        }
        return lesIngredients;
    }


    // Table UNITE_MESURE  ----------------

    public ArrayList<UniteMesure> getLesUnitesMesure() {
        ArrayList<UniteMesure> lesUnites = new ArrayList<>();
        String req = "select id_unite_mesure ,libelle, type_mesure from unite_mesure";
        Cursor cursor = maBase.rawQuery(req,null,null);
        while(cursor.moveToNext()){
            UniteMesure currentUnite = new UniteMesure(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
            lesUnites.add(currentUnite);
        }
        return lesUnites;
    }



    // Table RECETTE ------------------------------------

    public static ArrayList<Recette> getLesRecettes() {
        ArrayList<Recette> lesRecettes = new ArrayList<>();
        String req = "select id_recette,nom,duree,nb_personne,id_auteur from recette";
        Cursor cursor = maBase.rawQuery(req,null,null);
        while(cursor.moveToNext()){
           Recette currentRecette = new Recette(cursor.getLong(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),rechercherAuteur(cursor.getInt(4),"id_auteur"));
            lesRecettes.add(currentRecette);
        }
        return lesRecettes;
    }


    // Table LISTE_INGREDIENT  ----------------
    public static ArrayList<IngredientRecette> getListeDesIngredients() {
        ArrayList<IngredientRecette> lesIngredientsRec = new ArrayList<>();
        String req = "select id_recette,id_ingredient,quantite,id_unite_mesure from liste_ingredients";
        Cursor cursor = maBase.rawQuery(req,null,null);
        Recette defaultRecette = null;
        Boolean init = false;
        while(cursor.moveToNext()){
            Log.i("Quelqu'un?","id recette : " + cursor.getInt(0) + " , id ingrédient : " + cursor.getInt(1));
            IngredientRecette unIngreRec = new IngredientRecette(cursor.getLong(0),cursor.getInt(1),cursor.getInt(2),cursor.getInt(3));
            lesIngredientsRec.add(unIngreRec);
            //Permet de vérifier si on se trouve toujours sur la même recette pour ajouter la liste des ingrédients
            /*if (init == true){
                //Si la recette courante est différente de la recette pour l'ingrédient précédent
                if(defaultRecette.getId_recette()!=(cursor.getInt(0))) {
                    defaultRecette.setLesIngredientsRecette(lesIngredients);
                    lesRecettesAvecIngre.add(defaultRecette);
                    lesIngredients.clear();
                    init = false;
                }
            }
            if(init == false){
                 defaultRecette = rechercherRecette(cursor.getInt(0),"id_recette");
                init=true;
            }
            Ingredient currentIngredient = rechercherIngredient(cursor.getInt(1),"id_ingredient");
            currentIngredient.setQuantite(cursor.getInt(2));
            currentIngredient.setUniteMesure(rechercherUniteMesure(cursor.getInt(3),"id_unite_mesure"));
            lesIngredients.add(currentIngredient);*/
        }
        Log.i("Les_IngreRec", lesIngredientsRec.toString());
        return lesIngredientsRec;
    }


    // Table ETAPE_REALISATION  ----------------
    public static ArrayList<EtapeRealisation> getEtapesRealisation() {
        ArrayList<EtapeRealisation> LesEtapesRealisation = new ArrayList<>();

        String req = "select id_etape_realisation,id_recette,description from etape_realisation";
        Cursor cursor = maBase.rawQuery(req,null,null);
        Recette defaultRecette = null;
        Boolean init = false;
        while(cursor.moveToNext()) {
            EtapeRealisation uneEtapeRealisation = new EtapeRealisation(cursor.getInt(0), cursor.getLong(1), cursor.getString(2));
            LesEtapesRealisation.add(uneEtapeRealisation);

           /* if (init == true){
                //Si la recette courante est différente de la recette pour l'étape précédent
                if(defaultRecette.getId_recette()!=(cursor.getInt(1))) {
                    defaultRecette.setLesEtapes(lesEtapes);
                    lesRecettesAvecEtapes.add(defaultRecette);
                    Log.i("Contenu_etape",""+lesRecettesAvecEtapes.toString());
                    //lesEtapes.clear();
                    init = false;
                }
            }
            if(init == false){
                defaultRecette = rechercherRecette(cursor.getInt(1),"id_recette");
                init=true;
            }
            Log.i("Recup étape", "Recette : "+ "(" + defaultRecette.getId_recette() + ") " + defaultRecette.getNom() + "  , Nuum : " + cursor.getInt(0) + " , desc : " + cursor.getString(2) );
            lesEtapes.put(cursor.getInt(0),cursor.getString(2));
        }
        if(init==true){
            //lesRecettesAvecEtapes.add(defaultRecette);
        }*/
        }

        Log.i("Liste Recettes Etape","" + LesEtapesRealisation.toString());
        return LesEtapesRealisation;
    }



    // ================================================//
    //               Vider la BDD
    // ================================================//

    // Table AUTEUR  ----------------
    public void videAuteur() {
        maBase.delete("auteur",null,null);
    }


    // Table INGREDIENT ----------------
    public void videIngre() {
        maBase.delete("ingredient",null,null);
    }

    // Table UNITE_MESURE  ----------------
    public void videUniteMesure() {
        maBase.delete("unite_mesure",null,null);
    }


    // Table RECETTE  ----------------
    public void videRecette() {
        maBase.delete("recette",null,null);
    }


    // Table LISTE_INGREDIENT  ----------------
    public void videListeIngredients() {
        maBase.delete("liste_ingredients",null,null);
    }



    // Table ETAPE_REALISATION  ----------------
    public void videEtapeRealisation() {
        maBase.delete("etape_realisation",null,null);
    }



}
