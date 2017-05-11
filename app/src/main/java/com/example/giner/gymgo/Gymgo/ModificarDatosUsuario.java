package com.example.giner.gymgo.Gymgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.giner.gymgo.Objetos.Usuario;
import com.example.giner.gymgo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModificarDatosUsuario extends AppCompatActivity implements View.OnClickListener{

    private TextView altura,apellidos,nombre,peso;
    private EditText lblaltura,lblapellidos,lblnombre,lblpeso;
    private Button aceptarCambios;
    private FirebaseUser userLogueado;
    private int numRecuperados;
    private Usuario user;
    private String idUserLogueado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_datos_usuario);

        /*DE INSTANCIA LOS WIDGETS*/
        altura= (TextView)findViewById(R.id.altura);
        apellidos= (TextView)findViewById(R.id.apellidos);
        nombre= (TextView)findViewById(R.id.nombre);
        peso= (TextView)findViewById(R.id.peso);

        aceptarCambios=(Button)findViewById(R.id.aceptarCambios);

        lblaltura=(EditText)findViewById(R.id.lblaltura);
        lblapellidos=(EditText)findViewById(R.id.lblapellidos);
        lblnombre=(EditText)findViewById(R.id.lblnombre);
        lblpeso=(EditText)findViewById(R.id.lblpeso);


        /**/

        userLogueado = FirebaseAuth.getInstance().getCurrentUser();
            idUserLogueado=userLogueado.getUid();

        final DatabaseReference dbUsuarios = FirebaseDatabase.getInstance().getReference()
                        .child("User");

        while(dbUsuarios!=null){
            numRecuperados++;
        }


        dbUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lblaltura.setText(dataSnapshot.child(String.valueOf(numRecuperados)).child(idUserLogueado).child("altura").getValue().toString());
                lblapellidos.setText(dataSnapshot.child(String.valueOf(numRecuperados)).child(idUserLogueado).child("apellidos").getValue().toString());
                lblnombre.setText(dataSnapshot.child(String.valueOf(numRecuperados)).child(idUserLogueado).child("nombre").getValue().toString());
                lblpeso.setText(dataSnapshot.child(String.valueOf(numRecuperados)).child(idUserLogueado).child("peso").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.aceptarCambios){

        }
    }
}
