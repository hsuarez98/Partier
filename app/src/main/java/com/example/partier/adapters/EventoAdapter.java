package com.example.partier.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.partier.Clases.Evento;
import com.example.partier.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EventoAdapter extends ArrayAdapter {
    private final ArrayList<Object> listaEventos;
    private final ArrayList<Object> listaOriginal;
    private Evento evento;

    public EventoAdapter(Context context, ArrayList<Object> listaEventos) {
        super(context, R.layout.element, listaEventos);
        this.listaEventos = listaEventos;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaEventos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item;
        if (listaEventos.get(position) instanceof Evento) {

            item = LayoutInflater.from(getContext()).inflate(R.layout.element, parent, false);

            TextView txtLugarfiesta = item.findViewById(R.id.txtLugarfiesta);
            TextView txtCiudadSalida = item.findViewById(R.id.txtCiudadSalida);
            TextView txtHoraSalida = item.findViewById(R.id.txtHoraSalida);
            TextView txtNumPersonas = item.findViewById(R.id.txtNumPersonas);
            TextView txtPrecioGasolina = item.findViewById(R.id.txtPrecioGasolina);

            evento = (Evento) listaEventos.get(position);
            txtLugarfiesta.setText(evento.getLugarFiesta());
            txtCiudadSalida.setText("Ciudad de salida:\n" + evento.getCiudadSalida());
            txtHoraSalida.setText(String.valueOf("Hora de salida:\n" + evento.getHoraSalida()));
            txtNumPersonas.setText(String.valueOf("Sitios disponibles:\n" + evento.getNumPersonas()));
            txtPrecioGasolina.setText(String.valueOf("Aporte para gasolina:\n" + evento.getPrecioGasolina() + "€"));

        } else {

            item = LayoutInflater.from(getContext()).inflate(R.layout.ad_view, parent, false);
            final AdView adView = item.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        return item;
    }


    public void filtrar(String txtSearch) {
        int size = txtSearch.length();
        ArrayList<Evento> eventos = new ArrayList<>();
        if (size == 0) {
            listaEventos.clear();
            listaEventos.addAll(listaOriginal);
        } else {
            for (int i = 0; i < listaEventos.size(); i++) {
                if (listaEventos.get(i) instanceof Evento) {
                    int id = ((Evento) listaEventos.get(i)).getId();
                    String user = ((Evento) listaEventos.get(i)).getUser();
                    String userId = ((Evento) listaEventos.get(i)).getUserId();
                    String lugar = ((Evento) listaEventos.get(i)).getLugarFiesta();
                    String ciudadSalida = ((Evento) listaEventos.get(i)).getCiudadSalida();
                    String provincia = ((Evento) listaEventos.get(i)).getProvincia();
                    int numPersonas = ((Evento) listaEventos.get(i)).getNumPersonas();
                    String hora = ((Evento) listaEventos.get(i)).getHoraSalida();
                    int gasolina = ((Evento) listaEventos.get(i)).getPrecioGasolina();

                    evento = new Evento(id, user, userId, lugar, ciudadSalida, provincia, hora, numPersonas, gasolina);
                    eventos.add(evento);
                }
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Predicate<Evento> discoNameFilter = i -> i.getLugarFiesta().toUpperCase().contains(txtSearch.toUpperCase());

                List<Object> collection = eventos.stream()
                        .filter(discoNameFilter)
                        .collect(Collectors.toList());

                listaEventos.clear();
                listaEventos.addAll(collection);

            } else {
                for (Object e : listaOriginal) {
                    if (evento.getLugarFiesta().toUpperCase().contains(txtSearch.toUpperCase())) {
                        listaEventos.add(e);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filtrarComunidad(String txtSearch) {
        int size = txtSearch.length();
        ArrayList<Evento> eventos = new ArrayList<>();
        if (size == 0) {
            listaEventos.clear();
            listaEventos.addAll(listaOriginal);
        } else {
            for (int i = 0; i < listaEventos.size(); i++) {
                if (listaEventos.get(i) instanceof Evento) {
                    int id = ((Evento) listaEventos.get(i)).getId();
                    String user = ((Evento) listaEventos.get(i)).getUser();
                    String userId = ((Evento) listaEventos.get(i)).getUserId();
                    String lugar = ((Evento) listaEventos.get(i)).getLugarFiesta();
                    String ciudadSalida = ((Evento) listaEventos.get(i)).getCiudadSalida();
                    String provincia = ((Evento) listaEventos.get(i)).getProvincia();
                    int numPersonas = ((Evento) listaEventos.get(i)).getNumPersonas();
                    String hora = ((Evento) listaEventos.get(i)).getHoraSalida();
                    int gasolina = ((Evento) listaEventos.get(i)).getPrecioGasolina();

                    evento = new Evento(id, user, userId, lugar, ciudadSalida, provincia, hora, numPersonas, gasolina);
                    eventos.add(evento);
                }
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Predicate<Evento> provinciaNameFilter = i -> i.getProvincia().toUpperCase().contains(txtSearch.toUpperCase());

                List<Object> collection = eventos.stream()
                        .filter(provinciaNameFilter)
                        .collect(Collectors.toList());

                listaEventos.clear();
                listaEventos.addAll(collection);

            } else {
                for (Object e : listaOriginal) {
                    if (evento.getProvincia().toUpperCase().contains(txtSearch.toUpperCase())) {
                        listaEventos.add(e);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filtrarCiudad(String txtSearch) {
        int size = txtSearch.length();
        ArrayList<Evento> eventos = new ArrayList<>();
        if (size == 0) {
            listaEventos.clear();
            listaEventos.addAll(listaOriginal);
        } else {
            for (int i = 0; i < listaEventos.size(); i++) {
                if (listaEventos.get(i) instanceof Evento) {
                    int id = ((Evento) listaEventos.get(i)).getId();
                    String user = ((Evento) listaEventos.get(i)).getUser();
                    String userId = ((Evento) listaEventos.get(i)).getUserId();
                    String lugar = ((Evento) listaEventos.get(i)).getLugarFiesta();
                    String ciudadSalida = ((Evento) listaEventos.get(i)).getCiudadSalida();
                    String provincia = ((Evento) listaEventos.get(i)).getProvincia();
                    int numPersonas = ((Evento) listaEventos.get(i)).getNumPersonas();
                    String hora = ((Evento) listaEventos.get(i)).getHoraSalida();
                    int gasolina = ((Evento) listaEventos.get(i)).getPrecioGasolina();

                    evento = new Evento(id, user, userId, lugar, ciudadSalida, provincia, hora, numPersonas, gasolina);
                    eventos.add(evento);
                }
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Predicate<Evento> provinciaNameFilter = i -> i.getProvincia().toUpperCase().contains(txtSearch.toUpperCase());

                List<Object> collection = eventos.stream()
                        .filter(provinciaNameFilter)
                        .collect(Collectors.toList());

                listaEventos.clear();
                listaEventos.addAll(collection);

            } else {
                for (Object e : listaOriginal) {
                    if (evento.getProvincia().toUpperCase().contains(txtSearch.toUpperCase())) {
                        listaEventos.add(e);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filtrar(String txtDisco,String txtProvincia,String txtCiudad) {
        int sizeDisco = txtDisco.length();
        int sizeProvincia = txtProvincia.length();
        int sizeCiudad = txtCiudad.length();

        ArrayList<Evento> eventos = new ArrayList<>();
        if (sizeDisco == 0 && sizeProvincia == 0 && sizeCiudad == 0) {
            listaEventos.clear();
            listaEventos.addAll(listaOriginal);
        }else {
            for (int i = 0; i < listaEventos.size(); i++) {
                if (listaEventos.get(i) instanceof Evento) {
                    int id = ((Evento) listaEventos.get(i)).getId();
                    String user = ((Evento) listaEventos.get(i)).getUser();
                    String userId = ((Evento) listaEventos.get(i)).getUserId();
                    String lugar = ((Evento) listaEventos.get(i)).getLugarFiesta();
                    String ciudadSalida = ((Evento) listaEventos.get(i)).getCiudadSalida();
                    String provincia = ((Evento) listaEventos.get(i)).getProvincia();
                    int numPersonas = ((Evento) listaEventos.get(i)).getNumPersonas();
                    String hora = ((Evento) listaEventos.get(i)).getHoraSalida();
                    int gasolina = ((Evento) listaEventos.get(i)).getPrecioGasolina();

                    Evento evento = new Evento(id, user, userId, lugar, ciudadSalida, provincia, hora, numPersonas, gasolina);
                    eventos.add(evento);
                }
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Predicate<Evento> provinciaNameFilter = i -> i.getProvincia().toUpperCase().contains(txtProvincia.toUpperCase());
                Predicate<Evento> discoNameFilter = i -> i.getLugarFiesta().toUpperCase().contains(txtDisco.toUpperCase());
                Predicate<Evento> cityNameFilter = i -> i.getCiudadSalida().toUpperCase().contains(txtCiudad.toUpperCase());

                List<Object> collection = eventos.stream()
                        .filter(provinciaNameFilter)
                        .filter(discoNameFilter)
                        .filter(cityNameFilter)
                        .collect(Collectors.toList());

                listaEventos.clear();
                listaEventos.addAll(collection);

            } else {
                for (Object e : listaOriginal) {
                    if (evento.getProvincia().toUpperCase().contains(txtDisco.toUpperCase())) {
                        listaEventos.add(e);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
