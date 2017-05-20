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

import com.example.giner.gymgo.Objetos.Revision_user;
import com.example.giner.gymgo.Objetos.Rutina_Ejercicio;
import com.example.giner.gymgo.R;

import java.util.ArrayList;

public class MuestraRevisionesListView_Dialog extends DialogFragment implements AdapterView.OnItemClickListener{

    //Widgets

        private ListView listaRevisiones;
        private ArrayAdapter<Revision_user> arrayAdapterRevisiones;

    //Variables

        private Revision_user revisionSeleccionada;
        private ArrayList<Revision_user>arrayListRevisiones;
        private DialogRevisionesListView escuchador;

    //Dialogo

        private AlertDialog dialog;

    //Constructor

        public MuestraRevisionesListView_Dialog(ArrayList<Revision_user>revisiones){
            this.arrayListRevisiones=revisiones;
        }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //constuimos el dialogo

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Inflamos la vista del dialogo

        View customDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_muestralistviews,null);

        //Instancio los widgets

        listaRevisiones = (ListView)customDialog.findViewById(R.id.listViewMuestras);

        //Cargo el listView

        builder.setTitle("Estas son tus revisiones:");

        //Instancio el arrayAdapter y le paso el array de rutinas

        arrayAdapterRevisiones = new ArrayAdapter<Revision_user>(getActivity(),android.R.layout.simple_list_item_1,arrayListRevisiones);

        //Le pasamos el arrayAdapter al listView

        listaRevisiones.setAdapter(arrayAdapterRevisiones);

        //ActionListener del listView

        listaRevisiones.setOnItemClickListener(this);

        //Seteo el layout en el dialgo

        builder.setView(customDialog);

        //Boton volver

        builder.setNegativeButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        dialog= builder.create();

        return dialog;

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        revisionSeleccionada = (Revision_user)adapterView.getItemAtPosition(position);
        escuchador.muestraRevisio(revisionSeleccionada);
    }

    //Callbacks

        public interface DialogRevisionesListView{
            void muestraRevisio(Revision_user revision);
        }

        public void setDialogRevisionesListViewListener(DialogRevisionesListView escuchador){
            this.escuchador=escuchador;
        }

}
