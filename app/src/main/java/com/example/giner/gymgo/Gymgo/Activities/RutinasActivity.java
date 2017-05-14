package com.example.giner.gymgo.Gymgo.Activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.giner.gymgo.Gymgo.Dialogos.MuestraDatos_Dialog;
import com.example.giner.gymgo.Gymgo.Dialogos.MuestraListView_Dialog;
import com.example.giner.gymgo.Gymgo.Dialogos.numDias_Dialog;
import com.example.giner.gymgo.Objetos.Dieta;
import com.example.giner.gymgo.Objetos.Objetivo;
import com.example.giner.gymgo.Objetos.Rutina;
import com.example.giner.gymgo.Objetos.Rutina_User;
import com.example.giner.gymgo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class RutinasActivity extends AppCompatActivity implements View.OnClickListener, numDias_Dialog.OnNumDialog, MuestraListView_Dialog.DialogMuestrasListener, MuestraDatos_Dialog.OnListener{

    //Widgets

        private Button cambiarRutina;
        private Button crearRutina;

    //Variables

        private int numDias;
        private String uidUser;

    //Variables

        private FragmentTransaction transaction;
        private numDias_Dialog dialogoNumDias;
        private int objetivoSeleccionado;
        private int objetivoSelecc;
        private CharSequence[] objetivos;
        private ArrayList<Objetivo> objetivosArray = new ArrayList<>();
        private ArrayList<Rutina> rutinasArray = new ArrayList<>();
        private ArrayList<Rutina> rutinasFiltradas= new ArrayList<>();
        private ArrayAdapter<Rutina> arrayAdapterRutinas;
        private MuestraListView_Dialog muestraRutinas;
        private Rutina rutinaCambio;
        private MuestraDatos_Dialog dialogoMuestraRutina;
        private ArrayList diasSemana = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutinas);

        //Recupero el uid del usuario loggeado

            Intent recibeUID=getIntent();
            uidUser=recibeUID.getStringExtra(MainActivity.KEY_UID);

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

                Toasty.error(RutinasActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();

            }

        };

        dbObjetivos.addValueEventListener(eventListener);

        //Intancio los widgets

            cambiarRutina = (Button)findViewById(R.id.cambiarRutina);
            crearRutina = (Button)findViewById(R.id.crearRutina);

        //Escuchadores

            cambiarRutina.setOnClickListener(this);
            crearRutina.setOnClickListener(this);


    }

    public void muestraDialogNumDias(){
        transaction = getFragmentManager().beginTransaction();
        dialogoNumDias = new numDias_Dialog();
        dialogoNumDias.show(transaction,null);
        dialogoNumDias.setCancelable(false);
        dialogoNumDias.setNumDialogListener(this);
    }

    public AlertDialog seleccionDias(final int numDias){

        diasSemana.clear();

        //Dialogo para la seleccion de los dias
            final AlertDialog.Builder builder = new AlertDialog.Builder(RutinasActivity.this);

        final CharSequence[] items = new CharSequence[7];

        items[0] = "Lunes";
        items[1] = "Martes";
        items[2] = "Miercoles";
        items[3] = "Jueves";
        items[4] = "Viernes";
        items[5] = "Sabado";
        items[6] = "Domingo";

        builder.setTitle("Selecciona los dias").setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if(isChecked){
                    diasSemana.add(which);
                }
                else if(diasSemana.contains(which)){
                    diasSemana.remove(Integer.valueOf(which));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(diasSemana.size()>numDias){
                    Toasty.error(RutinasActivity.this,"Has introducido mas dias de los que vas a ir al gimnasio", Toast.LENGTH_SHORT).show();
                }
                else if(diasSemana.size()<numDias){
                    Toasty.error(RutinasActivity.this,"Has introducido menos dias de los que vas a ir al gimnasio", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Llama al dialogo que pide el objetivo
                    muestraDialogObjetivo();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return builder.create();

    }

    public void muestraDialogObjetivo(){

       ///Creo el dialogo

        AlertDialog.Builder builder = new AlertDialog.Builder(RutinasActivity.this);
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
                muestraDialgoRutinas();
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
    public void onClick(View view) {

        if(view.getId()==cambiarRutina.getId()){
            muestraDialogNumDias();
        }
        else if(view.getId()==crearRutina.getId()){

        }

    }

    @Override
    public void pasaNum(int numDias) {
        this.numDias=numDias;
        seleccionDias(numDias).show();
    }

    //Hay que pasarle los parametros y usarlos como filtros en la busqueda de rutinas

    public void muestraDialgoRutinas(){

        //Falta filtrar las rutinas

        //Recupero las rutinas de la bd

            DatabaseReference dbRutinas = FirebaseDatabase.getInstance().getReference().child("rutina");

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

            ValueEventListener eventListenerRutinas;

        eventListenerRutinas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int numRutinas;

                GenericTypeIndicator<ArrayList<Rutina>> t = new GenericTypeIndicator<ArrayList<Rutina>>() {};

                rutinasArray = dataSnapshot.getValue(t);

                //Filtramos los resultados

                rutinasFiltradas.clear();

                    for(int i=0;i<rutinasArray.size();i++){
                        if(rutinasArray.get(i).getObjetivo()==objetivoSeleccionado){
                            if(rutinasArray.get(i).getDias()==numDias){
                                rutinasFiltradas.add(rutinasArray.get(i));
                            }
                        }
                    }

                llamaDialogo();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toasty.error(RutinasActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();

            }
        };

        dbRutinas.addValueEventListener(eventListenerRutinas);

    }

    @Override
    public void onMuestraDieta(Dieta dieta) {
        //Este metodo no se ejecuta en esta actividad, por lo que estara vacio
    }

    @Override
    public void onMuestraRutina(Rutina rutina) {
        transaction = getFragmentManager().beginTransaction();
        dialogoMuestraRutina = new MuestraDatos_Dialog(rutina,null);
        dialogoMuestraRutina.setListener(this);
        dialogoMuestraRutina.show(transaction,null);
        dialogoMuestraRutina.setCancelable(false);
    }

    public void llamaDialogo(){
        //Llamo al dialogo
        transaction = getFragmentManager().beginTransaction();
        muestraRutinas=new MuestraListView_Dialog(rutinasFiltradas,null);
        muestraRutinas.setDialogMuestrasListener(this);
        muestraRutinas.show(transaction,null);

    }

    @Override
    public void onObjetoSeleccionado(Rutina rutinaSeleccionada, Dieta dietaSeleccionada) {

        rutinaCambio=rutinaSeleccionada;
        Rutina_User insercionRutina = new Rutina_User();
        insercionRutina.setId_rutina(rutinaSeleccionada.getId_rutina());
        insercionRutina.setDias(diasSemana);

        //Llamo al metodo para realizar el cambio.

            DatabaseReference dbUpdateRutina = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);
            dbUpdateRutina.child("rutina").setValue(insercionRutina);

    }
}
