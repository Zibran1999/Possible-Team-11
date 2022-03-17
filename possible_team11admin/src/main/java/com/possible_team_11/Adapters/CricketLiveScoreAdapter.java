package com.possible_team_11.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.possible_team_11.Models.MatchDetailModels.MatchDatum;
import com.possible_team_11.R;

import java.util.List;


public class CricketLiveScoreAdapter extends RecyclerView.Adapter<CricketLiveScoreAdapter.CricketLiveScoreViewHolder> {
    List<MatchDatum> cricketLiveScoreModelList;
    Context context;
    CricketRvAdapter listener;


    public CricketLiveScoreAdapter(List<MatchDatum> cricketLiveScoreModelList, Context context, CricketRvAdapter listener) {
        this.cricketLiveScoreModelList = cricketLiveScoreModelList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CricketLiveScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cricket_live_score_layout, parent, false);

        return new CricketLiveScoreViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CricketLiveScoreViewHolder holder, int position) {

        Glide.with(context).load("https://softwaresreviewguides.com/dreamteam11/APIs/Cricket_Team_Images/"+cricketLiveScoreModelList.get(position).getImage1()).into(holder.teamIcon1);
        Glide.with(context).load("https://softwaresreviewguides.com/dreamteam11/APIs/Cricket_Team_Images/"+cricketLiveScoreModelList.get(position).getImage2()).into(holder.teamIcon2);
        holder.teamName.setText(cricketLiveScoreModelList.get(position).getTeam1Name() + "  Vs  " + cricketLiveScoreModelList.get(position).getTeam2Name());
        holder.date.setText(cricketLiveScoreModelList.get(position).getMatchDate());
        holder.desc.setText(cricketLiveScoreModelList.get(position).getMatchDesc());
        holder.time.setText(cricketLiveScoreModelList.get(position).getMatchTime());
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(cricketLiveScoreModelList.get(position)));

    }

    @Override
    public int getItemCount() {
        return cricketLiveScoreModelList.size();
    }


    public static class CricketLiveScoreViewHolder extends RecyclerView.ViewHolder {
        ImageView teamIcon1, teamIcon2;
        TextView teamName, date, desc,time;
        CardView cardView;

        public CricketLiveScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            teamIcon1 = itemView.findViewById(R.id.team1);
            teamIcon2 = itemView.findViewById(R.id.team2);
            teamName = itemView.findViewById(R.id.teamName);
            date = itemView.findViewById(R.id.date);
            desc = itemView.findViewById(R.id.description);
            time = itemView.findViewById(R.id.cricket_time);
            cardView = itemView.findViewById(R.id.container);

        }
    }
}