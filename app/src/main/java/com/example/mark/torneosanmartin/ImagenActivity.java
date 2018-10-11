package com.example.mark.torneosanmartin;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.transition.Slide;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.mark.torneosanmartin.modelo.Noticia;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ImagenActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private PhotoView photoView;
    Noticia noticia;
    private Bitmap bitmap;
    private String TAG="Imagenactivity";
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);

        toolbar=(Toolbar)findViewById(R.id.toolbar_imagen);
        photoView=(PhotoView)findViewById(R.id.imagen_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupWindowAnimations();
        Bundle bundle=getIntent().getExtras();
        noticia=(Noticia) bundle.getSerializable("noticias");
        String url=noticia.getUrlImagen();

        if(noticia.getUrlImagen()!=null){

               Glide.with(this)
                        .asBitmap()
                        .load(noticia.getUrlImagen())
                        //.apply(requestOptions)
                        .into(new BitmapImageViewTarget(photoView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                // Do bitmap magic here
                                bitmap=resource;
                                if(bitmap!=null) {
                                    Log.d(TAG, "tamaño de la imagen comprimida" + String.valueOf(resource.getByteCount() / 1024));

                                }
                                super.setResource(resource);
                            }
                        });



        }else{

                Glide.with(this)
                        .asBitmap()
                        .load(R.drawable.mundial)
                        //.apply(requestOptions)
                        .into(new BitmapImageViewTarget(photoView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                // Do bitmap magic here
                                noticia.setImagenBitmap(resource);
                                super.setResource(resource);
                            }
                        });

          }



       /* //Log.d("url imagen",url);
        if(url!=null){

            Glide.with(this)
                    .load(url)
                    .into(photoView);

        }else{

            Glide.with(this)
                    .load(R.drawable.futbol)
                    .into(photoView);

        }*/




    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setupWindowAnimations() {

      /*  Slide fade = new Slide();
        fade.setDuration(500);
        getWindow().setEnterTransition(fade);*/


    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.WEBP, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, System.currentTimeMillis() + ".jpg", null);
        Log.d("direccion",path);
        Log.d(TAG,String.valueOf(inImage.getByteCount()/1024));
        return Uri.parse(path);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_imagen,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }

        if( item.getItemId()==R.id.action_share){
            compartir(bitmap);
        }else if(item.getItemId()==R.id.action_save){

            // codigo para crear un directorio
           /* File carpetaInterna=new File(getFilesDir(),"carpetaInterna");
            carpetaInterna.mkdirs();
            File carpetaIntern=new File(Environment.getExternalStorageDirectory(),"carpetaInterna");
            carpetaIntern.mkdirs();*/
           firebaseStorage=FirebaseStorage.getInstance();
           StorageReference storageReference=firebaseStorage.getReference().child("noticias/"+noticia.getNombre());
           Log.d(TAG,"guardar imagen");

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Descargando...");
            progressDialog.show();

            // final File localFile=File.createTempFile("imagesfirestore","jpg");
            File directori= new File(Environment.getExternalStoragePublicDirectory(
                      Environment.DIRECTORY_PICTURES), " Imagenes, Torneo San Martín ");
            if(!directori.exists()){
                directori.mkdirs();
            }

            File archivo=new File(directori,noticia.getNombre());

            storageReference.getFile(archivo).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                   // Bitmap bitmap=BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    //photoView.setImageBitmap(bitmap);
                    progressDialog.dismiss();
                    mostrar("guardado");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    mostrar("error");
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Descargando " + (int) progress + "%");
                }

            });

           /*  new ImageSaver(this)
                    .setExternal(true)
                    //se asigna un nombre a la imagen , obteniendo una cadena numerica aleatoria
                    .setFileName(String.valueOf(System.currentTimeMillis())+".jpg")
                    .setDirectoryName("Torneo San Martín U.N.Sa")
                    .save(bitmap);*/

          /* String direccion= guardar();
            Log.d("direccion",direccion);
            Toast.makeText(this, "Imagen Guardada", Toast.LENGTH_SHORT).show();*/
        }


        return super.onOptionsItemSelected(item);
    }

    private void mostrar(String mensaje) {
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }


    public String guardar(){

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;


    }


    private String getFilename() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() +"/torneoSanMartin");

        Log.d("direccion",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".webp");
        return uriSting;
    }

    public void compartir(Bitmap bitmap){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, getImageUri(this,bitmap));
        startActivity(Intent.createChooser(share,"Comparir via "));
    }


    @Override
    public void onBackPressed() {
        finish();
    }

}
