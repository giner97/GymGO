package com.example.giner.gymgo.Gymgo.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.giner.gymgo.Gymgo.Dialogos.RevisionDialog;
import com.example.giner.gymgo.Objetos.Revision_user;
import com.example.giner.gymgo.Objetos.Usuario;
import com.example.giner.gymgo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RevisionesActivity extends AppCompatActivity implements View.OnClickListener, RevisionDialog.OnRevision{

    //Widgets

        private Button buttonAnyadir;
        private Button buttonConsultar;
        private GraphView graph;
        private TextView pesoIdealTextView;

    //Variables

        private String uidUser;
        private ArrayList<Revision_user>revisionesUsuario = new ArrayList<>();
        private boolean userSinRevisiones=false;
        private double peso_ideal;
        private Usuario user;
        private FragmentTransaction transactionRevisiones;
        private RevisionDialog revisionDialog;
        private DatabaseReference dbRevisiones;
        private ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisiones);

        //Recupero el uid del usuario loggeado

            Intent recibeUID=getIntent();
            uidUser=recibeUID.getStringExtra(MainActivity.KEY_UID);

        //Instancio los widgets

            buttonAnyadir = (Button)findViewById(R.id.buttonAnyadir);
            buttonConsultar = (Button)findViewById(R.id.buttonConsultar);
            pesoIdealTextView = (TextView)findViewById(R.id.pesoIdeal);

        graph = (GraphView) findViewById(R.id.graph);

        //Recupero las revisiones del usuario

            dbRevisiones = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);

        //Escuchador para controlar cuando se modifican los datos en la bd, notificarlo a la aplicacion

            eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    user = dataSnapshot.getValue(Usuario.class);

                    if((dataSnapshot.child("revisiones").getValue()==null)){
                        if(dataSnapshot.child("pesoIdeal").getValue(double.class)==0){
                            userSinRevisiones = true;
                            usuarioSinRevision();
                        }
                    }

                    else if((dataSnapshot.child("revisiones").getValue()!=null)&&(dataSnapshot.child("pesoIdeal").getValue(double.class)!=0)){

                        peso_ideal = dataSnapshot.child("pesoIdeal").getValue(double.class);

                        pesoIdealTextView.setText(Double.toString(peso_ideal));

                        GenericTypeIndicator<ArrayList<Revision_user>> t = new GenericTypeIndicator<ArrayList<Revision_user>>() {
                        };
                        revisionesUsuario = dataSnapshot.child("revisiones").getValue(t);

                        /*LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, revisionesUsuario.get(0).getPeso_revision()),
                                new DataPoint(1, revisionesUsuario.get(1).getPeso_revision()),
                                new DataPoint(2, revisionesUsuario.get(2).getPeso_revision()),
                                new DataPoint(3, revisionesUsuario.get(3).getPeso_revision()),
                                new DataPoint(4, revisionesUsuario.get(4).getPeso_revision())
                        });
                        graph.addSeries(series);*/
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            };

            dbRevisiones.addValueEventListener(eventListener);

    }


    @Override
    public void onClick(View v) {



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

        if(userSinRevision==true){
            DatabaseReference dbRevision = FirebaseDatabase.getInstance().getReference().child("User").child(uidUser);
            Double IMC = calcularIMC(peso,altura);
            Double peso_idealAuto=calcularPesoIdeal(altura);
            String fecha_revision = parseaFecha(new Date());
            Revision_user revisionAuto = new Revision_user(0,peso,altura,IMC,fecha_revision);
            dbRevision.child("revisiones").child("0").setValue(revisionAuto);
            dbRevision.child("pesoIdeal").setValue(peso_idealAuto);
            dbRevision.child("peso").setValue(peso);
            dbRevision.child("altura").setValue(altura);
        }

        else{


        }

    }

    public void cerrarConexiones(){
        dbRevisiones.removeEventListener(eventListener);
    }

    @Override
    public void onBackPressed() {
        cerrarConexiones();
        finish();
    }
}
