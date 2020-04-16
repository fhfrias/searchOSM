package com.example.searchopenstreetmaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<ModelLocation> {
    private final String MY_DEBUG_TAG = "CustomerAdapter";
    private ArrayList<ModelLocation> items;
    private ArrayList<ModelLocation> itemsAll;
    private ArrayList<ModelLocation> suggestions;
    private int viewResourceId;

    public CustomAdapter(Context context, int viewResourceId, ArrayList<ModelLocation> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<ModelLocation>) items.clone();
        this.suggestions = new ArrayList<ModelLocation>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        ModelLocation modelLocation = items.get(position);
        if (modelLocation != null) {
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView ubic = (TextView) v.findViewById(R.id.cityState);
            if (name != null) {
                name.setText(modelLocation.getName());
                ubic.setText(modelLocation.getCity() + ", " + modelLocation.getState());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((ModelLocation)(resultValue)).getName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (ModelLocation modelLocation : itemsAll) {
                    if(modelLocation.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(modelLocation);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<ModelLocation> filteredList = (ArrayList<ModelLocation>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (ModelLocation c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}
