package com.example.giner.gymgo.Gymgo.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.giner.gymgo.Objetos.Dieta;
import com.example.giner.gymgo.Objetos.Ejercicio;
import com.example.giner.gymgo.Objetos.Plato;
import com.example.giner.gymgo.Objetos.Rutina;
import com.example.giner.gymgo.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class MuestraListView_Dialog extends DialogFragment implements AdapterView.OnItemClickListener{

    //Widgets

        private ListView listaDeMuestras;
        private ArrayAdapter<Rutina> arrayAdapterRutinas;
        private ArrayAdapter<Dieta>arrayAdapterDietas;
        private ArrayAdapter<Plato> arrayAdapterPlatos;
        private ArrayAdapter<Ejercicio>arrayAdapterEjercicios;


    //Variables

        private String tipoArray="";
        private Dieta dietaSeleccionada;
        private Rutina rutinaSeleccionada;
        private Ejercicio ejercicioSeleccionado;
        private Plato platoSeleccionado;
        private DialogMuestrasListener escuchador;

    //Arrays

        private ArrayList<Rutina>rutinas;
        private ArrayList<Dieta>dietas;
        private ArrayList<Ejercicio>ejercicios;
        private ArrayList<Plato>platos;

    //Dialogo

        private AlertDialog dialog;

    //Constructores

        public MuestraListView_Dialog(ArrayList<Rutina>rutinas, ArrayList<Dieta>dietas, ArrayList<Ejercicio>ejercicios, ArrayList<Plato>platos){
            if(dietas!=null) {
                this.dietas = dietas;
                tipoArray = "dieta";
            }
            else if(rutinas !=null) {
                this.rutinas = rutinas;
                tipoArray = "rutina";
            }
            else if(ejercicios!=null){
                this.ejercicios=ejercicios;
                tipoArray = "ejercicio";
            }
            else if(platos!=null){
                this.platos=platos;
                tipoArray = "plato";
            }
        }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Construyo y devuelvo el diálogo
        //Primero genero un constructor de diálogos de Alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View customDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_muestralistviews, null);

        //Instancio los widgets

            listaDeMuestras = (ListView)customDialog.findViewById(R.id.listViewMuestras);

        //Cargo el listView

            if(listaDeMuestras!=null){

                if((this.rutinas!=null)&&(arrayAdapterRutinas==null)){

                    builder.setTitle("Selecciona una "+tipoArray);

                    //Instancio el arrayAdapter y le paso el array de rutinas

                    arrayAdapterRutinas = new ArrayAdapter<Rutina>(getActivity(),android.R.layout.simple_list_item_1,rutinas);

                    //Le paso el arrayAdpater al listView

                    listaDeMuestras.setAdapter(arrayAdapterRutinas);

                }

                else if((this.dietas!=null)&&(arrayAdapterDietas==null)){

                    builder.setTitle("Selecciona una "+tipoArray);

                    //Instancio el arrayAdapter y le paso el array de dietas

                    arrayAdapterDietas = new ArrayAdapter<Dieta>(getActivity(),android.R.layout.simple_list_item_1,dietas);

                    //Le paso el arrayAdpater al listView

                    listaDeMuestras.setAdapter(arrayAdapterDietas);

                }

                else if((this.platos!=null)&&(arrayAdapterPlatos==null)){

                    builder.setTitle("Selecciona un "+tipoArray);

                    //Instancio el arrayAdapter y le paso el array de dietas

                    arrayAdapterPlatos = new ArrayAdapter<Plato>(getActivity(),android.R.layout.simple_list_item_1,platos);

                    //Le paso el arrayAdpater al listView

                    listaDeMuestras.setAdapter(arrayAdapterPlatos);

                    builder.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });

                }

                else if((this.ejercicios!=null)&&(arrayAdapterEjercicios==null)){

                    builder.setTitle("Selecciona un "+tipoArray);

                    //Instancio el arrayAdapter y le paso el array de dietas

                    arrayAdapterEjercicios = new ArrayAdapter<Ejercicio>(getActivity(),android.R.layout.simple_list_item_1,ejercicios);

                    //Le paso el arrayAdpater al listView

                    listaDeMuestras.setAdapter(arrayAdapterEjercicios);

                    builder.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });

                }

            }

        //ActionListener del listView

            listaDeMuestras.setOnItemClickListener(this);

        //Seteo el layout en el diálogo

        builder.setView(customDialog);
        dialog = builder.create();

        //Devuelvo el AlertDialog ya configurado

        return dialog;

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //Recuperamos el objeto seleccionado

        if(tipoArray.equals("rutina")){
            rutinaSeleccionada = (Rutina)adapterView.getItemAtPosition(i);
            escuchador.onMuestraRutina(rutinaSeleccionada);
            dismiss();
        }
        else if (tipoArray.equals("dieta")){
            dietaSeleccionada = (Dieta)adapterView.getItemAtPosition(i);
            escuchador.onMuestraDieta(dietaSeleccionada);
            dismiss();
        }
        else if (tipoArray.equals("plato")){
            platoSeleccionado = (Plato) adapterView.getItemAtPosition(i);
            escuchador.onMuestraPlato(platoSeleccionado);
            dismiss();
        }
        else if (tipoArray.equals("ejercicio")){
            ejercicioSeleccionado = (Ejercicio) adapterView.getItemAtPosition(i);
            escuchador.onMuestraEjercicio(ejercicioSeleccionado);
            dismiss();
        }

    }

    //Callbacks

        public interface DialogMuestrasListener{
            void onMuestraDieta(Dieta dieta);
            void onMuestraRutina(Rutina rutina);
            void onMuestraEjercicio(Ejercicio ejercicio);
            void onMuestraPlato(Plato platos);
        }

        public void setDialogMuestrasListener(DialogMuestrasListener escuchador){
            this.escuchador=escuchador;
        }

}
