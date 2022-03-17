package com.possible_team_11.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.possible_team_11.Models.MatchNewsModels.NewsDatum;
import com.possible_team_11.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    List<NewsDatum> newsModelList;
    Context context;
    NewsAdapterInterface anInterface;

    public NewsAdapter(List<NewsDatum> newsModelList, Context context, NewsAdapterInterface anInterface) {
        this.newsModelList = newsModelList;
        this.context = context;
        this.anInterface = anInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout_show, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load("https://softwaresreviewguides.com/dreamteam11/APIs/Cricket_News_Images/" + newsModelList.get(position).getNewsImg()).into(holder.imageIcon);
        holder.title.setText(newsModelList.get(position).getNewsTitle());
        holder.desc.setText(newsModelList.get(position).getNewsDesc());
        holder.itemView.setOnClickListener(v -> anInterface.onItemClicked(newsModelList.get(position)));

    }

    @Override
    public int getItemCount() {
        return newsModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIcon;
        TextView title, desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.news_icon);
            title = itemView.findViewById(R.id.news_title);
            desc = itemView.findViewById(R.id.news_desc);

        }
    }


}

