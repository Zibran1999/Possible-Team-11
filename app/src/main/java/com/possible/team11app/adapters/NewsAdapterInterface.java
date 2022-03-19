package com.possible.team11app.adapters;


import com.possible.team11app.models.MatchNewsModels.NewsDatum;

public interface NewsAdapterInterface {
    void onItemClicked(NewsDatum newsDatum, int position);
}
