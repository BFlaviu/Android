package com.example.flavi.client.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flavi.client.R;
import com.example.flavi.client.model.Song;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {

    private Context context;
    private List<Song> songs;
    private final View.OnClickListener onClickListener = new ListItemClickListener();
    private RecyclerView recyclerView;
    private AppCompatActivity parent;

    public SongListAdapter(List<Song> songsList, RecyclerView recyclerView, AppCompatActivity parent) {
        this.songs = songsList;
        this.recyclerView = recyclerView;
        this.parent = parent;
    }


    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.songFragment, viewGroup, false);
        view.setOnClickListener(onClickListener);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        TextView titleTextView = (TextView) SongViewHolder.constraintLayout.getViewById(R.id.titleTextView);
        titleTextView.setText(song.getName());
        TextView ratingTextView = (TextView) SongViewHolder.constraintLayout.getViewById(R.id.albumTextView);
        String ratingString = "Album: " + song.getAlbum();
        ratingTextView.setText(ratingString);
        TextView releaseYearTextView = (TextView) SongViewHolder.constraintLayout.getViewById(R.id.releaseYearTextView);
        String releaseYearString = "Release year: " + song.getReleaseYear();
        releaseYearTextView.setText(releaseYearString);


    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public static ConstraintLayout constraintLayout;
        public SongViewHolder(ConstraintLayout layout) {
            super(layout);
            constraintLayout = layout;
        }
    }

    class ListItemClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            int itemPosition = recyclerView.getChildLayoutPosition(view);
            UpdateSongFragment updateSongFragment = UpdateSongFragment.newInstance();
            updateSongFragment.setMainActivity(parent);
            updateSongFragment.setSong(songs.get(itemPosition));
            parent.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, updateSongFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}