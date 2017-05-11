package com.example.giner.gymgo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.giner.gymgo.Objetos.Objetivo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DietasActivity extends AppCompatActivity implements DialogInterface.OnClickListener, View.OnClickListener{


    private Button cambiarDieta;
    private Objetivo objetivoSeleccionado;
    private Objetivo recuperdo;
    int numRecuperados;


    //Firebase firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietas);

        cambiarDieta = (Button)findViewById(R.id.cambiarDieta);
        cambiarDieta.setOnClickListener(this);



    }
    public void cambiarDietaBoton(){

        final ArrayList objetivoSeleccionado = new ArrayList();

        final CharSequence objetivos []= new CharSequence[numRecuperados];


       final DatabaseReference dbObjetivos =
                FirebaseDatabase.getInstance().getReference()
                        .child("Objetivo");

        while(dbObjetivos!=null){
            numRecuperados++;
        }

        dbObjetivos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              String valorObnjetivo=(dataSnapshot.child(String.valueOf(numRecuperados)).getValue().toString());
                objetivos[numRecuperados]=valorObnjetivo;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            
            }
        });






        final AlertDialog.Builder builder= new AlertDialog.Builder(DietasActivity.this);

        builder.setTitle("Selecciona tu objetivo").setMultiChoiceItems(objetivos,null,new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if(isChecked){
                    objetivoSeleccionado.add(which);
                }
                else if(objetivoSeleccionado.contains(which)){
                    objetivoSeleccionado.remove(Integer.valueOf(which));
                }
            }
        });

        builder.setCancelable(false);

        builder.show();

    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cambiarDieta){

            cambiarDietaBoton();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
