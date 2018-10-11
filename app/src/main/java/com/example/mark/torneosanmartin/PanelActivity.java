package com.example.mark.torneosanmartin;

import android.Manifest;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v7.view.ActionMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mark.torneosanmartin.FCM.Data;
import com.example.mark.torneosanmartin.FCM.FirebaseCloudMessage;
//import com.example.mark.torneosanmartin.modelo.Equipos;
import com.example.mark.torneosanmartin.modelo.Noticia;
//import com.example.mark.torneosanmartin.modelo.Torneos;
import com.example.mark.torneosanmartin.modelo.Tokens;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class PanelActivity extends AppCompatActivity implements
AgregarNoticia.dialogListenerNoticia,NoticiasFragment.actionListener{


    //private AdaptadorEquipos adaptador;
    private ViewPager mViewPager;
    private SectionPageAdapter mSectionsPagerAdapter;
    private FirebaseFirestore db;
    private TabLayout tabLayout;
   // private AgregarEquipo dialogAgregar;
    private AgregarNoticia dialogoAgregarNoticia;
    private ActionMode actionMode;
    Toolbar toolbar;
    private static final int WRITE_PERIMISION =1 ;
    FirebaseStorage storage;
    StorageReference storageReference;
    private final String TAG="fcm";
    private final String key="AAAAMBUT_FA:APA91bGDdQ0kzadTaWhwG7dMnGugKaSzF1swOEjv3U0pVnmnjE9_HSVtRRNJ1B7Z_wXeINKYKeM1KS4fumYRqXuGJ5zi0v9GEgSVkeOV0uBxO2WLEYJ96qbJs5lfcSxFezTsyo-LFiTq";
    private ViewModelNoticias ModeloNoticias;
    private List<Tokens> listdoTokens;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        solicitarpermiso();
        setContentView(R.layout.activity_panel);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db=FirebaseFirestore.getInstance();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
         mViewPager = (ViewPager) findViewById(R.id.container);
         mViewPager.setAdapter(mSectionsPagerAdapter);
         tabLayout = (TabLayout) findViewById(R.id.tabs);
         mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
         tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        ModeloNoticias=ViewModelProviders.of(this).get(ViewModelNoticias.class);

          ///configuracion de page adapter

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabT);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               switch (tabLayout.getSelectedTabPosition()){

                   case 0:
                       /* AgregarTorneo dialogAgregarTorneo= new AgregarTorneo();
                         dialogAgregarTorneo.showNow(getSupportFragmentManager(),"agregar torneo");
                       break;*/

                   case 1:
                       /*dialogAgregar=new AgregarEquipo();
                       dialogAgregar.showNow(getSupportFragmentManager(),"agregar equipo");
                       break;*/

                   case 2:
                        dialogoAgregarNoticia=new AgregarNoticia();
                        dialogoAgregarNoticia.showNow(getSupportFragmentManager(),"agregar noticia");
                        break;

                   default:
                   break;

               }


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
            return true;
        }

       // return super.onOptionsItemSelected(item);



  /*  @Override
    public void insertar(Equipos equipo) {
        db.collection("equuipos").document(equipo.getNombre()).set(equipo);
    }
 /*
    @Override
    public void insertarTorneo(Torneos torneos,byte[] datosImagen) {
        uploadImage(datosImagen,torneos);
       // torneos.setUrlImagen(uri.toString());

    }*/

    @Override
    public void insertarNoticia(Noticia noticia, byte[] datos) {
        uploadImageNoticia(datos,noticia);
    }

    @Override
    public void seleccionarImagen() {
       // buscarImagen();
    }

    @Override
    public void activeMode() {
              if(actionMode==null){
                  actionMode=startSupportActionMode(new ActionModeCallback());
              }
    }

    @Override
    public void desactiveMode() {
                actionMode.finish();
                actionMode=null;
    }

    @Override
    public void settextmode(int count) {
        actionMode.setTitle("Seleccionados "+count);
        actionMode.invalidate();

    }


    public class ActionModeCallback implements android.support.v7.view.ActionMode.Callback{

        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {

            if (item.getItemId() == R.id.action_delete) {
                   eliminar();
            }
            if (item.getItemId() == R.id.action_select) {
                    seleccionar();

                return false;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {

          /* TorneosFragment torneosFragment=(TorneosFragment)mViewPager.getAdapter().instantiateItem(mViewPager, 0);*/
            NoticiasFragment fragment= (NoticiasFragment) mViewPager.getAdapter().instantiateItem(mViewPager,0);
           fragment.ClearList();
            actionMode=null;

            Log.d(TAG,"action mode destruido");
        }
    }

    private void seleccionar() {



    }

    private void eliminar() {
            NoticiasFragment fragment= (NoticiasFragment) mViewPager.getAdapter().instantiateItem(mViewPager,0);
            fragment.eliminar();
    }


    


    private  void solicitarpermiso(){

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){

                }

                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERIMISION);
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean mostrar=false;
        if(requestCode== WRITE_PERIMISION){
            if(grantResults.length==2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){


            }else{

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    mostrar=shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                if(mostrar){

                }else{
                    Toast.makeText(this,"los permisos fueron denegados",Toast.LENGTH_LONG);
                }
            }


        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }



   /* private void uploadImage(byte[] datos, final Torneos torneos) {
        final Uri[] uri = {null};

        if (datos.length != 0) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = FirebaseStorage.getInstance().getReference().child("/images"+ UUID.randomUUID().toString());
            ref.putBytes(datos)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                           torneos.setUrlImagen(taskSnapshot.getDownloadUrl().toString());
                            db.collection("torneos").document(torneos.getNombre()).set(torneos);
                            int cantidad=1;

                            for(int i=1 ; i<=torneos.getCantidadGrupos();i++){
                                Grupo grupo=new Grupo();
                                grupo.setNombre("Grupo :" + i );
                                grupo.setId(i);
                                db.collection("torneos").document(torneos.getNombre()).collection("grupos").document(String.valueOf(i)).set(grupo);
                            }

                            Toast.makeText(PanelActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PanelActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });


        }

    }*/

    private void uploadImageNoticia(byte[] datos, final Noticia noticia) {
        final Uri[] uri = {null};

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Cargndo...");
        progressDialog.show();
       if(datos!=null) {
           if (datos.length != 0) {

               final String nombre=UUID.randomUUID().toString()+".jpg";
               StorageReference ref = FirebaseStorage.getInstance().getReference().child("/noticias/" +nombre );
               ref.putBytes (datos)
                       .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                               noticia.setUrlImagen(taskSnapshot.getDownloadUrl().toString());
                               noticia.setNombre(nombre);
                               Log.d(TAG,taskSnapshot.getDownloadUrl().getPath());

                               db.collection("noticias").add(noticia).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                   @Override
                                   public void onComplete(@NonNull Task<DocumentReference> task) {
                                       enviarMensajeNotificacion(noticia.getTitulo(), noticia.getCuerpo());
                                       progressDialog.dismiss();
                                   }
                               });

                               Toast.makeText(PanelActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               progressDialog.dismiss();
                               Toast.makeText(PanelActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();


                           }
                       })
                       .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                               double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                       .getTotalByteCount());
                               progressDialog.setMessage("Uploaded " + (int) progress + "%");
                           }
                       });
           }

       }else{

           db.collection("noticias").add(noticia).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
               @Override
               public void onComplete(@NonNull Task<DocumentReference> task) {
                   enviarMensajeNotificacion(noticia.getTitulo(), noticia.getCuerpo());
                   progressDialog.dismiss();
               }
           });

       }
    }

    private void enviarMensajeNotificacion(String titulo,String mensaje) {


        Log.d(TAG, "sendMessageToDepartment: sending message to selected departments.");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/fcm/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Fcm fcmAPI = retrofit.create(Fcm.class);

        //attach the headers
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "key="+key);

        //send the message to all tokens

        listdoTokens=ModeloNoticias.getListaTokens();

        Log.d(TAG ,"cantidad de tokens de la lista"+String.valueOf(listdoTokens.size()));




            Data data = new Data();
            data.setHello(mensaje);
            data.setCodigo(titulo);
            //data.setData_type("Admin_Broadcst");

        for(int i=0;i<listdoTokens.size();i++){

            FirebaseCloudMessage firebaseCloudMessage = new FirebaseCloudMessage();
            firebaseCloudMessage.setData(data);
            firebaseCloudMessage.setTo(listdoTokens.get(i).getIdToken());
            Log.d(TAG, "sendMessageToDepartment: sending to token: " + listdoTokens.get(i).getIdToken());

            Call<ResponseBody> call = fcmAPI.send(headers, firebaseCloudMessage);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d(TAG, "onResponse: Server Response: " + response.toString());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "onFailure: Unable to send the message: " + t.getMessage());
                    Toast.makeText(PanelActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });

        }




/*
        //adjuntar los headers
        Data data=new Data();
        data.setMessaje(mensaje);
        data.setTitle(titulo);
        data.setData_type("notificacion");

        FirebaseCloudMessage firebaseCloudMessage=new FirebaseCloudMessage();
        firebaseCloudMessage.setData(data);
        firebaseCloudMessage.setTo("c7YXHoPVTB0:APA91bGsaVZTgdazCN9bS-78qCRwdLcLMxXAIW_EMoeLcVLjyIzLv21dSz5MLujKicgUptmB2Gcko1iv55v_LdJmCIaQRjm-rVbHXSQKHPNEhWR1FLufQJwE8Q0l7rLHYHmS390S7OEz");


        Fcm  apiService =  this.getClient().create(Fcm.class);
        //Call<ResponseBody> call=fcmapi.sendChatNotification(firebaseCloudMessage);

        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(firebaseCloudMessage);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("mensaje enviado","respuesta del  "+ response.headers().toString());
                Log.d("mensaje ","respuesta del  "+ response.message().toString());
                Log.d("mensaje body","respuesta del  "+ response.body().toString());
                Log.d("mensaje response","respuesta del mensaje "+response.toString()+response.message());
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });*/
    }



    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        String BASE_URL="https://fcm.googleapis.com/fcm/";
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
