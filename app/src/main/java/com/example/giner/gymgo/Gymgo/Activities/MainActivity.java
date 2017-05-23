package com.example.giner.gymgo.Gymgo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentTransaction;

import com.example.giner.gymgo.Gymgo.Dialogos.AutentificarDialog;
import com.example.giner.gymgo.Gymgo.Dialogos.CambiarContrasenyaDialog;
import com.example.giner.gymgo.Gymgo.Dialogos.CambiarEmailDialog;
import com.example.giner.gymgo.Objetos.Usuario;
import com.example.giner.gymgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AutentificarDialog.OnAutentificar, CambiarContrasenyaDialog.OnCambiarContrasenya, CambiarEmailDialog.OnCambiarEmail, View.OnClickListener{

    //Constantes

        private static int claveEliminarUsuario=0;
        private static int claveCambiarContrasenya=1;
        private static int claveCambiarEmail=2;
        public final static String KEY_UID="uid";

    //Widgets

        private TextView user;
        private static final String TAG = "MainActivity";
        private FirebaseUser userLogueado;
        private FirebaseAuth autentificacion;
        private FirebaseAuth.AuthStateListener autentificadorListener;

    //Dialogo Autentificar

        private FragmentTransaction transactionAutentificar;
        private AutentificarDialog autentificar_dialog;

    //Dialogo CambiarCiontraseña

        private FragmentTransaction transactionCambiarContrasenya;
        private CambiarContrasenyaDialog cambiarPass_dialog;

    //Dialogo CambiarEmail

        private FragmentTransaction transaccionCambiarEmail;
        private CambiarEmailDialog cambiarEmail_dialog;

    //Creo los widgets

        private ImageButton botonRutina;
        private ImageButton botonDieta;
        private ImageButton botonRevision;
        private ImageButton botonPGC;

    //Objetos

        private Usuario userDatabase;
        private DatabaseReference dbUser;
        private ValueEventListener eventListenerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autentificacion = FirebaseAuth.getInstance();
        userLogueado = FirebaseAuth.getInstance().getCurrentUser();

        //Recupero el usuario logueado

            autentificadorListener = new FirebaseAuth.AuthStateListener(){
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    userLogueado = firebaseAuth.getCurrentUser();
                    if(userLogueado==null){
                        Log.d(TAG,"El user es nulo");
                    }
                }
            };

        //Recupero el usuario de la bd

        dbUser = FirebaseDatabase.getInstance().getReference().child("User").child(userLogueado.getUid());

        eventListenerUser = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Si el usuario no existe en la bd (Se acaba de registrar), creo un nuevo usuario con su uid

                if(!dataSnapshot.exists()){

                    DatabaseReference dbCreateUser = FirebaseDatabase.getInstance().getReference().child("User");
                    Map<String, Usuario>crearUsuario = new HashMap<>();
                    Usuario objetoCrearUsuario = new Usuario();
                    objetoCrearUsuario.setEmail(userLogueado.getEmail());
                    objetoCrearUsuario.setUid(userLogueado.getUid());
                    crearUsuario.put(userLogueado.getUid(),objetoCrearUsuario);

                    dbCreateUser.child(userLogueado.getUid()).setValue(objetoCrearUsuario);

                }

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(MainActivity.this);

                //Instancio los botones

                botonRutina = (ImageButton) findViewById(R.id.buttonRutinas);
                botonDieta = (ImageButton)findViewById(R.id.buttonDietas);
                botonRevision = (ImageButton)findViewById(R.id.buttonRevision);
                botonPGC = (ImageButton)findViewById(R.id.buttonPGC);

                //Escuchadores de los botones

                botonRutina.setOnClickListener(MainActivity.this);
                botonDieta.setOnClickListener(MainActivity.this);
                botonRevision.setOnClickListener(MainActivity.this);
                botonPGC.setOnClickListener(MainActivity.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbUser.addValueEventListener(eventListenerUser);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        //Intancio los widgets

            user = (TextView)findViewById(R.id.nombreUser);

        //Seteo los datos del usuario

            if(userLogueado.getDisplayName()!=null){
                user.setText(userLogueado.getUid().toString());
            }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.datos) {
            Intent intencion;
            intencion = new Intent(this, ModificarDatosUsuarioActivity.class);
            startActivity(intencion);
        }
        else if (id == R.id.cambiarCorreo) {
            creaDialogoReAutentificacion(claveCambiarEmail);
        }
        else if (id == R.id.cambiarPass) {
            creaDialogoReAutentificacion(claveCambiarContrasenya);
        }
        else if (id == R.id.eliminarUser) {
            creaDialogoReAutentificacion(claveEliminarUsuario);
        }
        else if (id == R.id.cerrarSesion) {
            cerrarSesion();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    //Metodos para crear los dialogos

    public void creaDialogoReAutentificacion(int claveAccion){
        //Creo e instancio el dialogo para autentificar al usuario
            transactionAutentificar = getFragmentManager().beginTransaction();
            autentificar_dialog = new AutentificarDialog(claveAccion);
            autentificar_dialog.setOnAutentificarListener(this);
            autentificar_dialog.show(transactionAutentificar,null);
            autentificar_dialog.setCancelable(false);
    }

    public void creaDialogoCambiarPass(){
        //Creo e instancio el dialogo para que introduzca la nueva contraseña
            transactionCambiarContrasenya = getFragmentManager().beginTransaction();
            cambiarPass_dialog = new CambiarContrasenyaDialog();
            cambiarPass_dialog.setOnCambiarContrasenyaListener(this);
            cambiarPass_dialog.show(transactionCambiarContrasenya,null);
            cambiarPass_dialog.setCancelable(false);
    }

    public void creaDialogoCambiaEmail(){
        //Creo e instancio el dialogo para que introduzca el nuevo email
            transaccionCambiarEmail = getFragmentManager().beginTransaction();
            cambiarEmail_dialog = new CambiarEmailDialog();
            cambiarEmail_dialog.setOnCambiarEmailListener(this);
            cambiarEmail_dialog.show(transaccionCambiarEmail,null);
            cambiarEmail_dialog.setCancelable(false);
    }

    public void cerrarSesion(){
        cerrarConexiones();
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    @Override
    public void autentificarUsuario(String email, String pass, final int claveAccion) {

        userLogueado = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential autentificacion = EmailAuthProvider.getCredential(email,pass);
        userLogueado.reauthenticate(autentificacion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Reautentificación realizada");

                    if(claveAccion==claveEliminarUsuario){
                        eliminarUsuario();
                    }
                    else if(claveAccion==claveCambiarContrasenya){
                         creaDialogoCambiarPass();
                    }
                    else if(claveAccion==claveCambiarEmail){
                        creaDialogoCambiaEmail();
                    }
                }
                else{
                    Log.d(TAG, "Fallo en la reautentificación");
                    Toasty.error(MainActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void cambiarPassword(String pass) {
        userLogueado = FirebaseAuth.getInstance().getCurrentUser();
        userLogueado.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "La contraseña ha sido cambiada correctamente");
                    Toasty.success(MainActivity.this, "La contraseña ha sido cambiada", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d(TAG, "No se ha podido cambiar la contraseña");
                   Toasty.error(MainActivity.this,task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void cambiarEmail(final String email) {
        userLogueado = FirebaseAuth.getInstance().getCurrentUser();
        userLogueado.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "El email ha sido cambiado correctamente");
                    Toasty.success(MainActivity.this, "El email ha sido cambiado", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d(TAG, "No se ha podido cambiar el email");
                    Toasty.error(MainActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void eliminarUsuario(){

        //Metodo para eliminar el usuario

        userLogueado = FirebaseAuth.getInstance().getCurrentUser();

        userLogueado.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"Usuario eliminado.");
                    Toasty.success(MainActivity.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d(TAG,"Fallo en la eliminación del usuario");
                    Toasty.error(MainActivity.this,task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cerrarConexiones();
        finish();
    }

    @Override
    public void onClick(View v) {

        Intent intencion;

        if(v.getId()==botonRutina.getId()){
            intencion = new Intent(this, RutinasActivity.class);
            intencion.putExtra(KEY_UID, userLogueado.getUid());
            startActivity(intencion);
        }

        else if(v.getId()==botonDieta.getId()){
            intencion = new Intent(this, DietasActivity.class);
            intencion.putExtra(KEY_UID, userLogueado.getUid());
            startActivity(intencion);
        }

        else if(v.getId()==botonRevision.getId()){
            intencion = new Intent(this, RevisionesActivity.class);
            intencion.putExtra(KEY_UID, userLogueado.getUid());
            startActivity(intencion);
        }

        else if(v.getId()==botonPGC.getId()){
            intencion = new Intent(this, PGCActivity.class);
            startActivity(intencion);
        }
    }

    public void cerrarConexiones(){
        dbUser.removeEventListener(eventListenerUser);
    }

}
