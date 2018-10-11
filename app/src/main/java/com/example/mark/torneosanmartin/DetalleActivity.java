package com.example.mark.torneosanmartin;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mark.torneosanmartin.modelo.Noticia;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetalleActivity extends AppCompatActivity {

    private TextView titulo;
    private TextView Contenido;
    private TextView fecha;
    private String url;
    private ImageView imagen;
    private Noticia noticia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_noticia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle=getIntent().getExtras();
        noticia=(Noticia) bundle.getSerializable("noticias");

        titulo=(TextView)findViewById(R.id.textView_titulo_noticia_detalle);
        Contenido=(TextView)findViewById(R.id.textView_contenido_detalle_noticia);
        imagen=(ImageView)findViewById(R.id.imageView_foto_noticia_detalle);
        fecha=(TextView) findViewById(R.id.textView_fecha_detalle_noticia);

        titulo.setText(noticia.getTitulo());
       Contenido.setText(noticia.getCuerpo());

        setupWindowAnimations();

       imagen.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                llaamarActividad();
           }
       });

        SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
        Date fechas=(Date)noticia.getFecha();
        String fechatexto=formater.format(fechas);
        Log.d("fecha :",fechatexto);

        RequestOptions requestOptions=new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        //fecha.setText(fechatexto);

          fecha.setText(fechatexto);
          url=noticia.getUrlImagen();
          //Log.d("url imagen",url);
          if(url!=null){

              Glide.with(this)
                      .load(url)
                      .apply(new RequestOptions().override(500,400))
                      .into(imagen);

          }else{

              Glide.with(this)
                      .load(R.drawable.mundial)
                      .apply(new RequestOptions().override(500,400))
                      .into(imagen);

          }

    }

    private void llaamarActividad() {

        Intent intent=new Intent(this,ImagenActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("noticias",noticia);
        intent.putExtras(bundle);

        View sharedView = imagen;
        String transitionName = "transitionImagen";

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this, sharedView, transitionName);
        startActivity(intent, transitionActivityOptions.toBundle());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {

            /*Slide slide = null;
                slide = new Slide();
            slide.setDuration(500);
            getWindow().setExitTransition(slide);*/
        }

}
