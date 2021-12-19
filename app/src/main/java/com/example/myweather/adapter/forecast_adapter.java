package com.example.myweather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myweather.R;

import java.util.List;

public class forecast_adapter extends RecyclerView.Adapter<forecast_adapter.ViewHolder> {
    private List<hour> list;

    public forecast_adapter(List<hour> list){
        this.list = list;
    }

    @Override
    public forecast_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_one,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(forecast_adapter.ViewHolder holder, int position) {
        hour h = list.get(position);
        String time = h.getTime();
        //time = time.substring(11,16);
        holder.time.setText(time);
        holder.temperature.setText(h.getTemperature());
        holder.weather.setText(h.getWeather());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView time;
        private TextView weather;
        private TextView temperature;

        public ViewHolder( View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.forecast_time);
            weather = itemView.findViewById(R.id.forecast_weather_condition);
            temperature = itemView.findViewById(R.id.forecast_temperature);
        }
    }
}

