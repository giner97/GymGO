package com.example.giner.gymgo.Gymgo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.giner.gymgo.R;

public class CambiarEmailDialog extends DialogFragment implements View.OnClickListener {

    //Widgets

        private EditText nuevoMail;
        private ImageButton botonVolver;
        private ImageButton botonAutentificar;

    //Dialogo

        private AlertDialog dialog;
        private OnCambiarEmail escuchador;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Introduce el nuevo email");
        View dialogoCambioEmail = getActivity().getLayoutInflater().inflate(R.layout.dialog_cambiaremail, null);

        //Instancio los objetos

            nuevoMail = (EditText)dialogoCambioEmail.findViewById(R.id.cambioEmail);
            botonAutentificar = (ImageButton)dialogoCambioEmail.findViewById(R.id.autentificar);
            botonVolver = (ImageButton)dialogoCambioEmail.findViewById(R.id.volver);

        //Escuchadores

            botonAutentificar.setOnClickListener(this);
            botonVolver.setOnClickListener(this);

        //Le pasamos la vista al dialogo

            builder.setView(dialogoCambioEmail);

        //Creamos el dialogo

            dialog = builder.create();

        //Dialogo modal

            dialog.setCanceledOnTouchOutside(false);

        //Metodo para reiniciar los widgets

            reiniciarWidgets();

        //Return del dialogo

            return dialog;

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==botonAutentificar.getId()){
            if(nuevoMail.getText().toString().isEmpty()||!nuevoMail.getText().toString().contains("@")||!nuevoMail.getText().toString().contains(".")){
                //Condicionales del campo email
                if(nuevoMail.getText().toString().isEmpty()){
                    nuevoMail.setError("El campo del email esta vacio");
                }
                else if(!nuevoMail.getText().toString().contains("@")||!nuevoMail.getText().toString().contains(".")){
                    nuevoMail.setError("El email introducido no es válido");
                }
                else{
                    nuevoMail.setError(null);
                }
            }
            else {
                escuchador.cambiarEmail(nuevoMail.getText().toString());
                dismiss();
            }
        }
        else if (v.getId()==botonVolver.getId()){
            dismiss();
        }
    }

    //Metodo para reiniciar los widgets

    public void reiniciarWidgets(){
        nuevoMail.setText("");
    }

    public interface  OnCambiarEmail{
        //Metodo para pasar el nuevo email a la ctividad
            void cambiarEmail(String email);
    }

    public void setOnCambiarEmailListener(OnCambiarEmail escuchador){
        this.escuchador=escuchador;
    }

}
