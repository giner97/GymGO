package com.example.giner.gymgo.Gymgo.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.giner.gymgo.Objetos.Plato;
import com.example.giner.gymgo.R;

public class DatosPlatoDialog extends DialogFragment {

    //Variables

        private Plato plato;
        private String tipoComida;

        private AlertDialog dialog;

    //Widgets

        private TextView nombre;
        private TextView peso;
        private TextView tipo_Comida;

    //Constructores

        public DatosPlatoDialog(){

        }

        public DatosPlatoDialog(Plato plato, String tipoComida){
            this.plato=plato;
            this.tipoComida=tipoComida;
        }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Datos plato");
        View customDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_datos_plato, null);

        //Instancio los widgets

            nombre = (TextView)customDialog.findViewById(R.id.nombrePlato);
            peso = (TextView)customDialog.findViewById(R.id.peso);
            tipo_Comida = (TextView)customDialog.findViewById(R.id.tipoComida);

        //Añado los datos

            nombre.setText(plato.getNombre());
            peso.setText(plato.getPeso());
            tipo_Comida.setText(tipoComida);

        //Seteo el layout en el dialogo

            builder.setView(customDialog);

        builder.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        dialog = builder.create();
        return dialog;

    }
}
