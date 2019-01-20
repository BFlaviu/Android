package com.example.flavi.client.service;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.flavi.client.AppContext;
import com.example.flavi.client.api.LoginResource;
import com.example.flavi.client.database.LocalDatabase;
import com.example.flavi.client.database.UserDatabase;
import com.example.flavi.client.model.User;
import com.example.flavi.client.others.ProgressBar;
import com.example.flavi.client.others.Internet;

import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginService extends Observable {

    private Boolean loginResponse = false;
    public static Boolean offline = false;
    private LocalDatabase localDb;
    private UserDatabase userDatabase;
    public static String token = "";

    public LoginService() {
        startLocalDb();
    }

    public void startLocalDb() {
        localDb = LocalDatabase.getDatabase(AppContext.getContext());
        userDatabase = localDb.userDatabase();
    }

    public void login(final String username, final String password) {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();


        final Context context = AppContext.getContext();
        if(Internet.checkForInternet()) {
            ProgressBar.startProgressBar();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(LoginResource.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            LoginResource api = retrofit.create(LoginResource.class);
            Call<Void> call = api.login(new User(username, password));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    ProgressBar.stopProgressBar();
                    loginResponse = response.code() == 200;
                    if (loginResponse) {
                        Headers headers = response.headers();
                        token = headers.get("Auth");
                        if (!checkUserDb(username, password)) {
                            new addTask(userDatabase).execute(new User(username, password));
                        }
                    }
                    offline = false;
                    setChanged();
                    notifyObservers(loginResponse);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    ProgressBar.stopProgressBar();
                    Toast.makeText(context, "Eroare server,offline login!", Toast.LENGTH_LONG).show();
                    offline(username, password);
                }

            });
        }
        else{
            Toast.makeText(context, "Internetul nu este pornit, offline login!", Toast.LENGTH_LONG).show();
            offline(username,password);
        }
    }

    private Boolean checkUserDb(final String username, final String password){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Integer> callable = new Callable<Integer>() {
            public Integer call() {
                return userDatabase.checkLogin(username,password);
            }
        };
        Future<Integer> future = executor.submit(callable);
        try {
            return future.get().equals(1);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static class addTask extends AsyncTask<User, Void, Void> {

        private UserDatabase userTask;

        addTask(UserDatabase userDb) {
            userTask = userDb;
        }

        @Override
        protected Void doInBackground(final User... params) {
            userTask.insert(params[0]);
            return null;
        }
    }

    private void offline(final String username, final String password){
        final Context context = AppContext.getContext();
        if(checkUserDb(username,password)){
            loginResponse = true;
            offline = true;
            setChanged();
            notifyObservers(loginResponse);
        }
        else{
            Toast.makeText(context,"Username sau parola incorecta!",Toast.LENGTH_SHORT).show();
        }
    }

}


