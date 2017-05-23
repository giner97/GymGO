package com.example.giner.gymgo.Gymgo.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.giner.gymgo.Gymgo.Activities.RutinasActivity;
import com.example.giner.gymgo.R;

import es.dmoral.toasty.Toasty;

public class numDias_Dialog extends DialogFragment {

    //Dialogs

        private AlertDialog dialog;
        private EditText numDiasEditText;

    //Variable para almacenar el numero de dias

        private int numDias;
        private boolean userSinRutina;

    //Escuchador del dialogo

        private OnNumDialog escuchador;

    //Constructor

        public numDias_Dialog(boolean userSinRutina){
            this.userSinRutina=userSinRutina;
        }

    //OnCreateDialog

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Construimos el dialogo

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Introduce el número de días a la semana");
            View customDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_nums,null);

        //Instancio los objetos

            numDiasEditText = (EditText)customDialog.findViewById(R.id.nums);

        //Seteo el view

            builder.setView(customDialog);

        //Botones

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(!numDiasEditText.getText().toString().isEmpty()) {

                    numDias = Integer.valueOf(numDiasEditText.getText().toString());
                    if (numDias <=7 && numDias > 0) {
                        escuchador.pasaNum(numDias);
                    } else {
                        if(userSinRutina==true){
                            escuchador.finalizaActivity();
                            dismiss();
                        }
                        Toasty.error(getActivity(), "El número introducido es incorrecto", Toast.LENGTH_SHORT).show();
                    }

                }

                else{
                    if(userSinRutina==true){
                        escuchador.finalizaActivity();
                        dismiss();
                    }
                    Toasty.error(getActivity(), "Te has dejado el campo vacío", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(userSinRutina==true){
                    escuchador.finalizaActivity();
                    dismiss();
                }

                else{
                    dialogInterface.dismiss();
                }

            }
        });


        //Dialogo modal

            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);

        return dialog;

    }

    //Interfaz del dialogo

        public interface OnNumDialog{
            void pasaNum(int numDias);
            void finalizaActivity();
        }

        public void setNumDialogListener(OnNumDialog escuchador){
            this.escuchador=escuchador;
        }

}
