package br.pucpr.appdev.recycler.View;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.pucpr.appdev.recycler.Model.City;
import br.pucpr.appdev.recycler.Model.DataStore;
import br.pucpr.appdev.recycler.R;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityHolder> {

    private List<City> cities = DataStore.sharedInstance().getCities();

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycleview_cities, viewGroup, false);

        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityHolder cityHolder, int position) {

        City city = cities.get(position);

        cityHolder.txtName.setText(city.getName());
        cityHolder.txtPeople.setText(String.valueOf(city.getPeople()));
    }

    @Override
    public int getItemCount() {

        return cities.size();
    }

    public class CityHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtPeople;

        public CityHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtPeople = itemView.findViewById(R.id.txtPeople);
        }
    }
}
