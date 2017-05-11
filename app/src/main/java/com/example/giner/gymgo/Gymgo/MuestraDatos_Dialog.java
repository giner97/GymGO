package com.example.giner.gymgo.Gymgo;


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

import es.dmoral.toasty.Toasty;

public class MuestraDatos_Dialog extends DialogFragment{

    //Variables

        private Rutina rutina;
        private Dieta dieta;
        private String tipoDato=null;

    private AlertDialog dialogo;
    private OnListener escuchador;
    private ViewGroup layout;

    public MuestraDatos_Dialog(Rutina rutina,Dieta dieta){

        if (rutina != null) {
            rutina = new Rutina();
            this.rutina=rutina;
            tipoDato="rutina";
        }
        else if(dieta!=null){
            dieta = new Dieta();
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

            for (int j = 0; j < 7; j++) {

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragmento_muestra, null, false);

                TextView dia = (TextView) relativeLayout.findViewById(R.id.dia);
                dia.setText("Prueba");

                TextView dato1 = (TextView) relativeLayout.findViewById(R.id.dato1);
                dato1.setText(Integer.toString(j));
                TextView dato2 = (TextView) relativeLayout.findViewById(R.id.dato2);
                dato2.setText(Integer.toString(j));
                TextView dato3 = (TextView) relativeLayout.findViewById(R.id.dato3);
                dato3.setText(Integer.toString(j));
                TextView dato4 = (TextView) relativeLayout.findViewById(R.id.dato4);
                dato4.setText(Integer.toString(j));
                TextView dato5 = (TextView) relativeLayout.findViewById(R.id.dato5);
                dato5.setText(Integer.toString(j));
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
                dia.setText(dieta.getPlatos().get(1 + multiplicador).getPlato());
                TextView dato2 = (TextView) relativeLayout.findViewById(R.id.dato2);
                dia.setText(dieta.getPlatos().get(2 + multiplicador).getPlato());
                TextView dato3 = (TextView) relativeLayout.findViewById(R.id.dato3);
                dia.setText(dieta.getPlatos().get(3 + multiplicador).getPlato());
                TextView dato4 = (TextView) relativeLayout.findViewById(R.id.dato4);
                dia.setText(dieta.getPlatos().get(4 + multiplicador).getPlato());
                TextView dato5 = (TextView) relativeLayout.findViewById(R.id.dato5);
                dia.setText(dieta.getPlatos().get(5 + multiplicador).getPlato());
                multiplicador = multiplicador + 5;

                layout.addView(relativeLayout);
            }

        }

        //Seteo el layout en el diálogo
        builder.setView(customDialog);

        builder.setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                escuchador.onObjetoSeleccionado(rutina,dieta);
            }
        });

        builder.setNegativeButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
    }

}
