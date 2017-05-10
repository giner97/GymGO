package com.example.giner.gymgo.Gymgo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.giner.gymgo.R;

public class AutentificarDialog extends DialogFragment implements View.OnClickListener {

    //Widgets

        private EditText email;
        private EditText pass;
        private ImageButton botonVolver;
        private ImageButton botonAutentificar;

    //Dialogo

        private AlertDialog dialogo;
        private OnAutentificar escuchador;

    //Variables

        private int claveAccion;

    //Constructor

        public AutentificarDialog(int claveAccion){
            this.claveAccion=claveAccion;
        }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Reautentificación");
        View dialogAutentificacion = getActivity().getLayoutInflater().inflate(R.layout.dialog_autentificacion,null);

        //Instancio los objetos

            email = (EditText)dialogAutentificacion.findViewById(R.id.emailAutentificacion);
            pass = (EditText)dialogAutentificacion.findViewById(R.id.passwordAutentificacion);

            botonVolver = (ImageButton)dialogAutentificacion.findViewById(R.id.volver);
            botonAutentificar = (ImageButton)dialogAutentificacion.findViewById(R.id.autentificar);

        //Escuchadores

            botonAutentificar.setOnClickListener(this);
            botonVolver.setOnClickListener(this);

        //Seteo el layout en el dialogo

            builder.setView(dialogAutentificacion);

        //Hacemos el dialogo modal

            dialogo=builder.create();
            dialogo.setCanceledOnTouchOutside(false);

        //Metodo para reiniciar los widgets

            reiniciarWidgets();

        //Devolvemos al metodo el alertdialog configurado

            return dialogo;

    }

    //Metodo para reiniciar widgets

        public void reiniciarWidgets(){

            email.setText("");
            pass.setText("");

        }

    @Override
    public void onClick(View v) {

        if(v.getId()==botonAutentificar.getId()){

            if(email.getText().toString().isEmpty()||pass.getText().toString().isEmpty()||pass.getText().toString().length()<6||!email.getText().toString().contains("@")||!email.getText().toString().contains(".")){
                //Condicionales del campo pass
                if(pass.getText().toString().isEmpty()) {
                    pass.setError("El campo de la contraseña esta vacio");
                }
                else if(pass.getText().toString().length()<6){
                    pass.setError("La contraseña debe tener 6 caracteres como mínimo");
                }
                else{
                    pass.setError(null);
                }
                //Condicionales del campo email
                if(email.getText().toString().isEmpty()){
                    email.setError("El campo del email esta vacio");
                }
                else if(!email.getText().toString().contains("@")||!email.getText().toString().contains(".")){
                    email.setError("El email introducido no es válido");
                }
                else{
                    email.setError(null);
                }
            }
            else {
                escuchador.autentificarUsuario(email.getText().toString(), pass.getText().toString(), claveAccion);
                dismiss();
            }

        }

        else if (v.getId()==botonVolver.getId()){
            dismiss();
        }

    }

    public interface OnAutentificar{

        //Metodo para pasar el email y la contraseña a la actividad

            void autentificarUsuario(String email, String pass, int claveAccion);

    }

    public void setOnAutentificarListener(OnAutentificar escuchador){
        this.escuchador=escuchador;
    }

}
