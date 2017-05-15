package com.example.giner.gymgo.Gymgo.Activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.giner.gymgo.Gymgo.Dialogos.MuestraDatos_Dialog;
import com.example.giner.gymgo.Gymgo.Dialogos.MuestraListView_Dialog;
import com.example.giner.gymgo.Gymgo.Dialogos.numDias_Dialog;
import com.example.giner.gymgo.Objetos.Dieta;
import com.example.giner.gymgo.Objetos.Ejercicio;
import com.example.giner.gymgo.Objetos.Objetivo;
import com.example.giner.gymgo.Objetos.Rutina;
import com.example.giner.gymgo.Objetos.Rutina_Ejercicio;
import com.example.giner.gymgo.Objetos.Rutina_User;
import com.example.giner.gymgo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class RutinasActivity extends AppCompatActivity implements View.OnClickListener, numDias_Dialog.OnNumDialog, MuestraListView_Dialog.DialogMuestrasListener, MuestraDatos_Dialog.OnListener{

    //Widgets

        private Button cambiarRutina;

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
        private boolean userSinRutina;
        private int numObjetivoUser;
        private ArrayList<String>ejerciciosRutina;
        private ArrayList<Ejercicio>ejercicios;
        private CalendarView calendario;
        private Rutina_User rutinaUsuario;
        private Rutina rutinaRecuperada;
        private int diaRutina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutinas);

        userSinRutina=false;

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

        //Recupero la rutina de la bd

        DatabaseReference dbRutina = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        ValueEventListener eventListener2;

        eventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                numObjetivoUser=Integer.valueOf(dataSnapshot.child("objetivo").getValue().toString());
                rutinaUsuario=dataSnapshot.child("rutina").getValue(Rutina_User.class);

                if(dataSnapshot.child("rutina").getValue()==null){
                    userSinRutina=true;
                    muestraDialogNumDias();
                }

                //Instancio el calendario e importo los metodos

                calendario = (CalendarView)findViewById(R.id.calendarView);
                calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                        int diaSemana = recuperaIdDia(dayOfMonth,month,year);
                        muestraDiaRutina(diaSemana);

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toasty.error(RutinasActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();

            }

        };

        dbRutina.addValueEventListener(eventListener2);

        //Intancio los widgets

            cambiarRutina = (Button)findViewById(R.id.cambiarRutina);

        //Escuchadores

            cambiarRutina.setOnClickListener(this);

    }

    public void muestraDialogNumDias(){
        transaction = getFragmentManager().beginTransaction();
        dialogoNumDias = new numDias_Dialog(userSinRutina);
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
                    if(userSinRutina==true){
                        finish();
                    }
                    Toasty.error(RutinasActivity.this,"Has introducido mas dias de los que vas a ir al gimnasio", Toast.LENGTH_SHORT).show();
                }
                else if(diasSemana.size()<numDias){
                    if(userSinRutina==true){
                        finish();
                    }
                    Toasty.error(RutinasActivity.this,"Has introducido menos dias de los que vas a ir al gimnasio", Toast.LENGTH_SHORT).show();
                }
                else{

                    if(numObjetivoUser>0){
                        objetivoSeleccionado=numObjetivoUser;
                        muestraDialgoRutinas();
                    }

                    else{
                        //Llama al dialogo que pide el objetivo
                            muestraDialogObjetivo();
                    }

                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(userSinRutina==true){
                    finish();
                }
                else{
                    dialogInterface.dismiss();
                }
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
                if(userSinRutina==true){
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
    public void onClick(View view) {

        if(view.getId()==cambiarRutina.getId()){
            muestraDialogNumDias();
        }

    }

    @Override
    public void pasaNum(int numDias) {
        this.numDias=numDias;
        seleccionDias(numDias).show();
    }

    @Override
    public void finalizaActivity() {
        finish();
    }

    //Hay que pasarle los parametros y usarlos como filtros en la busqueda de rutinas

    public void muestraDialgoRutinas(){

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
        this.rutinaCambio=rutina;
        traduceEjercicios();
    }

    public void llamaDialogo(){
        //Llamo al dialogo
        transaction = getFragmentManager().beginTransaction();
        muestraRutinas=new MuestraListView_Dialog(rutinasFiltradas,null);
        muestraRutinas.setDialogMuestrasListener(this);
        muestraRutinas.setCancelable(false);
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
            dbUpdateRutina.child("objetivo").setValue(objetivoSeleccionado);
            userSinRutina=false;

    }

    @Override
    public void onCancelled() {
        if(userSinRutina==true){
            finish();
        }
    }

    //Metodo para convertir los id de los ejercicios de la rutina en el nombre del ejercicio para mostrarlo en el dialogo

    public void traduceEjercicios(){

        DatabaseReference dbEjercicios = FirebaseDatabase.getInstance().getReference().child("Ejercicio");

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        ValueEventListener eventListener;

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<Ejercicio>> t = new GenericTypeIndicator<ArrayList<Ejercicio>>() {};

                ejercicios = dataSnapshot.getValue(t);
                ejerciciosRutina=new ArrayList<>();
                for(int i=0;i<rutinaCambio.getEjercicios().size();i++){

                    for(int j=1;j<ejercicios.size();j++){

                        if(rutinaCambio.getEjercicios().get(i).getId_ejercicio()==ejercicios.get(j).getId_ejercicio()){
                            ejerciciosRutina.add(ejercicios.get(j).getNombreEjercicio().toString());
                        }

                    }

                }

                dialogoMuestraRutina = new MuestraDatos_Dialog(ejerciciosRutina,rutinaCambio,null,null);
                dialogoMuestraRutina.setListener(RutinasActivity.this);
                dialogoMuestraRutina.show(transaction,null);
                dialogoMuestraRutina.setCancelable(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toasty.error(RutinasActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();

            }

        };

        dbEjercicios.addValueEventListener(eventListener);

    }

    public int recuperaIdDia(int dia,int mes,int anyo){

        int idDia=0;

        //Parseamos la fecha a nombre del dia

        String fecha=dia+1+"/"+mes+"/"+anyo;
        SimpleDateFormat formato1 = new SimpleDateFormat("dd/mm/yyyy");
        Date fechaDate = new Date();
        try {
            fechaDate = formato1.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formato2 = new SimpleDateFormat("EEEE");
        String diaNombre = formato2.format(fechaDate);

        //Convertimos el nombre del dia en el id

        if(diaNombre.equals("Monday")){
            idDia=0;
        }
        else if(diaNombre.equals("Tuesday")){
            idDia=1;
        }
        else if(diaNombre.equals("Wednesday")){
            idDia=2;
        }
        else if(diaNombre.equals("Thursday")){
            idDia=3;
        }
        else if(diaNombre.equals("Friday")){
            idDia=4;
        }
        else if(diaNombre.equals("Saturday")){
            idDia=5;
        }
        else if(diaNombre.equals("Sunday")){
            idDia=6;
        }

        return idDia;

    }

    //Metodo que llama al dialogo que toca para mostrar las rutinas

        public void muestraDiaRutina(int id_dia){

            boolean vacio=true;
            ArrayList<Integer>dias = new ArrayList<>();
            final ArrayList<Rutina_Ejercicio>rutinaEjercicios = new ArrayList<>();
            dias = rutinaUsuario.getDias();
            diaRutina=1;

            for(int i=0;i<dias.size();i++){

                if(id_dia==dias.get(i)){

                    //Recupero la rutina del usuario de la bd

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                    //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

                        ValueEventListener eventListener;

                    eventListener= new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            rutinaEjercicios.clear();

                            rutinaRecuperada = dataSnapshot.child("rutina").child(Integer.toString(rutinaUsuario.getId_rutina())).getValue(Rutina.class);
                            for(int i=0;i<rutinaRecuperada.getEjercicios().size();i++){
                                if(rutinaRecuperada.getEjercicios().get(i).getDia_semana()==diaRutina){
                                    rutinaEjercicios.add(rutinaRecuperada.getEjercicios().get(i));
                                    Toasty.info(RutinasActivity.this,Integer.toString(rutinaEjercicios.get(i).getId_ejercicio()),Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    db.addValueEventListener(eventListener);

                    vacio=false;

                }

            }

            if(vacio==true){
                Toasty.info(RutinasActivity.this,"Este dia no esta asignado en tu rutina",Toast.LENGTH_SHORT).show();
            }

        }

}
