package fr.maelledauphin.ledauphinoisapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.maelledauphin.ledauphinoisapp.modele.bdd.GestionBD;
import fr.maelledauphin.ledauphinoisapp.ui.home.DetailRecette;
import fr.maelledauphin.ledauphinoisapp.modele.Recette;
import fr.maelledauphin.ledauphinoisapp.ui.utilisateur.ModifierRecetteActivity;

public class AdapterRecyler extends RecyclerView.Adapter<AdapterRecyler.MyViewHolder> {

    private ArrayList<Recette> lesRecettes = new ArrayList<Recette>();
    //private ArrayList<Recette> lesRecettesFull = new ArrayList<Recette>();
    private ArrayList<Integer> lesImages = new ArrayList<Integer>();


    Context context;
    Boolean edit = false;
    GestionBD sgbd;

    public AdapterRecyler(Context ct /*, ArrayList<Recette> lesRecettes_e*/, Boolean edit){
        context = ct;
        sgbd = new GestionBD(ct);
        sgbd.open();
        this.lesRecettes = GestionBD.getLesRecettes();
        //lesRecettesFull = new ArrayList<>(lesRecettes_e);
        this.edit=edit;
        initTableau(ct);
        sgbd.close();
        Log.i("Recettes_BDD",""+lesRecettes.toString());
    }


    private void initTableau(Context ct){
        for(Recette uneRecette : lesRecettes){
            lesImages.add(ct.getResources().getIdentifier(""+uneRecette.genererNomImage(), "drawable", ct.getPackageName()));
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_recette, parent,false);
        if(edit.equals(true)){
            view = inflater.inflate(R.layout.item_list_recette_edit, parent,false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.i("Position : ",""+ position);
        Log.i("Recettes :", lesRecettes.get(position).getNom());
        holder.monTitre.setText(lesRecettes.get(position).getNom());
        holder.maDuree.setText(lesRecettes.get(position).getFormatDuree());
        holder.monNbPersonnes.setText(lesRecettes.get(position).getNb_personne()+" pers.");
        holder.monImage.setImageResource(lesImages.get(position));
        if(edit==false){
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Clique","Vous avez cliqué sur une recette");
                    Intent intent = new Intent(context, DetailRecette.class);
                    intent.putExtra("uneRecette", lesRecettes.get(position));
                    context.startActivity(intent);
                }
            });
        }else if (edit==true){
            holder.btnModifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Btn modifier","Vous avez cliqué sur modifier");
                    Intent intent = new Intent(context, ModifierRecetteActivity.class);
                    intent.putExtra("uneRecette", lesRecettes.get(position));
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return lesRecettes.size();
    }

   /* @Override
    public Filter getFilter() {
        return recetteFilter;
    }

    private Filter recetteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Recette> filteredList = new ArrayList<>();
            if (constraint ==null || constraint.length() ==0){
                filteredList.addAll(lesRecettesFull);
            } else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Recette uneRecette : lesRecettesFull){
                    if(uneRecette.getNom().toLowerCase().contains((filterPattern))){
                        filteredList.add(uneRecette);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lesRecettes.clear();;
            lesRecettes.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };*/

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView monTitre, maDuree, monNbPersonnes;
        ImageView monImage;
        ConstraintLayout mainLayout;
        Button btnModifier;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            monTitre = itemView.findViewById(R.id.titre_cardView);
            maDuree = itemView.findViewById(R.id.duree_cardView);
            monNbPersonnes = itemView.findViewById(R.id.nbPersonnes_cardView);
            monImage = itemView.findViewById(R.id.photo_recette);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            if(edit==true){
                btnModifier = itemView.findViewById(R.id.btn_modifierRecette);
                /*itemView.findViewById(R.id.btn_modifierRecette).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("Btn modifier","Vous avez cliqué sur modifier");
                        Intent
                    }
                });*/
            }

        }
    }

    public void filterList(ArrayList<Recette> filterdList){
        lesRecettes = filterdList;
        notifyDataSetChanged();
    }
}
