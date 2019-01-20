package com.example.flavi.client.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flavi.client.AppContext;
import com.example.flavi.client.R;


public class SongListFragment extends Fragment {

    RecyclerView songsRecyclerView;
    private SongsViewModel songsViewModel;
    private RecyclerView songList;
    AppCompatActivity mainActivity;
    public boolean ok = false;

    public static SongListFragment newInstance() {
        return new SongListFragment();
    }

    public void setMainActivity(AppCompatActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.songListFragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        songsRecyclerView = view.findViewById(R.id.song_list);
        songsRecyclerView.setHasFixedSize(true);
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(AppContext.getContext()));
        songsViewModel = new SongsViewModel(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshSongs();
    }

    public void refreshSongs(){
        songsViewModel.getSongs().observe(this, songs -> {
            SongListAdapter moviesAdapter = new SongListAdapter(songs,songsRecyclerView,mainActivity);
            songsRecyclerView.setAdapter(moviesAdapter);
        });
    }

    public void sendData(){
        songsViewModel.sendData();
        refreshSongs();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(songsViewModel.sendToServer) {
            sendData();
        }
        ok = true;
    }

}