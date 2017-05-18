package com.example.giner.gymgo.Gymgo.Activities;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.giner.gymgo.Gymgo.Dialogos.MuestraDatos_Dialog;
import com.example.giner.gymgo.Gymgo.Dialogos.MuestraListView_Dialog;
import com.example.giner.gymgo.Objetos.Dieta;
import com.example.giner.gymgo.Objetos.Dieta_Plato;
import com.example.giner.gymgo.Objetos.Ejercicio;
import com.example.giner.gymgo.Objetos.Objetivo;
import com.example.giner.gymgo.Objetos.Plato;
import com.example.giner.gymgo.Objetos.Rutina;
import com.example.giner.gymgo.Objetos.Rutina_Ejercicio;
import com.example.giner.gymgo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class DietasActivity extends AppCompatActivity implements DialogInterface.OnClickListener, View.OnClickListener, MuestraListView_Dialog.DialogMuestrasListener, MuestraDatos_Dialog.OnListener{


    private Button cambiarDieta;

    private String uidUser;
    private MaterialCalendarView calendario;

    private int numObjetivoUser, diaDieta;
    private ArrayList<Plato>platos;
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
    private Dieta dietaCambio;
    private Dieta dietaUsuario;
    private Dieta dietaRecuperada;
    private Boolean userSinDieta;
    private ArrayList<Plato> listaPlato = new ArrayList<>();
    private ArrayList<Plato> listaPlatoFiltrado = new ArrayList<>();

    int numRecuperados;
    private int diaDietas;


    //Firebase firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietas);

        userSinDieta=false;

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

            }

        };

        dbObjetivos.addValueEventListener(eventListener);


        //Recupero la rutina de la bd

        DatabaseReference dbRutina = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        ValueEventListener eventListener2;

        eventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                numObjetivoUser=Integer.valueOf(dataSnapshot.child("objetivo").getValue().toString());
                dietaUsuario=dataSnapshot.child("Dieta").getValue(Dieta.class);

                if(dataSnapshot.child("Dieta").getValue()==null){
                    userSinDieta=true;

                }


                calendario = (MaterialCalendarView)findViewById(R.id.calendarView);
                calendario.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                        int diaSemana = recuperaIdDia(date.getDate());
                        muestraDiaDieta(diaSemana);

                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        dbRutina.addValueEventListener(eventListener2);


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
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(userSinDieta==true){
                    finish();
                }
                else{
                    dialogInterface.dismiss();
                }
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

    public void finalizaActivity() {
        finish();
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

            }
        };

        dbDieta.addValueEventListener(eventListenerRutinas);

    }

    public void llamaDialogo(){
        //Llamo al dialogo
        transaction = getFragmentManager().beginTransaction();
        muestraDietas=new MuestraListView_Dialog(null,dietasFiltradas,null,null);
        muestraDietas.setDialogMuestrasListener(this);
        muestraDietas.setCancelable(false);
        muestraDietas.show(transaction,null);

    }

    @Override
    public void onMuestraDieta(Dieta dieta) {
        transaction = getFragmentManager().beginTransaction();
        this.dietaCambio=dieta;
        traducePlatos();
    }

    @Override
    public void onMuestraRutina(Rutina rutina) {

    }

    @Override
    public void onMuestraEjercicio(Ejercicio ejercicio) {

    }

    @Override
    public void onMuestraPlato(Plato platos) {

    }

    @Override
    public void onObjetoSeleccionado(Rutina rutinaSeleccionada, Dieta dietaSeleccionada) {
        dietaCambio=dietaSeleccionada;
        Dieta inserccionDieta= new Dieta();
        inserccionDieta.setId_dieta(dietaSeleccionada.getId_dieta());


        //Llamo al metodo para realizar el cambio.

        DatabaseReference dbUpdateDieta = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);
        dbUpdateDieta.child("dieta").setValue(inserccionDieta);
        dbUpdateDieta.child("objetivo").setValue(dietaSeleccionada);
        userSinDieta=false;

    }

    @Override
    public void onCancelled() {
        if(userSinDieta==true){
            finish();
        }
    }
    public void traducePlatos(){

        DatabaseReference dbPlatos = FirebaseDatabase.getInstance().getReference().child("Plato");

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        ValueEventListener eventListener;

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<Plato>> t = new GenericTypeIndicator<ArrayList<Plato>>() {};

                platos = dataSnapshot.getValue(t);
                platosDieta= new ArrayList<>();

                for(int i=0;i<dietaCambio.getPlato().size();i++){

                    for(int j=1;j<platos.size();j++){

                        if(dietaCambio.getPlato().get(i).getId_plato()==platos.get(j).getId_plato()){
                            platosDieta.add(platos.get(j).getNombre().toString());
                        }

                    }

                }

                dialogoMuestraDietas = new MuestraDatos_Dialog(null,null,platosDieta,dietaCambio);
                dialogoMuestraDietas.setListener(DietasActivity.this);
                dialogoMuestraDietas.show(transaction,null);
                dialogoMuestraDietas.setCancelable(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        dbPlatos.addValueEventListener(eventListener);

    }

    public int recuperaIdDia(Date date){

        int idDia=0;

        //Parseamos la fecha a nombre del dia

        SimpleDateFormat formato2 = new SimpleDateFormat("EEEE");
        String diaNombre = formato2.format(date);

        //Convertimos el nombre del dia en el id

        if((diaNombre.equals("Monday"))||(diaNombre.equals("Lunes"))){
            idDia=0;
        }
        else if((diaNombre.equals("Tuesday"))||(diaNombre.equals("Martes"))){
            idDia=1;
        }
        else if((diaNombre.equals("Wednesday"))||(diaNombre.equals("Miercoles"))){
            idDia=2;
        }
        else if((diaNombre.equals("Thursday"))||(diaNombre.equals("Jueves"))){
            idDia=3;
        }
        else if((diaNombre.equals("Friday"))||(diaNombre.equals("Viernes"))){
            idDia=4;
        }
        else if((diaNombre.equals("Saturday"))||(diaNombre.equals("Sabado"))){
            idDia=5;
        }
        else if((diaNombre.equals("Sunday"))||(diaNombre.equals("Domingo"))){
            idDia=6;
        }

        return idDia;

    }

    //Metodo que llama al dialogo que toca para mostrar las rutinas

    public void muestraDiaDieta(int id_dia){

        boolean vacio=true;

        final ArrayList<Dieta_Plato>dietaPlatos = new ArrayList<>();

        diaDieta=1;
        dietaPlatos.clear();
        ArrayList<Plato>platosDia = new ArrayList<>();

        for(int i=0;i<7;i++){
            for(int j=0;j<dietaRecuperada.getPlato().size();j++){
                if(dietaRecuperada.getPlato().get(j).getDia_semana()==diaDieta){
                    dietaPlatos.add(dietaRecuperada.getPlato().get(j));
                }
            }

            //Recupero y filtro los ejercicios

            platosDia=recuperarPlato(dietaPlatos);

            //Llamo al dialogo y le paso los ejercicios filtrados

            //Llamo a otro dialogo para mostrar los datos del ejercicio seleccionado. Le paso rutinaEjercicio.getEjercicio.get() y el ejercicio seleccionado

            //Bucle para mostrar los valores del array

                    /*for(int q=0;q<rutinaEjercicios.size();q++) {
                        Toasty.info(RutinasActivity.this, Integer.toString(rutinaEjercicios.get(q).getId_ejercicio()), Toast.LENGTH_SHORT).show();
                    }*/







        }

        if(vacio==true){
            Toasty.info(DietasActivity.this,"Este dia no esta asignado en tu rutina",Toast.LENGTH_SHORT).show();
        }

    }

    public ArrayList<Plato>recuperarPlato(final ArrayList<Dieta_Plato> dia_dieta){

        DatabaseReference dbDietas = FirebaseDatabase.getInstance().getReference().child("Dieta");

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        ValueEventListener eventListener4;

        listaPlato.clear();

        eventListener4 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<Plato>> t = new GenericTypeIndicator<ArrayList<Plato>>() {};
                listaPlato = dataSnapshot.getValue(t);

                for(int i=0;i<dia_dieta.size();i++){

                    for(int j=1;j<listaPlato.size();j++){

                        if(dia_dieta.get(i).getId_plato()==listaPlato.get(j).getId_plato()){
                            listaPlatoFiltrado.add(listaPlato.get(j));
                        }

                    }

                }

                for(int q=0;q<listaPlatoFiltrado.size();q++){
                    Toasty.info(DietasActivity.this,listaPlatoFiltrado.get(q).getNombre(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbDietas.addValueEventListener(eventListener4);

        return listaPlatoFiltrado;

    }

}