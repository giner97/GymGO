package com.example.giner.gymgo.Gymgo.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giner.gymgo.Objetos.Objetivo;
import com.example.giner.gymgo.Objetos.Usuario;
import com.example.giner.gymgo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ModificarDatosUsuarioActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText lblaltura,lblapellidos,lblnombre,lblpeso;
    private Button aceptarCambios;
    private FirebaseUser userLogueado;
    private String idUserLogueado;
    private Button descartarCambios;
    private Button seleccionarObjetivo;

    //Variables del objeto User

        private String nombre="";
        private String apellidos="";
        private double peso=0;
        private double altura=0;
        private boolean finish=false;
        private int objetivoSelecc;
        private int objetivoSeleccionado;
        private CharSequence[] objetivos;
        private ArrayList<Objetivo> objetivosArray = new ArrayList<>();
        private DatabaseReference dbUsuarios;
        private ValueEventListener eventListener2;
        private DatabaseReference dbObjetivos;
        private ValueEventListener eventListener;
        private ArrayList<Objetivo> objetivo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_datos_usuario);

        /*SE INSTANCIA LOS WIDGETS*/

        aceptarCambios=(Button)findViewById(R.id.aceptarCambios);
        descartarCambios=(Button)findViewById(R.id.descartarCambios);
        seleccionarObjetivo = (Button)findViewById(R.id.buttonObjetivo);
        lblaltura=(EditText)findViewById(R.id.lblaltura);
        lblapellidos=(EditText)findViewById(R.id.lblapellidos);
        lblnombre=(EditText)findViewById(R.id.lblnombre);
        lblpeso=(EditText)findViewById(R.id.lblpeso);

        /**/

        userLogueado = FirebaseAuth.getInstance().getCurrentUser();
            idUserLogueado=userLogueado.getUid();

        dbUsuarios = FirebaseDatabase.getInstance().getReference();

        eventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if((dataSnapshot.child("User").child(idUserLogueado).child("apellidos").getValue()!=null)||(dataSnapshot.child("User").child(idUserLogueado).child("nombre").getValue()!=null)) {

                    if(finish==false) {

                        nombre = dataSnapshot.child("User").child(idUserLogueado).child("nombre").getValue().toString();
                        altura = Double.parseDouble(dataSnapshot.child("User").child(idUserLogueado).child("altura").getValue().toString());
                        peso = Double.parseDouble(dataSnapshot.child("User").child(idUserLogueado).child("peso").getValue().toString());
                        apellidos = dataSnapshot.child("User").child(idUserLogueado).child("apellidos").getValue().toString();

                        cargaDatos();

                    }

                }

                else if((dataSnapshot.child("User").child(idUserLogueado).child("peso").getValue(Double.class)>0)||(dataSnapshot.child("User").child(idUserLogueado).child("altura").getValue(Double.class)>0)){

                    if(finish==false){
                        altura = Double.parseDouble(dataSnapshot.child("User").child(idUserLogueado).child("altura").getValue().toString());
                        peso = Double.parseDouble(dataSnapshot.child("User").child(idUserLogueado).child("peso").getValue().toString());
                        lblaltura.setText(Double.toString(altura));
                        lblpeso.setText(Double.toString(peso));
                    }
                }

                objetivoSeleccionado = Integer.valueOf(dataSnapshot.child("User").child(idUserLogueado).child("objetivo").getValue().toString());

                GenericTypeIndicator<ArrayList<Objetivo>> t = new GenericTypeIndicator<ArrayList<Objetivo>>() {};

                objetivo = dataSnapshot.child("Objetivo").getValue(t);

                if(objetivoSeleccionado!=0){
                    seleccionarObjetivo.setText(objetivo.get(objetivoSeleccionado).getDescripcion());
                }
                else{
                    seleccionarObjetivo.setText("Seleccionar objetivo");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        dbUsuarios.addValueEventListener(eventListener2);

        //Recupero los objetivos de la bd

        dbObjetivos = FirebaseDatabase.getInstance().getReference().child("Objetivo");

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int numObjetivos;

                GenericTypeIndicator<ArrayList<Objetivo>> t = new GenericTypeIndicator<ArrayList<Objetivo>>() {};

                objetivosArray = dataSnapshot.getValue(t);

                numObjetivos=objetivosArray.size();

                //Pasamos los valores del arrayList al array CharSequence

                objetivos = new CharSequence[numObjetivos-1];

                int j=0;

                for(int i=1;i<numObjetivos;i++){
                    objetivos[j] = objetivosArray.get(i).getDescripcion();
                    j++;
                }

                //Listeners de los botones

                aceptarCambios.setOnClickListener(ModificarDatosUsuarioActivity.this);
                descartarCambios.setOnClickListener(ModificarDatosUsuarioActivity.this);
                seleccionarObjetivo.setOnClickListener(ModificarDatosUsuarioActivity.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        dbObjetivos.addValueEventListener(eventListener);

    }

    public void cargaDatos(){

        lblapellidos.setText(apellidos);
        lblnombre.setText(nombre);
        lblaltura.setText(Double.toString(altura));
        lblpeso.setText(Double.toString(peso));

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.aceptarCambios){

            //Protegemos la aplcacion de que introduce bien los datos y que no deje ningun campo vacio

                nombre=lblnombre.getText().toString();
                apellidos=lblapellidos.getText().toString();

            if((!nombre.isEmpty())&&(!apellidos.isEmpty())&&(!lblaltura.getText().toString().isEmpty())&&(!lblpeso.getText().toString().isEmpty())&&(objetivoSeleccionado!=0)){

                if((Double.parseDouble(lblpeso.getText().toString())>0)&&(Double.parseDouble(lblaltura.getText().toString())>0)) {

                    if(Double.parseDouble(lblaltura.getText().toString())<3) {

                        peso = Double.parseDouble(lblpeso.getText().toString());
                        altura = Double.parseDouble(lblaltura.getText().toString());

                        //Actualizamos los datos en la bd

                        DatabaseReference dbUpdateRutina = FirebaseDatabase.getInstance().getReference().child("User").child(userLogueado.getUid());
                        dbUpdateRutina.child("altura").setValue(altura);
                        dbUpdateRutina.child("peso").setValue(peso);
                        dbUpdateRutina.child("nombre").setValue(nombre);
                        dbUpdateRutina.child("apellidos").setValue(apellidos);
                        dbUpdateRutina.child("objetivo").setValue(objetivoSeleccionado);

                        finish = true;
                        cerrarConexiones();
                        finish();

                    }

                    else{
                        Toasty.warning(this, "La altura máxima son 3 metros", Toast.LENGTH_SHORT).show();
                    }

                }

                else{
                    Toasty.warning(this, "El peso y la altura deben ser mayores que 0", Toast.LENGTH_SHORT).show();
                }

            }

            else{

                Toasty.warning(this, "Te has dejado algún campo vacío", Toast.LENGTH_SHORT).show();

            }

        }

        else if(v.getId()==R.id.descartarCambios){
            cerrarConexiones();
            finish();
        }

        else if(v.getId()==R.id.buttonObjetivo){
            muestraDialogObjetivo();
        }

    }

    public void muestraDialogObjetivo(){

        objetivoSelecc=0;

        ///Creo el dialogo

        AlertDialog.Builder builder = new AlertDialog.Builder(ModificarDatosUsuarioActivity.this);
        builder.setTitle("Selecciona el objetivo").setSingleChoiceItems(objetivos, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                objetivoSelecc = which;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                objetivoSeleccionado=0;
                objetivoSeleccionado = objetivoSelecc+1;
                if(objetivoSeleccionado!=0){
                    seleccionarObjetivo.setText(objetivo.get(objetivoSeleccionado).getDescripcion());
                }
                else{
                    seleccionarObjetivo.setText("Seleccionar objetivo");
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        //Dialogo modal

            builder.setCancelable(false);

            builder.show();

    }

    public void cerrarConexiones(){
        dbUsuarios.removeEventListener(eventListener2);
        dbObjetivos.removeEventListener(eventListener);
    }

}
