package com.example.giner.gymgo.Gymgo.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giner.gymgo.Gymgo.Dialogos.RevisionDialog;
import com.example.giner.gymgo.Objetos.Revision_user;
import com.example.giner.gymgo.Objetos.Usuario;
import com.example.giner.gymgo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class RevisionesActivity extends AppCompatActivity implements View.OnClickListener, RevisionDialog.OnRevision{

    //Widgets

        private Button buttonAnyadir;
        private Button buttonConsultar;
        private GraphView graph;
        private TextView pesoIdealTextView;

    //Variables

        private String uidUser;
        private ArrayList<Revision_user>revisionesUsuario = new ArrayList<>();
        private ArrayList<Revision_user>listaRevisiones = new ArrayList<>();
        private boolean userSinRevisiones=false;
        private double peso_ideal;
        private Usuario user;
        private FragmentTransaction transactionRevisiones;
        private RevisionDialog revisionDialog;
        private DatabaseReference dbUser;
        private ValueEventListener eventListener2;
        private DatabaseReference dbRevisiones;
        private ValueEventListener eventListener3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisiones);

        //Recupero el uid del usuario loggeado

            Intent recibeUID=getIntent();
            uidUser=recibeUID.getStringExtra(MainActivity.KEY_UID);

        graph = (GraphView) findViewById(R.id.graph);

        //Recupero el usuario

        dbUser = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        eventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(Usuario.class);
                if(dataSnapshot.child("pesoIdeal").getValue()!=null) {

                    pesoIdealTextView = (TextView)findViewById(R.id.pesoIdeal);

                    peso_ideal = dataSnapshot.child("pesoIdeal").getValue(double.class);
                    pesoIdealTextView.setText(Double.toString(peso_ideal));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        dbUser.addValueEventListener(eventListener2);

        //Recupero el usuario

        dbRevisiones = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser).child("revisiones");

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

        eventListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null){

                    userSinRevisiones = false;

                    GenericTypeIndicator<ArrayList<Revision_user>> t = new GenericTypeIndicator<ArrayList<Revision_user>>() {};
                    listaRevisiones = dataSnapshot.getValue(t);

                    ArrayList<Double>pesosRevisiones = new ArrayList<>();
                    pesosRevisiones.clear();
                    pesosRevisiones = cargaGrafico(listaRevisiones);

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                            new DataPoint(0, pesosRevisiones.get(0)),
                            new DataPoint(1, pesosRevisiones.get(1)),
                            new DataPoint(2, pesosRevisiones.get(2)),
                            new DataPoint(3, pesosRevisiones.get(3)),
                            new DataPoint(4, pesosRevisiones.get(4))
                    });
                    graph.removeAllSeries();
                    graph.addSeries(series);

                    //Instancio los widgets

                        buttonAnyadir = (Button)findViewById(R.id.buttonAnyadir);
                        buttonConsultar = (Button)findViewById(R.id.buttonConsultar);

                    //Escuchadores de los botones

                        buttonAnyadir.setOnClickListener(RevisionesActivity.this);
                        buttonConsultar.setOnClickListener(RevisionesActivity.this);

                }

                else if(dataSnapshot.getValue()==null){
                    userSinRevisiones = true;
                    usuarioSinRevision();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        dbRevisiones.addValueEventListener(eventListener3);

    }


    @Override
    public void onClick(View v) {

        if(v.getId()==buttonAnyadir.getId()){

            //Llamo al dialogo
            transactionRevisiones = getFragmentManager().beginTransaction();
            revisionDialog=new RevisionDialog(userSinRevisiones);
            revisionDialog.setRevisionListener(this);
            revisionDialog.setCancelable(false);
            revisionDialog.show(transactionRevisiones,null);

        }
        else if(v.getId()==buttonConsultar.getId()){

        }

    }

    public void usuarioSinRevision(){

        //Compruebo si se ha introducido el peso y la altura desde modificaUser

            if(user.getPeso()==0&&user.getAltura()==0){
                //Se realizara la primera revision pidiendo los datos al usuario
                //Llamo al dialogo y le paso como parametro el boolean userSinRutina

                //Llamo al dialogo
                transactionRevisiones = getFragmentManager().beginTransaction();
                revisionDialog=new RevisionDialog(userSinRevisiones);
                revisionDialog.setRevisionListener(this);
                revisionDialog.setCancelable(false);
                revisionDialog.show(transactionRevisiones,null);

            }

            else{
                //Se realizara la primera revision automaticamente con los datos introducidos en user
                    generaRevisionAuto(user.getPeso(),user.getAltura());
            }

    }

    public void anyadirRevision(){



    }

    public void generaRevisionAuto(Double peso, Double altura){

        //Genero la revision automaticamente

            DatabaseReference dbRevision = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);
            Double IMC = calcularIMC(peso,altura);
            Double peso_idealAuto=calcularPesoIdeal(altura);
            String fecha_revision = parseaFecha(new Date());
            Revision_user revisionAuto = new Revision_user(0,peso,altura,IMC,fecha_revision);
            dbRevision.child("revisiones").child("0").setValue(revisionAuto);
            dbRevision.child("pesoIdeal").setValue(peso_idealAuto);

    }

    public double calcularIMC(Double peso, Double altura){

        double resultado;
        double alturaCuadrado=altura*altura;

        resultado = peso/alturaCuadrado;

        return resultado;

    }

    public String parseaFecha(Date fecha){

        String resultado;

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        resultado=format.format(fecha);

        return resultado;

    }

    public double calcularPesoIdeal(Double altura){

        double resultado;
        double alturaCm;
        double restaAltura;

        alturaCm=altura*100;
        restaAltura = alturaCm-150;

        resultado=0.75*restaAltura+50;

        return resultado;

    }

    @Override
    public void finalizaActivity() {
        cerrarConexiones();
        finish();
    }

    @Override
    public void pasaDatos(double altura, double peso, boolean userSinRevision) {

        DatabaseReference dbRevision = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);
        Double IMC = calcularIMC(peso,altura);
        String fecha_revision = parseaFecha(new Date());
        dbRevision.child("peso").setValue(peso);
        dbRevision.child("altura").setValue(altura);
        Double peso_idealAuto=calcularPesoIdeal(altura);
        dbRevision.child("pesoIdeal").setValue(peso_idealAuto);

        if(userSinRevision==true){
            Revision_user revisionAuto = new Revision_user(0,peso,altura,IMC,fecha_revision);
            dbRevision.child("revisiones").child("0").setValue(revisionAuto);
        }

        else{
            int numRevision= listaRevisiones.size();
            Revision_user revisionAuto = new Revision_user(numRevision,peso,altura,IMC,fecha_revision);
            dbRevision.child("revisiones").child(Integer.toString(numRevision)).setValue(revisionAuto);
        }

    }

    public void cerrarConexiones(){
        dbUser.removeEventListener(eventListener2);
        dbRevisiones.removeEventListener(eventListener3);
    }

    @Override
    public void onBackPressed() {
        cerrarConexiones();
        finish();
    }

    public ArrayList<Double>cargaGrafico(ArrayList<Revision_user>revisiones){

        ArrayList<Double> resultado = new ArrayList<>();

        if(revisiones.size()<=5) {

            for (int i = 0; i < revisiones.size(); i++) {
                if (revisiones.get(i) != null) {
                    resultado.add(revisiones.get(i).getPeso_revision());
                }
            }
            for (int j = 0; j < (5 - revisiones.size()); j++) {
                resultado.add((double) 0);
            }
        }

        else{
            for(int i=revisiones.size()-5;i<revisiones.size();i++){
                resultado.add(revisiones.get(i).getPeso_revision());
            }
        }

        return resultado;
    }

}
