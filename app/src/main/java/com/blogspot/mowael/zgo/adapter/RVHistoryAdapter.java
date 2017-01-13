package com.blogspot.mowael.zgo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.mowael.zgo.R;
import com.blogspot.mowael.zgo.dataModel.MeasureData;

import java.util.ArrayList;

/**
 * Created by moham on 1/13/2017.
 */

public class RVHistoryAdapter extends RecyclerView.Adapter<RVHistoryAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<MeasureData> measureDataArrayList;

    public RVHistoryAdapter(Context context, ArrayList<MeasureData> measureDataArrayList) {
        this.context = context;
        this.measureDataArrayList = measureDataArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_row_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MeasureData data = measureDataArrayList.get(position);
        holder.tvFrom.setText(data.getOrigin());
        holder.tvTo.setText(data.getDestination());
        holder.tvResultOfDirectDistance.setText(data.getTravelDistance());
        holder.tvDurationValue.setText(data.getDuration());
        holder.tvDurationInTrafficValue.setText(data.getDurationInTraffic());
    }

    @Override
    public int getItemCount() {
        return measureDataArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFrom, tvTo, tvResultOfDirectDistance, tvDurationValue, tvDurationInTrafficValue;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvFrom = (TextView) itemView.findViewById(R.id.tvFrom);
            tvTo = (TextView) itemView.findViewById(R.id.tvTo);
            tvResultOfDirectDistance = (TextView) itemView.findViewById(R.id.tvResultOfDirectDistance);
            tvDurationValue = (TextView) itemView.findViewById(R.id.tvDurationValue);
            tvDurationInTrafficValue = (TextView) itemView.findViewById(R.id.tvDurationInTrafficValue);
        }
    }
}
