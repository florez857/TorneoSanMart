package com.example.mark.torneosanmartin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mark.torneosanmartin.modelo.Equipos;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class PanelActivity extends AppCompatActivity implements AgregarEquipo.dialogListener{

    private FirebaseFirestore db;
    private AgregarEquipo dialogoAgregar;
    private RecyclerView recyclerView;
    private AdaptadorEquipos adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db=FirebaseFirestore.getInstance();

        dialogoAgregar=new AgregarEquipo();


        recyclerView=(RecyclerView)findViewById(R.id.recyclerEquipos);
        adaptador=new AdaptadorEquipos(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                dialogoAgregar.show(getSupportFragmentManager(),"Añadir Nueva Persona");


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void insertar(Equipos equipo) {

        db.collection("equipos").document(equipo.getNombre()).set(equipo);
    }

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("equipos").orderBy("nombre", Query.Direction.ASCENDING).addSnapshotListener(this, new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                List<Equipos> equipos= documentSnapshots.toObjects(Equipos.class);
                adaptador.setdatos(equipos);
                Log.d("listener",documentSnapshots.toString());
            }
        });

    }
}
