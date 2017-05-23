package com.example.giner.gymgo.Gymgo.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.giner.gymgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables

        private static final String TAG = "LoginActivity";
        private FirebaseAuth autentificacion;
        private FirebaseAuth.AuthStateListener autentificadorListener;
        private EditText emailRegistro;
        private EditText passRegistro;
        private EditText repetirPass;
        private Button botonRegistro;
        private Button botonVolver;
        private View mProgressView;
        private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        autentificacion = FirebaseAuth.getInstance();

        //Configuramos que responda a los cambios en el estado de inicio de sesion.

            autentificadorListener = new FirebaseAuth.AuthStateListener() {

                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    FirebaseUser usuario = firebaseAuth.getCurrentUser();

                    if(usuario!=null){
                        //El usuario ha iniciado sesion
                        //Log para informar del inicio de sesion
                        Log.d(TAG, "El metodo onAuthStateChanged ha sido llamado, resultado: signed in. UID: "+usuario.getUid());
                    }
                    else{
                        //El usuario ha cerrado sesion
                        //Log para informar del cierre de sesion
                        Log.d(TAG, "El metodo onAuthStateChanged ha sido llamado, resultado: signer out. UID: ");
                    }

                }
            };

        // Instancio los widgets

            emailRegistro = (EditText)findViewById(R.id.emailRegistro);
            passRegistro = (EditText)findViewById(R.id.passwordRegistro);
            repetirPass = (EditText)findViewById(R.id.confirmarRegistro);
            botonRegistro = (Button)findViewById(R.id.buttonRegistrar);
            botonVolver = (Button)findViewById(R.id.buttonVolver);
            botonRegistro.setOnClickListener(this);
            botonVolver.setOnClickListener(this);
            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);

    }

    @Override
    protected void onStart() {
        super.onStart();
        autentificacion.addAuthStateListener(autentificadorListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(autentificadorListener!=null){
            autentificacion.removeAuthStateListener(autentificadorListener);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==botonRegistro.getId()){
            if(emailRegistro.getText().toString().isEmpty()||passRegistro.getText().toString().isEmpty()||passRegistro.getText().toString().length()<6||
                    !emailRegistro.getText().toString().contains("@")||repetirPass.getText().toString().isEmpty()||repetirPass.getText().toString().length()<6||
                    !passRegistro.getText().toString().equals(repetirPass.getText().toString())||!emailRegistro.getText().toString().contains(".")){

                //Condicionales del campo pass
                if(passRegistro.getText().toString().isEmpty()) {
                    passRegistro.setError("El campo de la contraseña esta vacío");
                }
                else if(passRegistro.getText().toString().length()<6){
                    passRegistro.setError("La contraseña debe tener 6 carácteres como mínimo");
                }
                else{
                    passRegistro.setError(null);
                }
                //Condicionales del campo email
                if(emailRegistro.getText().toString().isEmpty()){
                    emailRegistro.setError("El campo del email esta vacío");
                }
                else if(!emailRegistro.getText().toString().contains("@")||!emailRegistro.getText().toString().contains(".")){
                    emailRegistro.setError("El email introducido no es válido");
                }
                else{
                    emailRegistro.setError(null);
                }
                //Condicionales del campo repetir pass
                if(!passRegistro.getText().toString().equals(repetirPass.getText().toString())){
                    repetirPass.setError("Las contraseñas no coinciden");
                    passRegistro.setError("Las contraseñas no coinciden");
                }
                else if(repetirPass.getText().toString().isEmpty()){
                    repetirPass.setError("El campo de repetir la contraseña esta vacío");
                }

            }
            else{
                showProgress(true);
                autentificacion.createUserWithEmailAndPassword(emailRegistro.getText().toString(),passRegistro.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showProgress(false);
                        if(task.isSuccessful()){
                            Log.d(TAG, "Usuario registrado");
                            Toasty.success(RegistroActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Log.d(TAG, "Fallo en el registro");
                            try{
                                throw task.getException();
                            }

                            catch(FirebaseAuthUserCollisionException e) {
                                Toasty.error(RegistroActivity.this,"El email introducido ya esta en uso", Toast.LENGTH_SHORT).show();
                            }

                            catch (Exception e){
                                Toasty.error(RegistroActivity.this,"Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        }
        else if (v.getId()==botonVolver.getId()){
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            botonRegistro.setVisibility(show ? View.GONE : View.VISIBLE);
            botonVolver.setVisibility(show ? View.GONE : View.VISIBLE);
        }

        else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            botonRegistro.setVisibility(show ? View.GONE : View.VISIBLE);
            botonVolver.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void onBackPressed(){
    }

}

