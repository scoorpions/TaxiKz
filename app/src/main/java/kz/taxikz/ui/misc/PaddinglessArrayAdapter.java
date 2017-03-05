package kz.taxikz.ui.misc;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import kz.taxikz.utils.DeviceUtil;

public class PaddinglessArrayAdapter<T> extends ArrayAdapter<T> {
    public PaddinglessArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public PaddinglessArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public PaddinglessArrayAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
    }

    public PaddinglessArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public PaddinglessArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
    }

    public PaddinglessArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setPadding(DeviceUtil.convertDpToPx(getContext(), 2), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        return view;
    }
}
