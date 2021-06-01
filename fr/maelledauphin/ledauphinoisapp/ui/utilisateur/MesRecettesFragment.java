package fr.maelledauphin.ledauphinoisapp.ui.utilisateur;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.maelledauphin.ledauphinoisapp.R;
import fr.maelledauphin.ledauphinoisapp.modele.bdd.RecupJSON;
import fr.maelledauphin.ledauphinoisapp.AdapterRecyler;
import fr.maelledauphin.ledauphinoisapp.modele.Recette;

public class MesRecettesFragment extends Fragment {

    private ArrayList<Recette> mesRecettes = new ArrayList<Recette>();
    View view;
    AdapterRecyler adapterRecyler;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mes_recettes, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_mesRecettes);
        for (Recette uneRecette : RecupJSON.getLesRecettes()){
            if(uneRecette.getAuteur().getId()==0){
                mesRecettes.add(uneRecette);
            }
        }
        adapterRecyler = new AdapterRecyler(getActivity()/*, mesRecettes*/,true);
        recyclerView.setAdapter(adapterRecyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}