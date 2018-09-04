package com.example.mark.torneosanmartin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.mark.torneosanmartin.modelo.Equipos;

public class AgregarEquipo extends DialogFragment {

    private dialogListener listener;
    private Equipos equipo;
    EditText nombre;
    EditText delegado;
    EditText documento;
    EditText telefono;

    public interface dialogListener{
        void insertar(Equipos persona);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        listener=(dialogListener) getActivity();
        View view=inflater.inflate(R.layout.layout_agregar_equipo,null);
        nombre=(EditText)view.findViewById(R.id.nombre);
        delegado=(EditText)view.findViewById(R.id.delegado);
        telefono=(EditText)view.findViewById(R.id.telefono);
        equipo=new Equipos();
        builder.setView(view)

                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        equipo.setNombre(nombre.getText().toString());
                        equipo.setDelegado(delegado.getText().toString());
                        equipo.setTelefono(telefono.getText().toString());
                        equipo.setDIF(0);
                        equipo.setGC(0);
                        equipo.setPE(0);
                        equipo.setPG(0);
                        equipo.setPJ(0);
                        equipo.setGC(0);
                        equipo.setGF(0);
                        equipo.setPTS(0);
                        listener.insertar(equipo);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setTitle("Añadir Nuevo Equipo");
        return builder.create();

    }



}
