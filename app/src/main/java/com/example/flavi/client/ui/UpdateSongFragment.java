package com.example.flavi.client.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flavi.client.AppContext;
import com.example.flavi.client.R;
import com.example.flavi.client.model.Song;
import com.example.flavi.client.service.SongService;

import java.util.Observable;
import java.util.Observer;

public class UpdateSongFragment extends Fragment implements Observer {

    private SongService songService;
    private EditText title, releaseYear, album;
    private Button updateButton, cancelButton;
    private AppCompatActivity mainActivity;
    private Song song;
    private Observer thisFragment = this;

    public static UpdateSongFragment newInstance() {
        return new UpdateSongFragment();
    }

    public void setMainActivity(AppCompatActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.updateSongFragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.titleEditText);
        releaseYear = view.findViewById(R.id.releaseYearEditText);
        album = view.findViewById(R.id.albumEditText);
        updateButton = view.findViewById(R.id.updateButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        songService = new SongService();
        songService.addObserver(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title.setText(song.getName());
        releaseYear.setText(song.getReleaseYear());
        album.setText(song.getAlbum());
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    song.setName(title.getText().toString());
                    song.setReleaseYear(releaseYear.getText().toString());
                    song.setAlbum(album.getText().toString());
                    songService.updateSong(song);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songService.deleteObserver(thisFragment);
                mainActivity.getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Boolean){
            Boolean response = (Boolean) arg;
            if(response) {
                Toast.makeText(AppContext.getContext(),"Updated song!",Toast.LENGTH_LONG).show();
                songService.deleteObserver(thisFragment);
                mainActivity.getSupportFragmentManager().popBackStack();
            }
        }
    }

    public void setSong(Song song){
        this.song = song;
    }
}
