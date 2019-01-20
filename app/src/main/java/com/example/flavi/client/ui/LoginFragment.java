package com.example.flavi.client.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flavi.client.AppContext;
import com.example.flavi.client.MainActivity;
import com.example.flavi.client.R;
import com.example.flavi.client.service.LoginService;

import java.util.Observable;
import java.util.Observer;

public class LoginFragment extends Fragment implements Observer {

    private Context context;
    private Button loginButton;
    private EditText username,password;
    private LoginService loginService;
    private MainActivity activity;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public void setMainActivity(MainActivity mainActivity){
        this.activity = mainActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loginFragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = view.findViewById(R.id.loginButton);
        username = view.findViewById(R.id.usernameEditText);
        password = view.findViewById(R.id.passwordEditText);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginService = new LoginService();
        loginService.addObserver(this);
        context = AppContext.getContext();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginService.login(username.getText().toString(), password.getText().toString());
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Boolean){
            Boolean response = (Boolean) arg;
            if(response){
                loginService.deleteObserver(this);
                SongListFragment songListFragment = SongListFragment.newInstance();
                songListFragment.setMainActivity(activity);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,songListFragment);
                transaction.commit();
            }
            else{
                Toast.makeText(context,"Username sau parola incorecta!",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
