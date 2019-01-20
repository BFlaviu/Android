package com.example.flavi.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.flavi.client.ui.LoginFragment;
import com.example.flavi.client.others.ProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.widget.ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        ProgressBar.setProgressBar(progressBar);

        if (savedInstanceState == null) {
            LoginFragment loginFragment = LoginFragment.newInstance();
            loginFragment.setMainActivity(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, loginFragment)
                    .commitNow();
        }
    }
}
