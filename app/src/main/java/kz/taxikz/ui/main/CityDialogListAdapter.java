package kz.taxikz.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kz.taxikz.taxi4.R;
import kz.taxikz.ui.widget.item.City;

public class CityDialogListAdapter extends ArrayAdapter<City> {
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onCitySelected(City city);
    }

    public CityDialogListAdapter(Context context, List<City> items, Callbacks callbacks) {
        super(context, R.layout.item_dialog_city_list, items);
        this.mCallbacks = callbacks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City city = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dialog_city_list, parent, false);
        }
        TextView tvCityName = (TextView) convertView.findViewById(R.id.city_name);
        tvCityName.setText(city.getName());
        tvCityName.setOnClickListener(v -> {
            if (this.mCallbacks != null) {
                this.mCallbacks.onCitySelected(city);
            }
        });
        return convertView;
    }
}
