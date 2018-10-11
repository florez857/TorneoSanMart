package com.example.mark.torneosanmartin;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.util.Log;

import com.example.mark.torneosanmartin.modelo.Noticia;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NoticiasLiveData extends LiveData<List<Noticia>> {


    private static final String LOG_TAG = "FirebaseQueryLiveData";
    private List<Noticia> Listanoticias;
    private FirebaseFirestore db;
    private final Query query;
    private final MyEventListener listener;
    private ListenerRegistration registration;
    private  Runnable removeListener;
    private boolean listenerRemovePending = false;
    private final Handler handler = new Handler();

    public NoticiasLiveData() {
        listener = new MyEventListener();
        db = FirebaseFirestore.getInstance();
        Listanoticias=new ArrayList<>();
        query=db.collection("noticias").orderBy("fecha", Query.Direction.DESCENDING);

        removeListener = new Runnable() {
            @Override
            public void run() {
                registration.remove();
                listenerRemovePending = false;
            }
        };

    }


    @Override
    protected void onActive() {

        Log.d(LOG_TAG,"activo");
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        }
        else {
         registration=query.addSnapshotListener(listener);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {

        Log.d(LOG_TAG,"inactivo");

        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    private class MyEventListener implements EventListener<QuerySnapshot> {

        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

            if(e!=null){
                Log.d(LOG_TAG,e.getMessage());
                return;
            }
            Listanoticias.clear();
            Log.d(LOG_TAG,"cantidad de datos em la lista de noticias vieja :"+Listanoticias.size());
            Log.d(LOG_TAG,"cantidad de documentos cambiados leidos :"+String.valueOf( documentSnapshots.getDocumentChanges().size()));
                        /*  for(DocumentChange dc:documentSnapshots.getDocumentChanges()){

                                    switch (dc.getType()){
                                        case ADDED:
                                            Noticia noticia=dc.getDocument().toObject(Noticia.class);
                                            noticia.setId(dc.getDocument().getId());

                                            Listanoticias.add(Listanoticias.size(),noticia);
                                           // Listanoticias.add(noticia);

                                        case REMOVED:


                                        case MODIFIED:

                                    }
                          }*/

            for(DocumentSnapshot dc:documentSnapshots.getDocuments()){
                Noticia noticia=dc.toObject(Noticia.class);
                noticia.setId(dc.getId());
                Listanoticias.add(noticia);
            }

          // List<Noticia> noticias = documentSnapshots.toObjects(Noticia.class);

                          if(Listanoticias!=null){

                              Log.d(LOG_TAG,"Lista noticias fina "+Listanoticias.size());
                              setValue(Listanoticias);
                              Log.d("live","Cargando datos.......");

                          }else{
                              Log.d("live","el array esta vacio");
                          }

        }
    }
}
