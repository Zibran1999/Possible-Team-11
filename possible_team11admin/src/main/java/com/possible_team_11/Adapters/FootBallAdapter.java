package com.possible_team_11.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.possible_team_11.Models.FootballDataModels.FootBallData;
import com.possible_team_11.R;

import java.util.List;


public class FootBallAdapter extends RecyclerView.Adapter<FootBallAdapter.ViewHolder> {
    List<FootBallData> footBallModelList;
    Context context;
    FootBallInterface listener;

    public FootBallAdapter(List<FootBallData> footBallModelList, Context context, FootBallInterface listener) {
        this.footBallModelList = footBallModelList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.football_layout, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load("https://minutenewsflash.com/admin/posible_team/APIs/Football_Team_Images/" + footBallModelList.get(position).getImage1()).into(holder.teamIcon1);
        Glide.with(context).load("https://minutenewsflash.com/admin/posible_team/APIs/Football_Team_Images/" + footBallModelList.get(position).getImage2()).into(holder.teamIcon2);
        holder.teamName.setText(footBallModelList.get(position).getTeam1Name() + "  VS  " + footBallModelList.get(position).getTeam2Name());
        holder.date.setText(footBallModelList.get(position).getMatchDate());
        holder.desc.setText(footBallModelList.get(position).getMatchDesc());
        holder.time.setText(footBallModelList.get(position).getMatchTime());
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(footBallModelList.get(position)));
    }

    @Override
    public int getItemCount() {
        return footBallModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView teamIcon1, teamIcon2;
        TextView teamName, date, desc,time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teamIcon1 = itemView.findViewById(R.id.team1);
            teamIcon2 = itemView.findViewById(R.id.team2);
            teamName = itemView.findViewById(R.id.teamName);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.football_time);
            desc = itemView.findViewById(R.id.description);


        }
    }
}

