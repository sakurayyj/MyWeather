package com.example.myweather.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweather.CityActivity;
import com.example.myweather.MainActivity;
import com.example.myweather.R;
import com.example.myweather.database.MyDatabaseHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.myweather.CityActivity.C_city_id;
import static com.example.myweather.CityActivity.C_city_name;
import static com.example.myweather.CityActivity.C_flag;
import static com.example.myweather.CityActivity.C_temperature;
import static com.example.myweather.CityActivity.C_weather;

public class city_adapter extends RecyclerView.Adapter<city_adapter.ViewHolder> {

    private List<city> list;

    private CityActivity activity;

    private OnItemClickListener mOnItemClickListener;

    private MyDatabaseHelper helper;

    public city_adapter(List<city> list,Context context){
        this.list = list;
        activity = (CityActivity) context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_four,parent,false);
        //mContext = parent.getContext();
        return new city_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        city c = list.get(position);
        holder.cityName.setText(c.getName());
        holder.cityWeather.setText(c.getWeather());
        holder.cityTemp.setText(c.getTemp());
        holder.cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                C_flag = c.getFlag();
                C_city_name =  c.getName();
                C_temperature = c.getTemp();
                C_city_id = c.getId();
                C_weather = c.getWeather();
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("city_id",C_city_id);
                intent.putExtra("flag",C_flag);
                intent.putExtra("city_name",C_city_name);
                Log.d("CityActivity","city_id="+C_city_id);
                activity.setResult(RESULT_OK,intent);
                //activity.startActivity(intent);
                activity.finish();
            }
        });
        holder.cityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper = new MyDatabaseHelper(activity,"CityStore.db",null,1);
                SQLiteDatabase database = helper.getWritableDatabase();
                database.delete("City2","cityName = ?",new String[]{c.getName()});
                try{
                    list.remove(position);
                    Log.d("remove",position+"");
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(activity, "删除失败", Toast.LENGTH_SHORT).show();
                }
                //notifyItemRangeChanged(position,list.size()-position);
                //notifyItemRemoved(position);
                Log.d("remove",position+"");
                CityActivity ca = new CityActivity();
                ca.refresh();
                //删除
                //notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public static TextView cityName;
        public static TextView cityTemp;
        public static TextView cityWeather;
        public static TextView cityBack;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.store_city_name);
            cityTemp = itemView.findViewById(R.id.store_temp);
            cityWeather = itemView.findViewById(R.id.store_weather);
            cityBack = itemView.findViewById(R.id.delete_inf);
        }
    }

    //  删除数据
    public void removeData(int position) {
        list.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

}
