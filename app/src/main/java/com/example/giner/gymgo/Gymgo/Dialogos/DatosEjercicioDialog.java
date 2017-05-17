package com.example.giner.gymgo.Gymgo.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giner.gymgo.Objetos.Ejercicio;
import com.example.giner.gymgo.Objetos.Rutina_Ejercicio;
import com.example.giner.gymgo.R;

import org.w3c.dom.Text;

import es.dmoral.toasty.Toasty;

public class DatosEjercicioDialog extends DialogFragment {

    //Variables

        private Ejercicio ejercicio;
        private Rutina_Ejercicio rutinaEjercicio;
        private String grupoMuscular;

        private AlertDialog dialogo;

    //Widgets

        private TextView nombre;
        private TextView descripcion;
        private TextView explicacion;
        private TextView grupo;
        private TextView series;
        private TextView repeticiones;

    //Constructores

        public DatosEjercicioDialog(){

        }

        public DatosEjercicioDialog(Ejercicio ejercicio, Rutina_Ejercicio rutinaEjercicio, String grupoMuscular){
            this.ejercicio=ejercicio;
            this.rutinaEjercicio=rutinaEjercicio;
            this.grupoMuscular=grupoMuscular;
        }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Datos ejercicio" );
        View customDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_datos_ejercicio, null);

        //Instancio los widgets

            nombre = (TextView) customDialog.findViewById(R.id.nombreEjercicio);
            descripcion = (TextView)customDialog.findViewById(R.id.descripcion);
            explicacion = (TextView)customDialog.findViewById(R.id.explicacion);
            grupo=(TextView)customDialog.findViewById(R.id.grupo);
            series=(TextView)customDialog.findViewById(R.id.series);
            repeticiones=(TextView)customDialog.findViewById(R.id.repeticiones);

        //Añado los datos

            nombre.setText(ejercicio.getNombreEjercicio());
            descripcion.setText("-"+ejercicio.getDescripcion());
            explicacion.setText("-"+ejercicio.getExplicacion());
            grupo.setText(grupoMuscular);
            series.setText(Integer.toString(rutinaEjercicio.getSeries()));
            repeticiones.setText(Integer.toString(rutinaEjercicio.getRepeticiones()));

        //Seteo el layout en el diálogo
            builder.setView(customDialog);


        builder.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        dialogo = builder.create();

        return dialogo;
    }

}
