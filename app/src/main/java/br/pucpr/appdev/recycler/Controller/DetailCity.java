package br.pucpr.appdev.recycler.Controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import br.pucpr.appdev.recycler.Model.City;
import br.pucpr.appdev.recycler.Model.DataStore;
import br.pucpr.appdev.recycler.R;

public class DetailCity extends AppCompatActivity {

    private TextView txtCity, txtPeople;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_city);

        txtCity = findViewById(R.id.txtCity);
        txtPeople = findViewById(R.id.txtPeople);

        position = getIntent().getIntExtra("position", -1);

        if (position > -1) {

            City city = DataStore.sharedInstance().getCity(position);
            txtCity.setText(city.getName());
            txtPeople.setText(String.valueOf(city.getPeople()));
        }
    }

    public void btnEditOnClick(View v) {

        Intent intent = new Intent(this, AddEditCity.class);
        intent.putExtra("position", position);
        startActivity(intent);
        finish();
    }

    public void btnRemoveOnClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tem certeza que deseja excluir esta cidade?");
        builder.setNegativeButton("Não", null);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataStore.sharedInstance().removeCity(position);
                LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(DetailCity.this);
                broadcastManager.sendBroadcast(new Intent("updateData"));
                DetailCity.this.finish();
            }
        });
        builder.show();
    }
}
