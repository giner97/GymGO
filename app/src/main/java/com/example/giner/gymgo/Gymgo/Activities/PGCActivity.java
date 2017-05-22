package com.example.giner.gymgo.Gymgo.Activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giner.gymgo.R;

import es.dmoral.toasty.Toasty;

public class PGCActivity extends AppCompatActivity implements View.OnClickListener{

    private String sexo[]= new String[2];
    private Button botonCalcular;
    private TextView informacion ,altura, cintura , cuello,cadera;
    private TextView informacion2;
    private EditText introAltura,introCuello,introCadera, introCintura;
    int tipoPersona;
    int eleccion =0;
    int sexoSeleccionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgc);

        informacion = (TextView)findViewById(R.id.informacion);
        informacion2 = (TextView)findViewById(R.id.informacion2);
        botonCalcular = (Button) findViewById(R.id.calcular);

        botonCalcular.setOnClickListener(this);

        sexo[0]="Hombre";
        sexo[1]="Mujer";


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.calcular){
            iniciarCalculo();
        }
    }

    public void iniciarCalculo (){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Introduzca su sexo");

        eleccion=0;
        builder.setSingleChoiceItems(sexo, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                eleccion=which;
            }
        });

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog , int eleccionPulsada){
                sexoSeleccionado=eleccion;
                realizarCalulo(sexoSeleccionado);
            }
        });

        builder.setNegativeButton("ATRAS", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog , int eleccionPulsada){
                dialog.dismiss();
            }
        });

        builder.show();
        builder.setCancelable(false);

    }

    public void realizarCalulo(final int opcionSeleccionada){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater =  this.getLayoutInflater();
        View w= inflater.inflate(R.layout.dialog_pgc,null);

        builder.setView(w);

        altura=(TextView)w.findViewById(R.id.altura);
        cintura=(TextView)w.findViewById(R.id.cintura);
        cadera=(TextView)w.findViewById(R.id.cadera);
        cuello=(TextView)w.findViewById(R.id.cuello);

        introCuello=(EditText)w.findViewById(R.id.introCuello);
        introCadera=(EditText)w.findViewById(R.id.introCadera);
        introCintura=(EditText)w.findViewById(R.id.introCintura);
        introAltura=(EditText)w.findViewById(R.id.introAltura);

        if(opcionSeleccionada==0){
            introCadera.setVisibility(View.GONE);
            cadera.setVisibility(View.GONE);

        }else{

            introCadera.setVisibility(View.VISIBLE);
            cadera.setVisibility(View.VISIBLE);

        }

        builder.setPositiveButton("Calcular PGC", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opcionSeleccionada == 0) {

                    if ((!introAltura.getText().toString().isEmpty()) && (!introCintura.getText().toString().isEmpty()) && (!introCuello.getText().toString().isEmpty())) {

                            if ((Double.valueOf(introAltura.getText().toString()) > 0) && (Double.valueOf(introCintura.getText().toString()) > 0) && (Double.valueOf(introCuello.getText().toString()) > 0)) {
                                realizarOperacionHombre();
                            }
                            else {
                                Toasty.warning(PGCActivity.this, "Debes introducir valores mayores de 0", Toast.LENGTH_SHORT).show();
                            }


                    }
                    else {
                            Toasty.warning(PGCActivity.this, "Te has dejado algún campo vacío", Toast.LENGTH_SHORT).show();
                    }

                }

                else {
                    if ((!introAltura.getText().toString().isEmpty()) && (!introCintura.getText().toString().isEmpty()) && (!introCuello.getText().toString().isEmpty())&&(!introCadera.getText().toString().isEmpty())){

                        if ((Double.valueOf(introAltura.getText().toString()) > 0) && (Double.valueOf(introCintura.getText().toString()) > 0) && (Double.valueOf(introCuello.getText().toString()) > 0)&&(Double.valueOf(introCadera.getText().toString())>0)){
                            realizarOperacionMujer();
                        }
                        else {
                            Toasty.warning(PGCActivity.this, "Debes introducir valores mayores de 0", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toasty.warning(PGCActivity.this, "Te has dejado algún campo vacío", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        builder.setNegativeButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void realizarOperacionHombre(){

        double  cintura, cuello , altura, resultado;

        cintura= Double.valueOf(introCintura.getText().toString());
        cuello= Double.valueOf(introCuello.getText().toString());
        altura= Double.valueOf(introAltura.getText().toString());

        resultado = (495/(1.0324-(0.19077*(Math.log((cintura-cuello))))+(0.15456*(Math.log(altura)))))-450;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Porcentaje de Grasa");
        builder.setMessage(Double.toString(Math.round(resultado*100.0)/100.0));
        builder.show();

    }
    public void realizarOperacionMujer(){

        double  cintura, cuello , altura,cadera, resultado;

        cintura= Double.valueOf(introCintura.getText().toString());
        cuello= Double.valueOf(introCuello.getText().toString());
        altura= Double.valueOf(introAltura.getText().toString());
        cadera =Double.valueOf(introCadera.getText().toString());

        resultado = (495/(1.29579-(0.35004*Math.log(cintura+cadera-cuello))+(0.22100*Math.log(altura))))-450;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Porcentaje de Grasa");
        builder.setMessage(Double.toString(Math.round(resultado*100.0)/100.0));
        builder.show();

    }

}