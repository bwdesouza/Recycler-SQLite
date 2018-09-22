package br.pucpr.appdev.recycler.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.pucpr.appdev.recycler.Model.City;
import br.pucpr.appdev.recycler.Model.DataStore;
import br.pucpr.appdev.recycler.R;
import br.pucpr.appdev.recycler.View.CityAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CityAdapter adapter;
    private GestureDetector gestureDetector;
    private TextView txtPesquisado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPesquisado = findViewById(R.id.txtPesquisar);

        DataStore.sharedInstance().setContext(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Cadastro de Cidades");

        recyclerView = findViewById(R.id.lstCities);
        adapter = new CityAdapter();
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {

            @Override
            public void onLongPress(MotionEvent e) {

                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                int position = recyclerView.getChildAdapterPosition(view);
                DataStore.sharedInstance().removeCity(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                int position = recyclerView.getChildAdapterPosition(view);

                Intent intent = new Intent(MainActivity.this, DetailCity.class);
                intent.putExtra("position", position);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {

                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                int position = recyclerView.getChildAdapterPosition(view);

                Intent intent = new Intent(MainActivity.this, AddEditCity.class);
                intent.putExtra("position", position);
                startActivity(intent);

                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

                View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                return (view != null && gestureDetector.onTouchEvent(motionEvent));
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(updateData, new IntentFilter("updateData"));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing()) {
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
            broadcastManager.unregisterReceiver(updateData);
        }
    }

    private BroadcastReceiver updateData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.mnuAddCity:
                Intent intent = new Intent(MainActivity.this, AddEditCity.class);
                intent.putExtra("position", -1);
                startActivity(intent);
                break;

            case R.id.mnuClear:
                AlertDialog.Builder message = new AlertDialog.Builder(this);
                message.setTitle("Tem certeza que deseja limpar a lista de cidades");
                message.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DataStore.sharedInstance().clearCities();
                        adapter.notifyDataSetChanged();
                    }
                });
                message.setNegativeButton("Não", null);
                message.show();
                break;
        }

        return true;
    }


    public void btnPesquisarOnClick(View v) {

        String textoPesquisado = txtPesquisado.getText().toString();

        DataStore.sharedInstance().pesquisaCities(textoPesquisado);

        adapter.notifyDataSetChanged();
    }
}
