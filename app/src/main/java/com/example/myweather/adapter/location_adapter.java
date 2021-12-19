package com.example.myweather.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweather.LocationActivity;
import com.example.myweather.MainActivity;
import com.example.myweather.R;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class location_adapter extends ArrayAdapter<location> {
    private List<location> list;

    private Context context;

    private int resourceId;

    public location_adapter(@NonNull Context context, int resource, @NonNull List<location> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        location l = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder= new ViewHolder();
            viewHolder.textView = view.findViewById(R.id.get_location);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(l.getLocation());
        return view;

    }

    class ViewHolder {
        TextView textView;
    }
}
