package com.example.giner.gymgo.Gymgo.Activities;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.giner.gymgo.Gymgo.Dialogos.MuestraDatos_Dialog;
import com.example.giner.gymgo.Gymgo.Dialogos.MuestraListView_Dialog;
import com.example.giner.gymgo.Objetos.Dieta;
import com.example.giner.gymgo.Objetos.Objetivo;
import com.example.giner.gymgo.Objetos.Rutina;
import com.example.giner.gymgo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class DietasActivity extends AppCompatActivity implements DialogInterface.OnClickListener, View.OnClickListener, MuestraListView_Dialog.DialogMuestrasListener, MuestraDatos_Dialog.OnListener{


    private Button cambiarDieta;

    private FragmentTransaction transaction;
    private int objetivoSeleccionado;
    private int objetivoSelecc;
    private CharSequence[] objetivos;
    private ArrayList<Objetivo> objetivosArray = new ArrayList<>();
    private MuestraListView_Dialog muestraDietas;
    private ArrayList<Dieta> dietasArray = new ArrayList<>();
    private ArrayList<Dieta> dietasFiltradas = new ArrayList<>();
    private MuestraDatos_Dialog dialogoMuestraDietas;
    private ArrayList<String>platosDieta;

    int numRecuperados;


    //Firebase firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietas);

        //Recupero los objetivos de la bd

        DatabaseReference dbObjetivos = FirebaseDatabase.getInstance().getReference().child("Objetivo");

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        ValueEventListener eventListener;

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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toasty.error(DietasActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();

            }

        };

        dbObjetivos.addValueEventListener(eventListener);

        //Intancio los widgets

        cambiarDieta = (Button)findViewById(R.id.cambiarDieta);


        //Escuchadores

        cambiarDieta.setOnClickListener(this);


    }


    public void muestraDialogObjetivo(){

        ///Creo el dialogo

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DietasActivity.this);
        builder.setTitle("Selecciona el objetivo").setSingleChoiceItems(objetivos, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                objetivoSelecc = which;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                objetivoSeleccionado = objetivoSelecc+1;

                muestraDialogoDietas();
                Toasty.success(DietasActivity.this,Integer.toString(objetivoSeleccionado), Toast.LENGTH_SHORT).show();

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



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cambiarDieta){
            muestraDialogObjetivo();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    public void muestraDialogoDietas(){

        DatabaseReference dbDieta = FirebaseDatabase.getInstance().getReference().child("Dieta");

        ValueEventListener eventListenerRutinas;

        eventListenerRutinas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int numDieta;

                GenericTypeIndicator<ArrayList<Dieta>> t = new GenericTypeIndicator<ArrayList<Dieta>>() {};

                dietasArray= dataSnapshot.getValue(t);

                //Filtramos los resultados

                dietasFiltradas.clear();

                for(int i=0;i<dietasArray.size();i++){
                    if(dietasArray.get(i).getObjetivo()==objetivoSeleccionado){
                        dietasFiltradas.add(dietasArray.get(i));
                    }
                }

                llamaDialogo();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toasty.error(DietasActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();

            }
        };

        dbDieta.addValueEventListener(eventListenerRutinas);

    }

    public void llamaDialogo(){
        //Llamo al dialogo
        transaction = getFragmentManager().beginTransaction();
        muestraDietas=new MuestraListView_Dialog(null,dietasFiltradas);
        muestraDietas.setDialogMuestrasListener(this);
        muestraDietas.show(transaction,null);

    }

    @Override
    public void onMuestraDieta(Dieta dieta) {

        transaction = getFragmentManager().beginTransaction();
        dialogoMuestraDietas= new MuestraDatos_Dialog(null,null,platosDieta,dieta);
        dialogoMuestraDietas.setListener(this);
        dialogoMuestraDietas.show(transaction,null);
        dialogoMuestraDietas.setCancelable(false);
    }

    @Override
    public void onMuestraRutina(Rutina rutina) {

    }

    @Override
    public void onObjetoSeleccionado(Rutina rutinaSeleccionada, Dieta dietaSeleccionada) {

    }

    @Override
    public void onCancelled() {

    }
}