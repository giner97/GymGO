package com.example.giner.gymgo.Gymgo.Dialogos;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giner.gymgo.Objetos.Dieta;
import com.example.giner.gymgo.Objetos.Rutina;
import com.example.giner.gymgo.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class MuestraDatos_Dialog extends DialogFragment{

    //Variables

    private ArrayList<String>ejerciciosRutina;
    private ArrayList<String>platosDieta;
    private Rutina rutina;
    private Dieta dieta;
    private String tipoDato=null;

    private AlertDialog dialogo;
    private OnListener escuchador;
    private ViewGroup layout;

    public MuestraDatos_Dialog(){

    }

    public MuestraDatos_Dialog(ArrayList<String>ejerciciosRutina,Rutina rutina, ArrayList<String>platosDieta, Dieta dieta){

        if (ejerciciosRutina != null) {
            this.ejerciciosRutina=ejerciciosRutina;
            this.rutina=rutina;
            tipoDato="rutina";
        }
        else if(platosDieta!=null){
            this.platosDieta=platosDieta;
            this.dieta=dieta;
            tipoDato="dieta";
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Datos de la "+tipoDato);
        View customDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_muestradatos, null);

        layout = (ViewGroup)customDialog.findViewById(R.id.content);

        int multiplicador=0;

        if (rutina != null) {

            for (int j = 0; j < rutina.getDias(); j++) {

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragmento_muestra, null, false);

                TextView dia = (TextView) relativeLayout.findViewById(R.id.dia);
                dia.setText("Dia "+(j+1));

                TextView dato1 = (TextView) relativeLayout.findViewById(R.id.dato1);
                dato1.setText(ejerciciosRutina.get(0+multiplicador));
                TextView dato2 = (TextView) relativeLayout.findViewById(R.id.dato2);
                dato2.setText(ejerciciosRutina.get(1+multiplicador));
                TextView dato3 = (TextView) relativeLayout.findViewById(R.id.dato3);
                dato3.setText(ejerciciosRutina.get(2+multiplicador));
                TextView dato4 = (TextView) relativeLayout.findViewById(R.id.dato4);
                dato4.setText(ejerciciosRutina.get(3+multiplicador));
                TextView dato5 = (TextView) relativeLayout.findViewById(R.id.dato5);
                dato5.setText(ejerciciosRutina.get(4+multiplicador));
                multiplicador = multiplicador + 5;

                layout.addView(relativeLayout);
            }
        }

        else if(dieta!=null){

            for (int j = 0; j < 7; j++) {

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragmento_muestra, null, false);

                TextView dia = (TextView) relativeLayout.findViewById(R.id.dia);
                dia.setText("Dia " + (j + 1));

                TextView dato1 = (TextView) relativeLayout.findViewById(R.id.dato1);
                dato1.setText("Desayuno: "+platosDieta.get(0+multiplicador));
                TextView dato2 = (TextView) relativeLayout.findViewById(R.id.dato2);
                dato2.setText("Almuerzo: "+platosDieta.get(1+multiplicador));
                TextView dato3 = (TextView) relativeLayout.findViewById(R.id.dato3);
                dato3.setText("Comida: "+platosDieta.get(2+multiplicador));
                TextView dato4 = (TextView) relativeLayout.findViewById(R.id.dato4);
                dato4.setText("Merienda: "+platosDieta.get(3+multiplicador));
                TextView dato5 = (TextView) relativeLayout.findViewById(R.id.dato5);
                dato5.setText("Cena: "+platosDieta.get(4+multiplicador));
                multiplicador = multiplicador + 5;

                layout.addView(relativeLayout);
            }

        }

        //Seteo el layout en el diálogo
        builder.setView(customDialog);

        builder.setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toasty.success(getActivity(),"Se ha cambiado la rutina",Toast.LENGTH_SHORT).show();
                escuchador.onObjetoSeleccionado(rutina,dieta);
            }
        });

        builder.setNegativeButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toasty.info(getActivity(),"Se ha cancelado el cambio de rutina", Toast.LENGTH_SHORT).show();
                escuchador.onCancelled();
                dismiss();
            }
        });

        //Hago que el diálogo no sea cancelable pulsando fuera de él (diálogo modal)
        dialogo = builder.create();
        dialogo.setCanceledOnTouchOutside(false);

        //Devuelvo el AlertDialog ya configurado
        return dialogo;

    }

    public void setListener(OnListener listener){
        this.escuchador = listener;
    }

    //Construyo una interface que defina la callback onLogin()
    public interface OnListener{
        void onObjetoSeleccionado(Rutina rutinaSeleccionada, Dieta dietaSeleccionada);
        void onCancelled();
    }

}