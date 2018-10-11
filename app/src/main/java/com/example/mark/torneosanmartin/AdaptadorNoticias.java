package com.example.mark.torneosanmartin;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mark.torneosanmartin.modelo.Noticia;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdaptadorNoticias extends RecyclerView.Adapter<AdaptadorNoticias.holder> {

    List<Noticia> ListaNoticia;
    LayoutInflater inflater;
    SparseBooleanArray selectedItems;
    onItemClickListener  listener;
    Context context;
    private String TAG="Adaptadornoticias";


    interface onItemClickListener{
        void ItemClick(int position);
        void ItemLongClick(int position);
        void desactiveMode();

    }

    public AdaptadorNoticias(Context context) {
        ListaNoticia=new ArrayList<>();
        selectedItems = new SparseBooleanArray();
        inflater=LayoutInflater.from(context);
        this.context =context;

    }

    void setListener(onItemClickListener listener){
        this.listener=listener;
    };


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =inflater.inflate(R.layout.item_noticias,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        if(ListaNoticia!=null) {
            Log.d("cantidad del listado: ",String.valueOf(ListaNoticia.size()));
            Log.d("posicion : ",String.valueOf(position));
            Log.d("nombre : ",String.valueOf(ListaNoticia.get(position).getTitulo()));
            Log.d("grupos : ",String.valueOf(ListaNoticia.get(position).getCuerpo()));
            Log.d("año : ",String.valueOf(ListaNoticia.get(position).getFecha()));

            holder.setDatos(ListaNoticia.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(ListaNoticia!=null){
            return ListaNoticia.size();
        }else{return 0;}

    }

    public void setdatos(List<Noticia> noticias){
        if(noticias!=null){
            selectedItems.clear();
            this.ListaNoticia.clear();
            this.ListaNoticia.addAll(noticias);
            notifyDataSetChanged();}
    }


    public int getCountSelect(){
        if (selectedItems!=null){
            return selectedItems.size();}
        else{
            return 0;
        }
    }




    public class holder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        private TextView titulo;
        private TextView cuerpo;
        private TextView fecha;
        //private TextView grupos;
        private ImageView imagen;
        private FrameLayout layout;


        public holder(View itemView) {
            super(itemView);
            titulo=(TextView) itemView.findViewById(R.id.textView_titulo_noticia);
            cuerpo=(TextView) itemView.findViewById(R.id.textView_cuerpo_noticia);
            fecha=(TextView) itemView.findViewById(R.id.textView_fecha_noticia);
            //grupos=(TextView) itemView.findViewById(R.id.grupos);
            imagen=(ImageView) itemView.findViewById(R.id.imagenView_foto_noticias);
            layout=(FrameLayout) itemView.findViewById(R.id.layout_noticia);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        public void setDatos(final Noticia noticias){
            titulo.setText(noticias.getTitulo());
            cuerpo.setText(noticias.getCuerpo());
            SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
            Date fechas=(Date)noticias.getFecha();
            String fechatexto=formater.format(fechas);
            Log.d("fecha :",fechatexto);
            fecha.setText(fechatexto);
            RequestOptions requestOptions=new RequestOptions();
            requestOptions.placeholder(R.drawable.placeholder);
            requestOptions.override(400,300);
            if(noticias.getUrlImagen()!=null){



               Glide.with(context)
                        //.asBitmap()
                        .load(noticias.getUrlImagen())
                        .apply(requestOptions)
                        .into(imagen);
                        /*.into(new BitmapImageViewTarget(imagen) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                // Do bitmap magic here
                                if (resource == null) {
                                    Log.d(TAG,"esto es null");
                                }else{
                                    Log.d(TAG,"ancho de la imagen :" +String.valueOf(resource.getWidth()));
                                    Log.d(TAG,"alto de la imagen :" +String.valueOf(resource.getHeight()));
                                    Log.d(TAG,"cantidad de KB :" +String.valueOf(resource.getByteCount()/1024));

                                }


                                //noticias.setImagenBitmap(resource);
                                super.setResource(resource);
                            }
                        });*/


                /*Glide.with(context)
                        .load(noticias.getUrlImagen())
                        .into(imagen);*/
            }else{

                /*Glide.with(context)
                        .asBitmap()
                        .load(R.drawable.futbol)
                        .into(new BitmapImageViewTarget(imagen) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                // Do bitmap magic here
                                noticias.setImagenBitmap(resource);
                                super.setResource(resource);
                            }
                        });*/

                Glide.with(context)
                        .load(R.drawable.mundial)
                        .apply(requestOptions)
                        .into(imagen);
            }

            //grupos.setText(String.valueOf(torneos.getCantidadGrupos()));
            if (noticias.isEstado()){
                Log.d("estado",String.valueOf(noticias.isEstado()));
                layout.setVisibility(View.VISIBLE);
            }else{
                layout.setVisibility(View.INVISIBLE);
            }
        };

        @Override
        public void onClick(View view) {
            Log.d("clickkkk", String.valueOf(getAdapterPosition()));
            if(listener!=null){
                Log.d("clickkkk", String.valueOf(getAdapterPosition()));
                listener.ItemClick(getAdapterPosition());
            }

        }


        @Override
        public boolean onLongClick(View view) {
            if(listener!=null) {
                listener.ItemLongClick(getAdapterPosition());
            }
            return true;
        }
    }

    public Noticia getNoticia (int position){
        return ListaNoticia.get(position);
    }


    public void setSelect(int pos){

        //get(int ,bollean) devuelve el bolleano mapeado en esa pos , si no existe nada en esa posicion devuelve el booleano asignado.
        if(selectedItems.get(pos,false)){
            ListaNoticia.get(pos).setEstado(false);
            selectedItems.delete(pos);
        }else{
            selectedItems.put(pos,true);
            ListaNoticia.get(pos).setEstado(true);
            Log.d(TAG,"items seleccionados en array boleano "+selectedItems.size());
            Log.d(TAG,"posicion de dato : " + selectedItems.get(pos));


        }
        notifyItemChanged(pos);
    }

    public void ClearSelected(){
        selectedItems.clear();
    }


    public void clearSelection(){

        if(selectedItems.size()>0) {
          Log.d(TAG,"tamaño de select list :"+selectedItems.size());
          Log.d(TAG,"tamaño de lista noticias :"+ListaNoticia.size());
           if(selectedItems.size()<=ListaNoticia.size()){
            for (int i = 0; i < selectedItems.size(); i++) {
                int position = selectedItems.keyAt(i);
                Log.d(TAG,"tamaño lista select : "+selectedItems.size());
                Log.d(TAG,"posisicon en select : "+position);
                Log.d(TAG,"tamaño de lista noticias : "+ListaNoticia.size());
                Log.d(TAG,"Lista noticias :"+ListaNoticia.get(position));
                ListaNoticia.get(position).setEstado(false);

            }
               ClearSelected();
               notifyDataSetChanged();
           }else{

               ClearSelected();
               notifyDataSetChanged();
           }

        }
    }






    public void eliminarItems(){
        FirebaseFirestore  db =FirebaseFirestore.getInstance();

        WriteBatch batch=db.batch();
        int i=0;

      if(selectedItems.size()>0){

          final ProgressDialog progressDialog = new ProgressDialog(context);
          progressDialog.setTitle("Eliminando...");
          progressDialog.show();
          Log.d(TAG,"cantidad de items seleccionados "+ selectedItems.size());
        for(i=0;i<selectedItems.size();i++){
            final int position=selectedItems.keyAt(i);
            String id=ListaNoticia.get(i).getId();
            //ListaNoticia.remove(position);
            //selectedItems.delete(position);
            Log.d(TAG, "posicion a leiminar "+position);
            Log.d(TAG, "id a eliminar : "+ id);
            DocumentReference documentReference=db.collection("noticias").document(id);
                    batch.delete(documentReference);

                    String nombre=ListaNoticia.get(i).getNombre();
                    if(nombre!=null){

                        StorageReference ref = FirebaseStorage.getInstance().getReference().child("/noticias/" +nombre );
                        ref.delete();

                    }


        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                  ClearSelected();
                  //clearSelection();
                  listener.desactiveMode();
                  notifyDataSetChanged();
                  progressDialog.dismiss();
              }
          });
        }
        //notifyDataSetChanged();}

    }

}
