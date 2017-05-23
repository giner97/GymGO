package com.example.giner.gymgo.Gymgo.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.giner.gymgo.R;

public class CambiarContrasenyaDialog extends DialogFragment implements View.OnClickListener {

    //Widgets

        private EditText nuevaPass;
        private ImageButton botonVolver;
        private ImageButton botonAutentificar;

    //Dialogo

        private AlertDialog dialog;
        private OnCambiarContrasenya escuchador;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Introduce la nueva contraseña");
        View dialogoCambioContrasenya = getActivity().getLayoutInflater().inflate(R.layout.dialog_nuevapass,null);

        //Instancio los objetos

            nuevaPass = (EditText)dialogoCambioContrasenya.findViewById(R.id.newPass);
            botonAutentificar =(ImageButton)dialogoCambioContrasenya.findViewById(R.id.autentificar);
            botonVolver = (ImageButton)dialogoCambioContrasenya.findViewById(R.id.volver);

        //Escuchadores

            botonVolver.setOnClickListener(this);
            botonAutentificar.setOnClickListener(this);

        //Le pasamos la vista al dialogo

            builder.setView(dialogoCambioContrasenya);

        //Creamos el dialogo

            dialog=builder.create();

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
            if(nuevaPass.getText().toString().isEmpty()||nuevaPass.getText().toString().length()<6){
                //Condicionales del campo pass
                if(nuevaPass.getText().toString().isEmpty()) {
                    nuevaPass.setError("El campo de la contraseña esta vacío");
                }
                else if(nuevaPass.getText().toString().length()<6){
                    nuevaPass.setError("La contraseña debe tener 6 carácteres como mínimo");
                }
                else{
                    nuevaPass.setError(null);
                }
            }
            else {
                escuchador.cambiarPassword(nuevaPass.getText().toString());
                dismiss();
            }
        }
        else if (v.getId()==botonVolver.getId()){
            dismiss();
        }
    }

    //Metodo para reiniciar los widgets

        public void reiniciarWidgets(){
            nuevaPass.setText("");
        }

    public interface OnCambiarContrasenya{
        //Metodo para pasar la nueva contraseña a la actividad
            void cambiarPassword(String pass);
    }

    public void setOnCambiarContrasenyaListener(OnCambiarContrasenya escuchador){
        this.escuchador=escuchador;
    }

}
