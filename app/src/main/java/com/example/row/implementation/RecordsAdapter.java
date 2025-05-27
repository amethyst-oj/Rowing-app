package com.example.row.implementation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.row.R;
import com.example.row.RecordDetailsActivity;
import com.example.row.RecordsActivity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
//for implementing recycleview, records list
public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {
    private final Records localDataSet;
    private ArrayList<SingleRecord> recList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton rowButton;
        private final TextView timeText;
        private final TextView dateText;
        private final TextView distText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowButton = itemView.findViewById(R.id.row_button);
            timeText = itemView.findViewById(R.id.specific_time);
            dateText = itemView.findViewById(R.id.specific_date);
            distText = itemView.findViewById(R.id.specific_dist);
        }
        public void bind(String timeData, String dateData, String distData) {
            timeText.setText(timeData);
            dateText.setText(dateData);
            distText.setText(distData);

            rowButton.setOnClickListener(v -> {
                Intent toDetails = new Intent(v.getContext(), RecordDetailsActivity.class);  // go to record details page
                toDetails.putExtra("distance", distData);
                toDetails.putExtra("date", dateData);
                String safeTime = LocalTime.parse(timeData).withNano(0).toString();
                toDetails.putExtra("time", safeTime);
                v.getContext().startActivity(toDetails);
            });
        }
    }

    public RecordsAdapter(Records dataSet) {
        localDataSet = dataSet;
        this.recList = new ArrayList<>(dataSet.getRecordsChronologically());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SingleRecord cur = recList.get(position);
        LocalDateTime rel = cur.getDateTime();

        holder.bind(rel.toLocalTime().toString(), rel.toLocalDate().toString(), Integer.toString(cur.getDistance()));
    }

    @Override
    public int getItemCount() {
        return recList.size();
    }

    public void refresh() {
        recList.clear();
        recList.addAll(localDataSet.getRecordsChronologically());
        notifyDataSetChanged();
    }
}
