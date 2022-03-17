package com.possible_team_11.Models.MatchDetailModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MatchViewModelFactory implements ViewModelProvider.Factory {
    Application application;
    String id;

    public MatchViewModelFactory(Application application, String id) {
        this.application = application;
        this.id = id;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MatchViewModel(application, id);
    }
}
