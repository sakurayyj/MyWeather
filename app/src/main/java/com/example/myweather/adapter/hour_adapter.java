package com.example.myweather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweather.R;

import java.util.List;

public class hour_adapter extends RecyclerView.Adapter<hour_adapter.ViewHolder> {

    private List<hour> list;

    public hour_adapter(List<hour> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_two,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(hour_adapter.ViewHolder holder, int position) {
        hour h = list.get(position);
        String time = h.getTime();
        time = time.substring(11,16);
        holder.time.setText(time);
        holder.temperature.setText(h.getTemperature() + "°");
        holder.weather.setText(h.getWeather());
        holder.photo.setImageResource(h.getId());
        //holder.wind_dir_today.setText(wth_24hour.getWindDir());
        //holder.wind_series_today.setText(wth_24hour.getWindScale());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView time;
        private TextView weather;
        private TextView temperature;
        private ImageView photo;

        public ViewHolder( View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.current_time);
            weather = itemView.findViewById(R.id.weather_condition);
            temperature = itemView.findViewById(R.id.temperature);
            photo = itemView.findViewById(R.id.weather_photo);
        }
    }
}
