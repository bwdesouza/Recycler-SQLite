package br.pucpr.appdev.recycler.Controller;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import br.pucpr.appdev.recycler.Model.City;
import br.pucpr.appdev.recycler.Model.DataStore;
import br.pucpr.appdev.recycler.R;

public class AddEditCity extends AppCompatActivity {

    private TextView txtTitle;
    private EditText txtCity, txtPeople;

    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_city);

        txtTitle = findViewById(R.id.txtTitle);
        txtCity = findViewById(R.id.txtCity);
        txtPeople = findViewById(R.id.txtPeople);

        position = getIntent().getIntExtra("position", -1);

        if (position == -1) {
            txtTitle.setText("Cadastrar Cidade");
        } else {
            txtTitle.setText("Editar Cidade");
            City city = DataStore.sharedInstance().getCity(position);
            txtCity.setText(city.getName());
            txtPeople.setText(String.valueOf(city.getPeople()));
        }
    }

    public void btnCancelOnClick(View v) {

        finish();
    }

    public void btnSaveOnClick(View v) {

        City city = new City(txtCity.getText().toString(), Integer.parseInt(txtPeople.getText().toString()));

        if (position == -1) {
            DataStore.sharedInstance().addCity(city);
        } else {
            DataStore.sharedInstance().editCity(city, position);
        }

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.sendBroadcast(new Intent("updateData"));
        finish();
    }
}
