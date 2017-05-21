package com.example.giner.gymgo.Gymgo.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giner.gymgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    //Variables

        private static final String TAG = "LoginActivity";
        private FirebaseAuth autentificacion;
        private FirebaseAuth.AuthStateListener autentificadorListener;

    // UI references.
    private EditText email;
    private EditText pass;
    private Button botonLogin;
    private TextView registrarse;
    private TextView olvidoPass;
    private View mProgressView;
    private View mLoginFormView;
    private ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

            logo = (ImageView) findViewById(R.id.logoIamge);
            email = (EditText)findViewById(R.id.email);
            pass = (EditText)findViewById(R.id.password);
            botonLogin = (Button)findViewById(R.id.email_sign_in_button);
            botonLogin.setOnClickListener(this);
            registrarse = (TextView)findViewById(R.id.registrase);
            olvidoPass = (TextView)findViewById(R.id.recuperarPass);
            registrarse.setOnClickListener(this);
            olvidoPass.setOnClickListener(this);
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

        if (v.getId() == botonLogin.getId()) {

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

                showProgress(true);
                    autentificacion.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            showProgress(false);
                            //Al completarse muestra un log para informar de que ha sido logueado correctamente, si falla el login mostrar'a un log y toast al usuario
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Login correcto.");
                                OnLoginCorrecto();
                            } else {
                                Log.d(TAG, "Login fallido");

                                try{
                                    throw task.getException();
                                }

                                catch(FirebaseAuthInvalidCredentialsException e) {
                                    Toasty.error(LoginActivity.this,"El email o la contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                                }

                                catch(FirebaseAuthInvalidUserException e) {
                                    Toasty.error(LoginActivity.this,"El email no existe", Toast.LENGTH_SHORT).show();
                                }

                                catch (Exception e){
                                    Toasty.error(LoginActivity.this,"Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                                }

                                onRestart();
                            }
                        }
                    });
            }
        }

        else if(v.getId() == registrarse.getId()){
            Intent intencionRegistrar = new Intent(this,RegistroActivity.class);
            startActivity(intencionRegistrar);
        }

        else if(v.getId() == olvidoPass.getId()){
            Intent intencionRecuperarPass = new Intent(this,RecuperarPassActivity.class);
            startActivity(intencionRecuperarPass);
        }
    }

    public void OnLoginCorrecto(){
        Intent intencionInicioSesion = new Intent(this,MainActivity.class);
        startActivity(intencionInicioSesion);
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
            botonLogin.setVisibility(show ? View.GONE : View.VISIBLE);
            registrarse.setVisibility(show ? View.GONE : View.VISIBLE);
            olvidoPass.setVisibility(show ? View.GONE : View.VISIBLE);

        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            botonLogin.setVisibility(show ? View.GONE : View.VISIBLE);
            registrarse.setVisibility(show ? View.GONE : View.VISIBLE);
            olvidoPass.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //email.setText("");
        pass.setText("");
    }

}

