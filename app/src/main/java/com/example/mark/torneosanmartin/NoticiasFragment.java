package com.example.mark.torneosanmartin;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mark.torneosanmartin.modelo.Noticia;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticiasFragment extends Fragment implements AdaptadorNoticias.onItemClickListener {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private AdaptadorNoticias adaptador;
    private View view;
    private PanelActivity context;
    private int position;
    private Bundle bundle;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private ViewModelNoticias ModeloNoticias;
    private List<Noticia> ListaNoticias;
    //private AgregarEquipo dialogoAgregar;


    public NoticiasFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();

        ListaNoticias=new ArrayList<Noticia>();

       /* db.collection("noticias").orderBy("fecha", Query.Direction.DESCENDING).addSnapshotListener((Activity) context, new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                List<Noticia> noticias = documentSnapshots.toObjects(Noticia.class);
                adaptador.setdatos(noticias);
                Log.d("listener", "datos cargados");
            }
        });*/
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerNoticias);
        adaptador = new AdaptadorNoticias(context);
        //dividerItemDecoration= new DividerItemDecoration(context,LinearLayoutManager.VERTICAL);
        //Drawable drawable= ContextCompat.getDrawable(context, R.drawable.divider);
        //if(drawable!=null)
        // dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(context, R.drawable.divider));
        // recyclerView.addItemDecoration(dividerItemDecoration);
        adaptador.setListener(this);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adaptador);


       // db = FirebaseFirestore.getInstance();
        //dialogoAgregar=new AgregarEquipo();
         ModeloNoticias=ViewModelProviders.of(this).get(ViewModelNoticias.class);
         ModeloNoticias.getLiveData().observe(this,new Observer<List<Noticia>>(){
             @Override
             public void onChanged(@Nullable List<Noticia> noticias) {
                 if(noticias!=null) {
                     ListaNoticias.clear();
                     ListaNoticias.addAll(noticias);
                     adaptador.setdatos(ListaNoticias);
                 }
             }
         });

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (PanelActivity) context;
    }


    @Override
    public void ItemClick(int position) {

        Log.d("seleccionda detalle", String.valueOf(position));
        if (adaptador.getCountSelect() > 0) {
            select(position);
        } else {
            //si hago click en elelemento y no esta seleccionado
            //ningun item, se llama a la actividad de detalle
            Noticia noticia = adaptador.getNoticia(position);
            Intent intent = new Intent(context, DetalleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("noticias", noticia);
            intent.putExtras(bundle);
            startActivity(intent);
        }


    }


    private void select(int position) {
        //esta sentencia lo que hace seleccionar el elemento
        //si no lo esta lo selecciona y si esta lo deselecciona
        adaptador.setSelect(position);
        int count = adaptador.getCountSelect();
        Log.d("cantidad seleccionda", String.valueOf(count));
        if (count == 0) {
            context.desactiveMode();
        } else {
            context.settextmode(count);
        }
    }


    @Override
    public void ItemLongClick(int position) {
        //al hacer un clig largo se pregunta si es que
        //el actionMode esta activo o no
        //si no esta activo se activa y se selecciona
        //si esta activo solo se selecciona el elemento
        context.activeMode();
        select(position);


    }

    @Override
    public void desactiveMode() {
        context.desactiveMode();
    }


    public interface actionListener {
        void activeMode();

        void desactiveMode();

        void settextmode(int count);

    }



    public void eliminar(){
        adaptador.eliminarItems();
    }

    public void ClearList(){
        adaptador.clearSelection();
    }


}





