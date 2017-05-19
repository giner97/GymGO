package com.example.giner.gymgo.Gymgo.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.giner.gymgo.R;

import es.dmoral.toasty.Toasty;


public class RevisionDialog extends DialogFragment {

    //Dialogs

        private AlertDialog dialog;
        private EditText altura;
        private EditText peso;

    //Escuchador

        private OnRevision escuchador;

    //Variables

        private boolean userSinRevision;

    //Constructor

        public RevisionDialog(boolean userSinRevision){
            this.userSinRevision=userSinRevision;
        }

    //Creo el dialogo

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            //Contruimos el dialogo

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Introduce los datos para la revision");
                View customDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_imc,null);

            //Instancio los objetos

                altura = (EditText)customDialog.findViewById(R.id.editAltura);
                peso = (EditText)customDialog.findViewById(R.id.editPeso);

            //Seteo el view

                builder.setView(customDialog);

            //Botones

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if((!altura.getText().toString().isEmpty())&&(!peso.getText().toString().isEmpty())){
                            if((Double.parseDouble(peso.getText().toString())>0)&&(Double.parseDouble(altura.getText().toString())>0)) {
                                escuchador.pasaDatos(Double.parseDouble(altura.getText().toString()), Double.parseDouble(peso.getText().toString()), userSinRevision);
                            }
                            else{
                                Toasty.error(getActivity(),"El peso y la altura deben ser mayores que 0", Toast.LENGTH_SHORT).show();
                                if(userSinRevision==true) {
                                    escuchador.finalizaActivity();
                                }

                            }
                        }
                        else{
                            Toasty.error(getActivity(),"Te has dejado algun campo vacio", Toast.LENGTH_SHORT).show();
                            if(userSinRevision==true) {
                                escuchador.finalizaActivity();
                            }
                        }
                    }
                });

                builder.setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(userSinRevision==true){
                            escuchador.finalizaActivity();
                        }
                        else{
                            dismiss();
                        }
                    }
                });

            dialog = builder.create();
            dialog.setCancelable(false);

            return dialog;
        }

        public interface OnRevision{
            void finalizaActivity();
            void pasaDatos(double altura, double peso, boolean userSinRevision);
        }

        public void setRevisionListener(OnRevision escuchador){
            this.escuchador=escuchador;
        }


}
