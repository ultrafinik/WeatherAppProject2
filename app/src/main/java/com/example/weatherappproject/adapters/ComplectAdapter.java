package com.example.weatherappproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherappproject.R;
import com.example.weatherappproject.model.Complect;
import java.util.List;

public class ComplectAdapter extends RecyclerView.Adapter<ComplectAdapter.ViewHolder>
{
    public interface OnComplectClickListener{
        void onComplectClick(Complect state, int position);
    }
    private final OnComplectClickListener onClickListener;
    private LayoutInflater inflater;
    private List<Complect> complects;

    public ComplectAdapter(List<Complect> states, Context context, OnComplectClickListener onClickListener) {
        this.onClickListener = onClickListener;
        inflater=LayoutInflater.from(context);
        this.complects = states;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.complect_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Complect complect = complects.get(position);
        holder.name_complect.setText("Комплект "+(position+1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onComplectClick(complect,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return complects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_complect;
        ViewHolder(View view){
            super(view);
            name_complect = view.findViewById(R.id.name_complect);
        }
    }
}
