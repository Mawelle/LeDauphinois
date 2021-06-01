package fr.maelledauphin.ledauphinoisapp.modele.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDHelper extends SQLiteOpenHelper {
    public BDHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table RECETTE ------------------------
        String reqRec = "create table recette (" +
                "id_recette INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom text," +
                "duree integer," +
                "nb_personne integer," +
                "id_auteur integer REFERENCES auteur )";


        // Table INGREDIENT ------------------------
        String reqIngre = "create table ingredient (" +
                "id_ingredient INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom text)";


        // Table UNITE_MESURE ------------------------
        String reqUniteMesure = "create table unite_mesure (" +
                "id_unite_mesure INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "libelle text," +
                "type_mesure text)";


        // Table Auteur ------------------------
        String reqAuteur = "create table auteur (" +
                "id_auteur INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom text," +
                "prenom text)";


        // Table liste_ingredients ------------------------
        String reqListeIngre = "create table liste_ingredients (" +
                "id_recette INTEGER REFERENCES recette, " +
                "id_ingredient INTEGER REFERENCES ingredient," +
                "quantite DOUBLE," +
                "id_unite_mesure INTEGER REFERENCES unite_mesure," +
                " PRIMARY KEY (id_recette, id_ingredient))";



        String reqEtapeRealisation = "create table etape_realisation (" +
                "id_etape_realisation INTEGER ," +
                "id_recette INTEGER REFERENCES recette, " +
                "description TEXT," +
                "PRIMARY KEY (id_etape_realisation, id_recette))";

        // Execution des requêtes
        db.execSQL(reqAuteur);
        db.execSQL(reqIngre);
        db.execSQL(reqUniteMesure);
        db.execSQL(reqRec);
        db.execSQL(reqListeIngre);
        db.execSQL(reqEtapeRealisation);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
