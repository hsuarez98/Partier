package com.example.partier.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.partier.Clases.Evento;
import com.example.partier.Clases.Mensaje;
import com.example.partier.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MensajesAdapter extends ArrayAdapter {

    private ArrayList<Mensaje> listaMensajes;

    public MensajesAdapter(Context context, ArrayList<Mensaje> listaMensajes) {
        super(context, R.layout.mensajes_layout, listaMensajes);
        this.listaMensajes = listaMensajes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = LayoutInflater.from(getContext()).inflate(R.layout.mensajes_layout, parent, false);

        TextView txtUser = item.findViewById(R.id.txtUser);
        TextView txtMensaje = item.findViewById(R.id.txtMensaje);

        txtUser.setText(listaMensajes.get(position).getUserCreator());
        txtMensaje.setText(listaMensajes.get(position).getMensaje());

        return item;
    }
}
