package com.example.giner.gymgo.Gymgo.Activities;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.giner.gymgo.Objetos.Objetivo;
import com.example.giner.gymgo.Objetos.Plato;
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
    private CalendarView calendario;
    private String uidUser;
    private int numObjetivoUser;
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

                Toasty.error(DietasActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();

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

                //Instancio el calendario e importo los metodos

               /* calendario = (CalendarView)findViewById(R.id.calendarView);
                calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                        int diaSemana = recuperaIdDia(dayOfMonth,month,year);
                        muestraDiaRutina(diaSemana);

                    }
                });*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toasty.error(DietasActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();

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
    public void onObjetoSeleccionado(Rutina rutinaSeleccionada, Dieta dietaSeleccionada) {
        dietaCambio=dietaSeleccionada;
        Dieta inserccionDieta= new Dieta();
        inserccionDieta.setId_dieta(rutinaSeleccionada.getId_rutina());


        //Llamo al metodo para realizar el cambio.

        DatabaseReference dbUpdateDieta = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);
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

                Toasty.error(DietasActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();

            }

        };

        dbPlatos.addValueEventListener(eventListener);

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

    /*public void muestraDiaDieta(int id_dia){

        boolean vacio=true;
        int diasDieta = 6;
        ArrayList<Integer>dias = new ArrayList<>();
        final ArrayList<Dieta>dietasDias = new ArrayList<>();
        dias = 6;
        diaDietas=1;

        for(int i=0;i<dias.size();i++){

            if(id_dia==dias.get(i)){

                //Recupero la rutina del usuario de la bd

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

                ValueEventListener eventListener;

                eventListener= new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        platos.clear();

                        dietaRecuperada = dataSnapshot.child("Dieta").child(Integer.toString(platos.get())).getValue(Dieta.class);
                        for(int i=0;i<dietaRecuperada.getPlato().size();i++){
                            if(dietaRecuperada.getPlato().get(i).getDia_semana()==diaDietas){
                                platos.add(dietaRecuperada.getPlato().get(i));
                                Toasty.info(DietasActivity.this,Integer.toString(plato.get(i).getId_dieta()),Toast.LENGTH_SHORT).show();
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
            Toasty.info(DietasActivity.this,"Este dia no esta asignado en tu dieta",Toast.LENGTH_SHORT).show();
        }*/

}