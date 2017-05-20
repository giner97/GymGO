package com.example.giner.gymgo.Gymgo.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.giner.gymgo.Objetos.Revision_user;
import com.example.giner.gymgo.R;

public class DatosRevisionesDialog extends DialogFragment {

    //Variables

        private Revision_user revision;
        private AlertDialog dialog;

    //Widgets

        private TextView fecha;
        private TextView peso;
        private TextView altura;
        private TextView imc;

    //Constructor

        public DatosRevisionesDialog(Revision_user revision){
            this.revision=revision;
        }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Datos de la revisión seleccionada: ");
        View customDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_muestra_datos_revision,null);

        //Instancio los widgets

            fecha = (TextView)customDialog.findViewById(R.id.fechaRevision);
            peso = (TextView)customDialog.findViewById(R.id.pesoRevision);
            altura = (TextView)customDialog.findViewById(R.id.alturaRevision);
            imc = (TextView)customDialog.findViewById(R.id.imcRevision);

        //Inserto los datos para mostrarlos

            fecha.setText(revision.getFecha_revision());
            peso.setText(Double.toString(revision.getPeso_revision()));
            altura.setText(Double.toString(revision.getAltura_revision()));
            imc.setText(Double.toString(Math.round(revision.getImc_revision()*100.0)/100.0));

        //Seteo el layout en el diálogo
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
