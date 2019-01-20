package com.example.flavi.client.others;

import android.view.View;

public class ProgressBar {

    private ProgressBar(){}

    private static android.widget.ProgressBar progressBar;

    public static void setProgressBar(android.widget.ProgressBar progressBar){
        ProgressBar.progressBar = progressBar;
    }

    public static void startProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
    }

    public static void stopProgressBar(){
        progressBar.setVisibility(View.GONE);
    }

}
