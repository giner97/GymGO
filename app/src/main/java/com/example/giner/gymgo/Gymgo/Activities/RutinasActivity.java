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

import com.example.giner.gymgo.Gymgo.Dialogos.DatosEjercicioDialog;
import com.example.giner.gymgo.Gymgo.Dialogos.MuestraDatos_Dialog;
import com.example.giner.gymgo.Gymgo.Dialogos.MuestraListView_Dialog;
import com.example.giner.gymgo.Gymgo.Dialogos.numDias_Dialog;
import com.example.giner.gymgo.Objetos.Dieta;
import com.example.giner.gymgo.Objetos.Ejercicio;
import com.example.giner.gymgo.Objetos.EventDecorator;
import com.example.giner.gymgo.Objetos.Objetivo;
import com.example.giner.gymgo.Objetos.Plato;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.CalendarWeekDayFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
        private ArrayList<Rutina_Ejercicio>rutinaEjercicios;
        private ArrayAdapter<Rutina> arrayAdapterRutinas;
        private MuestraListView_Dialog muestraRutinas;
        private MuestraListView_Dialog muestraEjercicio;
        private DatosEjercicioDialog datosEjercicio;
        private Rutina rutinaCambio;
        private MuestraDatos_Dialog dialogoMuestraRutina;
        private ArrayList diasSemana = new ArrayList();
        private boolean userSinRutina;
        private int numObjetivoUser;
        private ArrayList<String>ejerciciosRutina;
        private ArrayList<Ejercicio>ejercicios;
        private MaterialCalendarView calendario;
        private Rutina_User rutinaUsuario;
        private Rutina rutinaRecuperada;
        private Rutina_Ejercicio rutinaEjercicio;
        private int diaRutina;
        private ArrayList<Ejercicio> listaEjercicios = new ArrayList<>();
        private ArrayList<Ejercicio> listaEjerciciosFiltrada = new ArrayList<>();
        private DatabaseReference dbObjetivos=null;
        private DatabaseReference dbRutina=null;
        private DatabaseReference db=null;
        private DatabaseReference dbRutinas=null;
        private DatabaseReference dbGrupo=null;
        private DatabaseReference dbUpdateRutina=null;
        private DatabaseReference dbEjercicios=null;
        private DatabaseReference dbEjercicio=null;
        private ValueEventListener eventListener=null;
        private ValueEventListener eventListener2=null;
        private ValueEventListener eventListener3=null;
        private ValueEventListener eventListenerRutinas=null;
        private ValueEventListener eventListenerGrupo=null;
        private ValueEventListener eventListener4=null;
        private ValueEventListener eventListener5=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutinas);

        userSinRutina=false;

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

        //Recupero la rutina de la bd

        dbRutina = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        eventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                numObjetivoUser=Integer.valueOf(dataSnapshot.child("objetivo").getValue().toString());
                rutinaUsuario=dataSnapshot.child("rutina").getValue(Rutina_User.class);

                if(dataSnapshot.child("rutina").getValue()==null){
                    userSinRutina=true;
                    muestraDialogNumDias();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        dbRutina.addValueEventListener(eventListener2);

        //Recupero la rutina del usuario de la bd

        db = FirebaseDatabase.getInstance().getReference();

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        eventListener3= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(rutinaUsuario!=null) {
                    rutinaRecuperada = dataSnapshot.child("rutina").child(Integer.toString(rutinaUsuario.getId_rutina())).getValue(Rutina.class);
                }

                calendario = (MaterialCalendarView)findViewById(R.id.calendarView);
                //EventDecorator eventDecorator = new EventDecorator(R.color.colorPrimaryDark, );
                //calendario.addDecorator(eventDecorator);
                calendario.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                        int diaSemana = recuperaIdDia(date.getDate());
                        muestraDiaRutina(diaSemana);
                    }
                });

                //Intancio los widgets

                cambiarRutina = (Button)findViewById(R.id.cambiarRutina);

                //Escuchadores

                cambiarRutina.setOnClickListener(RutinasActivity.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        db.addValueEventListener(eventListener3);

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
                        cerrarConexiones();
                        finish();
                    }
                    Toasty.error(RutinasActivity.this,"Has introducido mas dias de los que vas a ir al gimnasio", Toast.LENGTH_SHORT).show();
                }
                else if(diasSemana.size()<numDias){
                    if(userSinRutina==true){
                        cerrarConexiones();
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
                    cerrarConexiones();
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
                    cerrarConexiones();
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
        cerrarConexiones();
        finish();
    }

    //Hay que pasarle los parametros y usarlos como filtros en la busqueda de rutinas

    public void muestraDialgoRutinas(){

        //Recupero las rutinas de la bd

            dbRutinas = FirebaseDatabase.getInstance().getReference().child("rutina");

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

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

    @Override
    public void onMuestraEjercicio(final Ejercicio ejercicio) {

        for(int i=0;i<rutinaEjercicios.size();i++){
            if(rutinaEjercicios.get(i).getId_ejercicio()==ejercicio.getId_ejercicio()){
                rutinaEjercicio = rutinaEjercicios.get(i);
            }
        }

        //Recupero el grupo muscular

        dbGrupo = FirebaseDatabase.getInstance().getReference().child("Grupo_muscular").child(Integer.toString(ejercicio.getGrupo_muscular()));

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        eventListenerGrupo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String grupoMuscular=dataSnapshot.child("nombre_grupoMuscular").getValue(String.class);

                //Llamo a otro dialogo para mostrar los datos del ejercicio seleccionado. Le paso rutinaEjercicio.getEjercicio.get(), el ejercicio seleccionado y el grupo muscular

                transaction = getFragmentManager().beginTransaction();
                datosEjercicio = new DatosEjercicioDialog(ejercicio,rutinaEjercicio,grupoMuscular);
                datosEjercicio.show(transaction,null);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbGrupo.addValueEventListener(eventListenerGrupo);

    }

    @Override
    public void onMuestraPlato(Plato platos) {
        //Este metodo no se usa en esta activity
    }

    public void llamaDialogo(){
        //Llamo al dialogo
        transaction = getFragmentManager().beginTransaction();
        muestraRutinas=new MuestraListView_Dialog(rutinasFiltradas,null, null, null);
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

            dbUpdateRutina = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);
            dbUpdateRutina.child("rutina").setValue(insercionRutina);
            dbUpdateRutina.child("objetivo").setValue(objetivoSeleccionado);
            userSinRutina=false;

    }

    @Override
    public void onCancelled() {
        if(userSinRutina==true){
            cerrarConexiones();
            finish();
        }
    }

    //Metodo para convertir los id de los ejercicios de la rutina en el nombre del ejercicio para mostrarlo en el dialogo

    public void traduceEjercicios(){

        dbEjercicios = FirebaseDatabase.getInstance().getReference().child("Ejercicio");

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        eventListener4 = new ValueEventListener() {
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

            }

        };

        dbEjercicios.addValueEventListener(eventListener4);

    }

    public int recuperaIdDia(Date date){

        int idDia=0;

        //Parseamos la fecha a nombre del dia

        SimpleDateFormat formato2 = new SimpleDateFormat("EEEE");
        String diaNombre = formato2.format(date);

        //Toast.makeText(this, diaNombre, Toast.LENGTH_SHORT).show();

        //Convertimos el nombre del dia en el id

        if((diaNombre.equals("Monday"))||(diaNombre.equals("lunes"))){
            idDia=0;
        }
        else if((diaNombre.equals("Tuesday"))||(diaNombre.equals("martes"))){
            idDia=1;
        }
        else if((diaNombre.equals("Wednesday"))||(diaNombre.equals("miércoles"))){
            idDia=2;
        }
        else if((diaNombre.equals("Thursday"))||(diaNombre.equals("jueves"))){
            idDia=3;
        }
        else if((diaNombre.equals("Friday"))||(diaNombre.equals("viernes"))){
            idDia=4;
        }
        else if((diaNombre.equals("Saturday"))||(diaNombre.equals("sábado"))){
            idDia=5;
        }
        else if((diaNombre.equals("Sunday"))||(diaNombre.equals("domingo"))){
            idDia=6;
        }

        return idDia;

    }

    //Metodo que llama al dialogo que toca para mostrar las rutinas

        public void muestraDiaRutina(int id_dia){

            boolean vacio=true;
            ArrayList<Integer>dias = new ArrayList<>();
            rutinaEjercicios = new ArrayList<>();
            dias = rutinaUsuario.getDias();
            dias = rutinaUsuario.getDias();
            diaRutina=1;
            rutinaEjercicios.clear();
            ArrayList<Ejercicio>ejerciciosDia = new ArrayList<>();

            for(int i=0;i<dias.size();i++){

                if(id_dia==dias.get(i)){


                    for(int j=0;j<rutinaRecuperada.getEjercicios().size();j++){
                        if(rutinaRecuperada.getEjercicios().get(j).getDia_semana()==diaRutina){
                            rutinaEjercicios.add(rutinaRecuperada.getEjercicios().get(j));
                        }
                    }

                    //Recupero y filtro los ejercicios

                        ejerciciosDia=recuperaEjercicios(rutinaEjercicios);

                    //Llamo al dialogo y le paso los ejercicios filtrados

                        transaction = getFragmentManager().beginTransaction();
                        muestraEjercicio=new MuestraListView_Dialog(null,null, ejerciciosDia, null);
                        muestraEjercicio.setDialogMuestrasListener(this);
                        muestraEjercicio.setCancelable(false);
                        muestraEjercicio.show(transaction,null);

                    vacio=false;

                }

                else{
                    diaRutina++;
                }

            }

            if(vacio==true){
                Toasty.info(RutinasActivity.this,"Este dia no esta asignado en tu rutina",Toast.LENGTH_SHORT).show();
            }

        }

        public ArrayList<Ejercicio>recuperaEjercicios(final ArrayList<Rutina_Ejercicio> rutina_ejercicio){

            dbEjercicio = FirebaseDatabase.getInstance().getReference().child("Ejercicio");

            //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

            listaEjerciciosFiltrada.clear();

            eventListener5 = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    GenericTypeIndicator<ArrayList<Ejercicio>> t = new GenericTypeIndicator<ArrayList<Ejercicio>>() {};
                    listaEjercicios = dataSnapshot.getValue(t);

                    for(int i=0;i<rutina_ejercicio.size();i++){

                        for(int j=1;j<listaEjercicios.size();j++){

                            if(rutina_ejercicio.get(i).getId_ejercicio()==listaEjercicios.get(j).getId_ejercicio()){
                                listaEjerciciosFiltrada.add(listaEjercicios.get(j));
                            }

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            };

            dbEjercicio.addValueEventListener(eventListener5);

            return listaEjerciciosFiltrada;

        }

    @Override
    public void onBackPressed() {
        cerrarConexiones();
        finish();
    }

    public void cerrarConexiones(){
        try {
            dbObjetivos.removeEventListener(eventListener);
            dbRutina.removeEventListener(eventListener2);
            db.removeEventListener(eventListener3);
            dbRutinas.removeEventListener(eventListenerRutinas);
            dbGrupo.removeEventListener(eventListenerGrupo);
            dbEjercicios.removeEventListener(eventListener4);
            dbEjercicio.removeEventListener(eventListener5);
        }
        catch (Exception e){

        }
    }

}
