package com.example.mark.torneosanmartin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mark.torneosanmartin.modelo.Equipos;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorEquipos extends RecyclerView.Adapter<AdaptadorEquipos.holder> {

    List<Equipos> equipos;
    LayoutInflater inflater;


    public AdaptadorEquipos(Context context){
        inflater=LayoutInflater.from(context);
        equipos=new ArrayList<Equipos>();
    }


    @NonNull
    @Override
    public AdaptadorEquipos.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.item_equipo ,parent ,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorEquipos.holder holder, int position) {

        if(equipos!=null){
            holder.nombre.setText(equipos.get(position).getNombre());
            holder.delegado.setText("Delegado : "+equipos.get(position).getDelegado());
            holder.telefono.setText("Telefono : "+equipos.get(position).getTelefono());
        }


    }

    @Override
    public int getItemCount() {
        if (equipos!=null){
            return equipos.size();
        }else{
            return 0;}
    }


    public void setdatos(List<Equipos> equipos){
        if(equipos!=null){
            this.equipos.clear();
            this.equipos.addAll(equipos);
            notifyDataSetChanged();}
    }


    public class holder extends RecyclerView.ViewHolder {

        private TextView nombre;
        private TextView delegado;
        private TextView telefono;

        public holder(View itemView) {
            super(itemView);

            nombre=(TextView) itemView.findViewById(R.id.nombre);
            delegado=(TextView) itemView.findViewById(R.id.delegado);
            telefono=(TextView) itemView.findViewById(R.id.telefono);

        }



    }
}
