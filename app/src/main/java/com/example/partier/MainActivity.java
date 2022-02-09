package com.example.partier;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.partier.Clases.Evento;
import com.example.partier.adapters.EventoAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Evento");

    ArrayList<Object> listaEventos = new ArrayList<>();

    Evento[] evento = new Evento[1000];
    String idChat1, idChat2;
    EventoAdapter adapter;

    // A banner ad is placed in every 8th position in the RecyclerView.
    public static final int ITEMS_PER_AD = 4;

    // OFICIAL PUBLICAR // private static final String AD_UNIT_ID = "ca-app-pub-9024271567416953/1626558854";
    // ID PROBAR // private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";

    private RelativeLayout frontLayout;
    private LinearLayout backLayout, backLayout2;
    private RelativeLayout.LayoutParams lp;
    boolean showBackLayout = false;
    boolean showBackLayout2 = false;
    private ListView listView;
    MenuItem itemCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logopartiername);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listaMensajes);
        TextInputEditText eventSearch = findViewById(R.id.eventSearch);
        TextInputEditText comunitySearch = findViewById(R.id.comunitySearch);
        TextInputEditText citySearch = findViewById(R.id.citySearch);

        frontLayout = findViewById(R.id.front_layout);
        backLayout = findViewById(R.id.back_layout);
        backLayout2 = findViewById(R.id.back_layout2);

        //Obtener eventos i llenar lista
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getDatosFirebase(snapshot);
                addBannerAds();
                loadBannerAds();
                adapter = new EventoAdapter(MainActivity.this, listaEventos);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Iniciar chat
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            idChat1 = FirebaseAuth.getInstance().getCurrentUser().getUid() + evento[i].getUserId();
            idChat2 = evento[i].getUserId() + FirebaseAuth.getInstance().getCurrentUser().getUid();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Enviar Mensaje");
            builder.setMessage(R.string.request_message);
            builder.setPositiveButton("Si", (dialog, id) -> {

                String confId1 = FirebaseDatabase.getInstance().getReference().child("Chats").child(idChat1).getKey();
                String confId2 = FirebaseDatabase.getInstance().getReference().child("Chats").child(idChat2).getKey();

                int comp = idChat1.compareTo(idChat2);

                String chatID;
                if (comp > 0) {
                    chatID = idChat1 + '_' + idChat2;
                } else {
                    chatID = idChat2 + '_' + idChat1;
                }

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                if (idChat1.equals(confId1) && idChat2.equals(confId2)) {

                    intent.putExtra("idChat1", chatID);
                    intent.putExtra("evento", evento[i].getUser());
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);

                } else {

                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);

                }
            });
            builder.setNegativeButton("No", (dialogInterface, i1) -> {

            });
            builder.show();
        });

        //adapter.filtrar(comunitySearch.getText().toString(),eventSearch.getText().toString(),citySearch.getText().toString());

        //// scroll lista main filtros /////
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (mLastFirstVisibleItem < firstVisibleItem) {
                    // Scrolling down
                    dropLayoutDown();
                }
                if (mLastFirstVisibleItem > firstVisibleItem) {
                    // scrolling up
                    dropLayoutUp();
                }
                mLastFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    private void dropLayoutDown() {
        backLayout.setVisibility(View.GONE);
        backLayout2.setVisibility(View.VISIBLE);
    }

    private void dropLayoutUp() {
        backLayout.setVisibility(View.VISIBLE);
        backLayout2.setVisibility(View.GONE);
    }
    //// scroll lista main filtros /////


    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filtrar(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String textSearch) {
        adapter.filtrar(textSearch);
        return false;
    }
    ///* CardVIew Filtros *///

    @Override
    protected void onResume() {
        for (Object item : listaEventos) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.resume();
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        for (Object item : listaEventos) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.pause();
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        for (Object item : listaEventos) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.destroy();
            }
        }
        super.onDestroy();
    }

    ///  ADS ///

    /**
     * Adds banner ads to the items list.
     */
    private void addBannerAds() {
        // Loop through the items array and place a new banner ad in every ith position in
        // the items List.
        for (int i = 4; i <= listaEventos.size(); i += ITEMS_PER_AD) {
            View item = LayoutInflater.from(this).inflate(R.layout.ad_view, null);
            final AdView adView = item.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            listaEventos.add(i, adView);
        }
    }

    /**
     * Sets up and loads the banner ads.
     */
    private void loadBannerAds() {
        // Load the first banner ad in the items list (subsequent ads will be loaded automatically
        // in sequence).
        loadBannerAd(4);
    }

    /**
     * Loads the banner ads in the items list.
     */
    private void loadBannerAd(final int index) {

        if (index >= listaEventos.size()) {
            return;
        }

        Object item = listaEventos.get(index);
        if (!(item instanceof AdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a banner ad"
                    + " ad.");
        }

        final AdView adView = (AdView) item;

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(
                new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        // The previous banner ad loaded successfully, call this method again to
                        // load the next ad in the items list.
                        loadBannerAd(index + ITEMS_PER_AD);
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // The previous banner ad failed to load. Call this method again to load
                        // the next ad in the items list.
                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        Log.e(
                                "MainActivity",
                                "The previous banner ad failed to load with error: "
                                        + error
                                        + ". Attempting to"
                                        + " load the next banner ad in the items list.");
                        loadBannerAd(index + ITEMS_PER_AD);
                    }
                });

        // Load the banner ad.
        adView.loadAd(new AdRequest.Builder().build());
    }
    ///* ADS *///

    /// FATOS FIREBASE ///
    public void getDatosFirebase(@NonNull DataSnapshot snapshot) {
        int i = 0;
        for (DataSnapshot ds : snapshot.getChildren()) {
            GenericTypeIndicator<Evento> t = new GenericTypeIndicator<Evento>() {
            };
            evento[i] = ds.getValue(t);
            listaEventos.add(evento[i]);

            i++;
        }
    }

    //Acciones iconos app bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addBtn:
                // User chose the "add" item, show the add event page...
                Intent intent = new Intent(this, RegistroEvento.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                return true;

            case R.id.msgBtn:
                // User chose the "Message" action, open the mensage box
                Intent intentMensajes = new Intent(this, MensajesActivity.class);
                startActivity(intentMensajes);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                return true;
            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Cerrar Sesión");
                builder.setMessage(R.string.request_log_out);
                builder.setPositiveButton("Si", (dialog, id) -> AuthUI.getInstance().signOut(MainActivity.this)
                        .addOnCompleteListener(task -> {
                            MainActivity.this.finish();
                        }));
                builder.setNegativeButton("No", (dialogInterface, i) -> {

                });
                builder.show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                getMenuInflater().inflate(R.menu.main_activity_menu, menu);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                getMenuInflater().inflate(R.menu.main_activity_menu_light, menu);
                break;
        }
        return true;
    }
}