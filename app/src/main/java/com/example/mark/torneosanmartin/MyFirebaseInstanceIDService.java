package com.example.mark.torneosanmartin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mark.torneosanmartin.modelo.Tokens;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    SharedPreferences preferencias;


    private static String TAG="FirebaseInstanceServiceId";




    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String TokenId= FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,TokenId);
        sendTokenServer(TokenId);
    }

    private void sendTokenServer(final String tokenId) {
         final CollectionReference dataReference=FirebaseFirestore.getInstance().collection("users");
         //obtengo una referencias de preferencias compartidas
       Context context=getApplicationContext();
         //la referencia compartida tendra el nombre tokens
       preferencias=context.getSharedPreferences("tokens",context.MODE_PRIVATE);
         //obtengo un atributo guardado
        Map<String,String> map= (Map<String, String>) preferencias.getAll();
        for(Map.Entry<String,String> entry:map.entrySet()){
            Log.d(TAG,"valor de  key :"+entry.getKey());
            Log.d(TAG,"valor de  contenido :"+entry.getValue());

        }
       String tokenOld=preferencias.getString("tokenid","noexiste");
        //pregunto si existe un token guardado
        //si no existe es por que es la primera vez que inicia la app
        //con lo cual se guarda el token en las preferencias y
        //se guarda el token en la base de datos

        Log.d(TAG,"valor de token old :"+tokenOld);
          if(tokenOld.equals("noexiste")){
              SharedPreferences.Editor editor=preferencias.edit();
              editor.putString("tokenid",tokenId);
              editor.commit();
              String version=Build.VERSION.RELEASE;
              Map<String,String> datos=new HashMap<>();
              datos.put("modelo",version);
              datos.put("idToken",tokenId);
             
              dataReference.document(tokenId).set(datos);
              Log.d(TAG,"no existe token, nuevo token "+tokenId);


          }else{
              //si ya existe un token es por que en la base de datos ya se guardo un token
              //y en las preferencias tambien tenemos un token guardado
              //tenemos que buscar el token en la base de datos apartir de la preferencia guardada
              //y remplazarla por el nuevo token
              dataReference.document(tokenOld).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      String version=Build.VERSION.RELEASE;
                      SharedPreferences.Editor editor=preferencias.edit();
                      editor.putString("tokenid",tokenId);
                      editor.commit();
                      Map<String,String> datos=new HashMap<>();
                      datos.put("modelo",version);
                      datos.put("idToken",tokenId);
                      dataReference.document(tokenId).set(datos);
                      Log.d(TAG,"se renonvo el token "+tokenId);

                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {

                  }
              });

          }

    }
}
