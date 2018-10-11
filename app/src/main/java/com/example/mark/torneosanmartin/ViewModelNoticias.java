package com.example.mark.torneosanmartin;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mark.torneosanmartin.modelo.Noticia;
import com.example.mark.torneosanmartin.modelo.Tokens;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewModelNoticias extends ViewModel {

    private NoticiasLiveData noticiasLiveData;
    private List<Tokens> ListaTokens;
    private FirebaseFirestore db;
    private String TAG="ViewModel";


    public ViewModelNoticias() {
         noticiasLiveData=new NoticiasLiveData();
         ListaTokens=new ArrayList<Tokens>();
         db=FirebaseFirestore.getInstance();
         CargarToken();
    }


    public LiveData<List<Noticia>> getLiveData(){
        return noticiasLiveData;
    }


    public List<Tokens> getListaTokens(){
        return ListaTokens;
    }

    public  void CargarToken(){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ListaTokens=task.getResult().toObjects(Tokens.class);
                Log.d(TAG,"cantidad de objetos seleccionados : "+ListaTokens.size());
            }
        });



    }


}
