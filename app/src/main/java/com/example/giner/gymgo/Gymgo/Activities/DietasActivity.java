package com.example.giner.gymgo.Gymgo.Activities;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.giner.gymgo.Gymgo.Dialogos.MuestraDatos_Dialog;
import com.example.giner.gymgo.Gymgo.Dialogos.MuestraListView_Dialog;
import com.example.giner.gymgo.Objetos.Dieta;
import com.example.giner.gymgo.Objetos.Dieta_Plato;
import com.example.giner.gymgo.Objetos.Ejercicio;
import com.example.giner.gymgo.Objetos.Objetivo;
import com.example.giner.gymgo.Objetos.Plato;
import com.example.giner.gymgo.Objetos.Rutina;
import com.example.giner.gymgo.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

public class DietasActivity extends AppCompatActivity implements View.OnClickListener, MuestraListView_Dialog.DialogMuestrasListener, MuestraDatos_Dialog.OnListener{


    /*firebase bbbdd*/
    DatabaseReference dbObjetivos= null;
    ValueEventListener eventListener = null;

    DatabaseReference dbDieta2 = null;
    ValueEventListener eventListenerDietas =null;

    DatabaseReference dbUpdateDieta= null;
    DatabaseReference dbPlatos = null;
    ValueEventListener eventListener3= null;

    DatabaseReference dbDietas = null;
    ValueEventListener eventListener4=null;

    DatabaseReference db = null;
    ValueEventListener eventListenerDb=null;

    DatabaseReference dbComida = null;
    ValueEventListener eventListenerComida=null;

    /**/



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

    private ArrayList<Dieta_Plato>listaPlatos;
    private Dieta_Plato dietaplato;

    private MuestraListView_Dialog muestraPlatos;
    private MuestraListView_Dialog muestraDietas;
    private ArrayList<Dieta> dietasArray = new ArrayList<>();
    private ArrayList<Dieta> dietasFiltradas = new ArrayList<>();
    private MuestraDatos_Dialog dialogoMuestraDietas;
    private ArrayList<String>platosDieta;
    private Dieta dietaCambio;
    private Dieta dietaUsuario;
    private Dieta dietaRecuperada;
    private int objetivoRecuperado;
    private Boolean userSinDieta;
    private ArrayList<Plato> listaPlato = new ArrayList<>();
    private ArrayList<Plato> listaPlatoFiltrado = new ArrayList<>();


    FirebaseUser usuarioActual;
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        dbObjetivos.addValueEventListener(eventListener);

/*------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
        db = FirebaseDatabase.getInstance().getReference();

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        eventListenerDb= new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dietaRecuperada  =dataSnapshot.child("User").child(uidUser).child("dieta").getValue(Dieta.class);
                objetivoRecuperado = Integer.valueOf(dataSnapshot.child("User").child(uidUser).child("objetivo").getValue().toString());
                objetivoSeleccionado=objetivoRecuperado;

                if(dataSnapshot.child("User").child(uidUser).child("dieta").getValue()==null){
                    userSinDieta=true;

                    if(objetivoRecuperado>0) {
                        muestraDialogoDietas();
                    }

                    else{
                        muestraDialogObjetivo();
                    }

                }

                calendario = (MaterialCalendarView)findViewById(R.id.calendarView);

                calendario.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                        Calendar fechaHoy = Calendar.getInstance();
                        fechaHoy.add(Calendar.DAY_OF_YEAR,-1);
                        Calendar fechaFin = Calendar.getInstance();
                        fechaFin.add(Calendar.YEAR,1);

                        if((fechaHoy.before(date.getCalendar()))&&(fechaFin.after(date.getCalendar()))) {
                            int diaSemana = recuperaIdDia(date.getDate());
                            muestraDiaDieta(diaSemana);
                        }
                    }
                });

                //Intancio los widgets

                cambiarDieta = (Button)findViewById(R.id.cambiarDieta);

                //Escuchadores

                cambiarDieta.setOnClickListener(DietasActivity.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        db.addValueEventListener(eventListenerDb);

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

            if(objetivoRecuperado>0){muestraDialogoDietas();}else{muestraDialogObjetivo();}

        }
    }

    public void finalizaActivity() {
        finish();
    }


    public void muestraDialogoDietas(){

        dbDieta2 = FirebaseDatabase.getInstance().getReference().child("Dieta");

        eventListenerDietas = new ValueEventListener() {
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

        dbDieta2.addValueEventListener(eventListenerDietas);

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
    public void onMuestraPlato(Plato plato) {

        for(int i=0;i<listaPlatoFiltrado.size();i++){
            if(listaPlatoFiltrado.get(i).getId_plato()==plato.getId_plato()){
                dietaplato = listaPlatos.get(i);
            }
        }

        //Recupero el grupo muscular

        //dbGrupo = FirebaseDatabase.getInstance().getReference().child("Tipo_comida").child(Integer.toString(platos.getGrupo_muscular()));

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        /*eventListenerGrupo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String grupoMuscular=dataSnapshot.child("nombre_grupoMuscular").getValue(String.class);

                //Llamo a otro dialogo para mostrar los datos del ejercicio seleccionado. Le paso rutinaEjercicio.getEjercicio.get(), el ejercicio seleccionado y el grupo muscular
/*
                transaction = getFragmentManager().beginTransaction();
                datosEjercicio = new DatosEjercicioDialog(platos,listaPlato,grupoMuscular);
                datosEjercicio.show(transaction,null);*/

           /* }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbGrupo.addValueEventListener(eventListenerGrupo);*/

    }

    @Override
    public void onObjetoSeleccionado(Rutina rutinaSeleccionada, Dieta dietaSeleccionada) {
        dietaCambio=dietaSeleccionada;
        Dieta inserccionDieta= new Dieta();
        inserccionDieta=dietaSeleccionada;

        //Llamo al metodo para realizar el cambio.

        dbUpdateDieta = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);
        dbUpdateDieta.child("dieta").setValue(inserccionDieta);
        dbUpdateDieta.child("objetivo").setValue(objetivoSeleccionado);
        userSinDieta=false;

    }

    @Override
    public void onCancelled() {
        if(userSinDieta==true){
            finish();
        }
    }
    public void traducePlatos(){

        dbPlatos = FirebaseDatabase.getInstance().getReference().child("Plato");

        eventListener3 = new ValueEventListener() {
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

        dbPlatos.addValueEventListener(eventListener3);

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

        listaPlatos = new ArrayList<>();
        listaPlatos.clear();

            for (int j = 0; j < dietaRecuperada.getPlato().size(); j++) {
                if (dietaRecuperada.getPlato().get(j).getDia_semana() == id_dia) {
                    listaPlatos.add(dietaRecuperada.getPlato().get(j));
                }
            }

        listaPlatoFiltrado.clear();

        dbDietas = FirebaseDatabase.getInstance().getReference().child("Plato");

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        eventListener4 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<Plato>> t = new GenericTypeIndicator<ArrayList<Plato>>() {};
                listaPlato = dataSnapshot.getValue(t);

                for(int i=0;i<listaPlatos.size();i++){

                    for(int j=1;j<listaPlato.size();j++){

                        if(listaPlatos.get(i).getId_plato()==listaPlato.get(j).getId_plato()){
                            listaPlatoFiltrado.add(listaPlato.get(j));
                        }

                    }

                }

                recuperarPlato(listaPlatoFiltrado);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbDietas.addValueEventListener(eventListener4);

    }

    public void recuperarPlato(final ArrayList<Plato> platosDelDia){
        transaction = getFragmentManager().beginTransaction();
        muestraPlatos=new MuestraListView_Dialog(null,null, null,platosDelDia);
        muestraPlatos.setDialogMuestrasListener(this);
        muestraPlatos.setCancelable(false);
        muestraPlatos.show(transaction,null);
    }


    public void cerrarConexiones(){
        try {
            dbObjetivos.removeEventListener(eventListener);
            db.removeEventListener(eventListenerDb);
            dbDieta2.removeEventListener(eventListenerDietas);
            dbPlatos.removeEventListener(eventListener3);
            dbDietas.removeEventListener(eventListener4);
            dbComida.removeEventListener(eventListenerComida);
        }
        catch (Exception e){

        }
    }

    @Override
    public void onBackPressed() {
        cerrarConexiones();
        finish();
        super.onBackPressed();
    }

}