package com.adverticoLTD.avms.ui.siteSelectionView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.Filterable;


import com.adverticoLTD.avms.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAdapter extends BaseAdapter implements Filterable {

    List<String> arrayList;
    List<String> mOriginalValues; // Original Values
    LayoutInflater inflater;
    HashMap<String, Boolean> selectedHashMap;

    public MyAdapter(Context context, List<String> arrayList,
                     HashMap<String, Boolean> selectedHashMap) {
        this.arrayList = arrayList;
        this.selectedHashMap = selectedHashMap;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        CheckedTextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.simple_list_item_view, null);
            holder.textView = (CheckedTextView) convertView
                    .findViewById(R.id.checkedTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(arrayList.get(position));

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedHashMap.clear();
                selectedHashMap.put(arrayList.get(position),true);
                notifyDataSetChanged();
            }
        });

        if (selectedHashMap.containsKey(arrayList.get(position)) &&
                selectedHashMap.get(arrayList.get(position))) {
            holder.textView.setChecked(true);
        } else {
            holder.textView.setChecked(false);
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                arrayList = (List<String>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<String> FilteredArrList = new ArrayList<String>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<String>(arrayList); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i);
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}