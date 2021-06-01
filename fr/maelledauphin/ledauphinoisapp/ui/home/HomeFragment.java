package fr.maelledauphin.ledauphinoisapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.maelledauphin.ledauphinoisapp.AdapterRecyler;
import fr.maelledauphin.ledauphinoisapp.R;
import fr.maelledauphin.ledauphinoisapp.modele.bdd.GestionBD;
import fr.maelledauphin.ledauphinoisapp.modele.Recette;
import fr.maelledauphin.ledauphinoisapp.modele.bdd.RecupJSON;

public class HomeFragment extends Fragment {

    View view;
    Context context;

    //RecyclerView
    RecyclerView recyclerView;
    AdapterRecyler adapterRecyler;
    GestionBD sgbd;



    // ================================================//
    //              Méthode par défaut
    // ================================================//


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view ==null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        /*lesRecettes.clear();
        lesIngredients.clear();
        lesUnitesMesure.clear();
        lesAuteurs.clear();*/

        init();

    }



    private void init(){
        recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerView);
        //adapterRecyler = new AdapterRecyler(getActivity(), RecupJSON.getLesRecettes(),false);
        adapterRecyler = new AdapterRecyler(getActivity(),false);
        recyclerView.setAdapter(adapterRecyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Barre de recherche
        EditText editText = getActivity().findViewById(R.id.et_search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text){
        ArrayList<Recette> filteredList = new ArrayList<>();
        for(Recette uneRecette : RecupJSON.getLesRecettes()){
            if(uneRecette.getNom().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(uneRecette);
            }
            adapterRecyler.filterList(filteredList);
        }
    }

}