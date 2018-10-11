package com.example.mark.torneosanmartin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mark.torneosanmartin.modelo.Noticia;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AgregarNoticia extends DialogFragment {


    private AgregarNoticia.dialogListenerNoticia listener;
    private Noticia noticia;
    EditText Titulo;
    EditText Contenido;
    private Button cargarImagen;
    private Uri filePath;
    private byte[] datos;
    private ImageView imageView;
    private  PanelActivity context;
    private TextInputLayout inputLayoutTitulo;
    private TextInputLayout inputLayoutcontenido;
    private Button aceptar;
    private Button cancelar;
    private String TAG="agregarnoticia";

    public interface dialogListenerNoticia{
        void insertarNoticia(Noticia noticia, byte[] datos);
        void seleccionarImagen();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        listener=(AgregarNoticia.dialogListenerNoticia)getActivity();
        View view=inflater.inflate(R.layout.layout_agregar_noticia,null);
        Titulo=(EditText)view.findViewById(R.id.edit_titulo_noticia);
        Contenido=(EditText)view.findViewById(R.id.edit_cuerpo_noticia);
        cargarImagen=(Button)view.findViewById(R.id.button_agregar);
        aceptar=(Button)view.findViewById(R.id.button_noticia_aceptar);
        cancelar=(Button)view.findViewById(R.id.button_noticia_cancelar);
        imageView=(ImageView) view.findViewById(R.id.imageView_foto);
        inputLayoutcontenido=(TextInputLayout)view.findViewById(R.id.textinput_contenido_noticia);
        inputLayoutTitulo=(TextInputLayout)view.findViewById(R.id.textinput_titulo_noticia);
        //año=(EditText)view.findViewById(R.id.edit_año_torneo);
        noticia=new Noticia();

        Titulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(inputLayoutTitulo.isErrorEnabled()){
                    inputLayoutTitulo.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){

                    inputLayoutTitulo.setErrorEnabled(true);
                    inputLayoutTitulo.setError("no se permiten campos vacios");

                }else{

                    inputLayoutTitulo.setErrorEnabled(false);

                }


            }
        });

        Contenido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(inputLayoutcontenido.isErrorEnabled()){
                    inputLayoutcontenido.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){

                    inputLayoutcontenido.setErrorEnabled(true);
                    inputLayoutcontenido.setError("no se permiten campos vacios");

                }else{

                    inputLayoutcontenido.setErrorEnabled(false);

                }

            }
        });
        //---------------caragar Imagen --------------//
        cargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // context.seleccionarImagen();
                buscarImagen();
            }
        });


        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //inputLayoutcontenido.setError("no se permite campo vacio");
               // inputLayoutTitulo.setError("no se permite campo vacio");

               // cerrar();
                if(!Titulo.getText().toString().equals("")) {
                    Log.d("titulo ",Titulo.getText().toString());
                    if(!Contenido.getText().toString().equals("")){
                        Log.d("titulo ",Titulo.getText().toString());
                        noticia.setTitulo(Titulo.getText().toString().toUpperCase().trim());
                        noticia.setCuerpo( Contenido.getText().toString());
                        // torneos.setAño(Integer.parseInt(año.getText().toString()));
                        Date fecha=new Date(Calendar.getInstance().getTimeInMillis());
                        noticia.setFecha(fecha);
                        Toast.makeText(context,"todo ok",Toast.LENGTH_LONG);
                        listener.insertarNoticia(noticia,datos);
                        cerrar();

                    }else{
                        Toast.makeText(context,"contenido vacio",Toast.LENGTH_LONG);
                        inputLayoutcontenido.setErrorEnabled(true);
                        inputLayoutcontenido.setError("no se permite campo vacio");
                    }
                }else{
                    Toast.makeText(context,"titulo vacio",Toast.LENGTH_LONG);
                    inputLayoutTitulo.setErrorEnabled(true);
                    inputLayoutTitulo.setError("no se permite campo vacio");
                }
            }
        });


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("titulo ",Titulo.getText().toString());
                cerrar();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    private void cerrar() {
        this.dismiss();
    }


    private void buscarImagen() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(),this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Log.d(TAG,"uri de la imagen seleccionada: "+filePath);
            Log.d(TAG,"result code : "+String.valueOf(requestCode));
            if (resultCode == RESULT_OK) {
                filePath = result.getUri();
                Log.d(TAG,"nombre de la imagen seleccionada: "+filePath);
                if(!filePath.equals("")){
                 Bitmap salida= null;
                 try {

                    salida = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);

                      Log.d(TAG,"cantidad de KBytes de la foto: "+String.valueOf(salida.getByteCount()/1024));
                     Log.d(TAG,"altura de la foto:"+String.valueOf(salida.getHeight()));
                     Log.d(TAG,"ancho de la foto :"+String.valueOf(salida.getWidth()));
                 } catch (IOException e) {
                    e.printStackTrace();
                 }
                cargarBitmap(salida);

             /*   Glide.with(getContext())
                        .asBitmap().load(filePath)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        });*/

                //cargarBitmap(bitmap);
                }else{datos=null;}
            }else{datos=null;}
                  /*  int maxlength=Math.max(bitmap.getWidth(),bitmap.getHeight());
                    float proporcion=800/maxlength;
                    int ancho= Math.round(bitmap.getWidth()*proporcion);
                   int alto= Math.round(bitmap.getHeight()*proporcion);
                    Log.d("ancho de imagen",String.valueOf(bitmap.getWidth()));
                    Log.d("alto de imagen",String.valueOf(bitmap.getHeight()));
                    Bitmap imagenfinal= Bitmap.createScaledBitmap(bitmap,500,700,true);*/
            //filePath=UtilFile.getImageUri(this,bitmap)

        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            datos=null;
            // Exception error = result.getError();
        }
    }

    private void cargarBitmap(Bitmap bitmap1) {

        //redimensiono la imagen y obtengo el nuevo bitmap
       Bitmap bitmap= Bitmap.createScaledBitmap(bitmap1,768,1024,true);

      //  Toast.makeText(getActivity(), bitmap.getByteCount() / 1000 + " KB", Toast.LENGTH_LONG);
        // Bitmap cropped = cropImageView.getCroppedImage();
       Log.d(TAG,"bitmap.getByteCount KB despues de comprimir :"+ String.valueOf( bitmap.getByteCount()/1000));
        //
        //comprimiimagen(imagenActual);
        //filePath=Uri.fromFile(compressedImage);
        // hago una compresion de de resolucion del bitmap
        for (int i = 1; i < 11; i++) {

            datos = getBytesFromBitmap(bitmap, 100 / i);
            Toast.makeText(getActivity(), "compresion del "+datos.length / 1024 + " KB", Toast.LENGTH_LONG);
            Log.d(TAG,"bitmap.getByteCount KB"+ String.valueOf( datos.length/1024));
            if (datos.length <= 70000) {
                break;
            }
        }
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(datos, 0, datos.length);
        imageView.setImageBitmap(compressedBitmap);
    }


    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }



    private File getAlbumStorageDir(String albumName) {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=(PanelActivity)context;
    }
}
