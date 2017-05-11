package com.example.giner.gymgo.Gymgo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.giner.gymgo.Objetos.Dieta;
import com.example.giner.gymgo.Objetos.Rutina;
import com.example.giner.gymgo.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Muestras_Dialog extends DialogFragment implements AdapterView.OnItemClickListener{

    //Widgets

        private ListView listaDeMuestras;
        private ArrayAdapter<Rutina> arrayAdapterRutinas;
        private ArrayAdapter<Dieta>arrayAdapterDietas;

    //Variables

        String tipoArray="";
        Dieta dietaSeleccionada;
        Rutina rutinaSeleccionada;
        private DialogMuestrasListener escuchador;

    //Arrays

        ArrayList<Rutina>rutinas;
        ArrayList<Dieta>dietas;

    //Dialogo

        private AlertDialog dialog;

    //Constructores

        public Muestras_Dialog(ArrayList<Rutina>rutinas,ArrayList<Dieta>dietas){
            if(dietas==null) {
                this.rutinas = rutinas;
                tipoArray = "rutina";
            }
            else if(rutinas==null) {
                this.dietas = dietas;
                tipoArray = "dietas";
            }
        }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Construyo y devuelvo el diálogo
        //Primero genero un constructor de diálogos de Alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Selecciona una "+tipoArray);
        View customDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_muestras, null);

        //Instancio los widgets

            listaDeMuestras = (ListView)customDialog.findViewById(R.id.listViewMuestras);

        //Cargo el listView

            if(listaDeMuestras!=null){

                if((this.rutinas!=null)&&(arrayAdapterRutinas==null)){

                    //Instancio el arrayAdapter y le paso el array de rutinas

                    arrayAdapterRutinas = new ArrayAdapter<Rutina>(getActivity(),android.R.layout.simple_list_item_1,rutinas);

                    //Le paso el arrayAdpater al listView

                    listaDeMuestras.setAdapter(arrayAdapterRutinas);

                }

                else if((this.dietas!=null)&&(arrayAdapterDietas==null)){

                    //Instancio el arrayAdapter y le paso el array de dietas

                    arrayAdapterDietas = new ArrayAdapter<Dieta>(getActivity(),android.R.layout.simple_list_item_1,dietas);

                    //Le paso el arrayAdpater al listView

                    listaDeMuestras.setAdapter(arrayAdapterDietas);

                }

                else{
                    Toasty.info(getActivity(),"Entra aqui", Toast.LENGTH_SHORT).show();
                }

            }

        //ActionListener del listView

            listaDeMuestras.setOnItemClickListener(this);

        //Seteo el layout en el diálogo

        builder.setView(customDialog);

        //Hago que el diálogo no sea cancelable pulsando fuera de él (diálogo modal)

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

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

    }

    //Callbacks

        public interface DialogMuestrasListener{
            void onMuestraDieta(Dieta dieta);
            void onMuestraRutina(Rutina rutina);
        }

        public void setDialogMuestrasListener(DialogMuestrasListener escuchador){
            this.escuchador=escuchador;
        }

}
