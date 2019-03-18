package com.example.comcon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
    }

    //Button Listeners
    public void onClickHttpServerCommunication(final View Btn){
        Intent intent = new Intent(this, HttpDemosActivity.class);
        startActivity(intent);
    }

    public void onClickDemoThread(View Btn){
        Button button = (Button)findViewById(R.id.demoThreadButton);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                button.setText("DemoThread Läuft...");
                try {
                    Thread.sleep(7000);
                    button.setText("DemoThread starten");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void onClickThreadUIFreez(View Btn){
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onCLickMultiAsyncTask(View Btn) {
        MultiAsyncTask multiAsyncTask = new MultiAsyncTask();
        URL[] urls = new URL[5];
        try {
            urls[0] = new URL("http://wherever.ch/hslu/title0.txt");
            urls[1] = new URL("http://wherever.ch/hslu/title1.txt");
            urls[2] = new URL("http://wherever.ch/hslu/title2.txt");
            urls[3] = new URL("http://wherever.ch/hslu/title3.txt");
            urls[4] = new URL("http://wherever.ch/hslu/title4.txt");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        multiAsyncTask.execute(urls);

    }

    public void onClickStartDemoTask(View Btn){
       new AsyncTaskDemo().execute(12,33,55,77);
    }


    //Tasks
    public class MultiAsyncTask extends AsyncTask<URL, String, Void> {

        private String[] allMovies = new String[10];

        @Override
        protected Void doInBackground(URL... urls) {
            HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
            logger.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logger).build();
            int i = 0;
            for(URL url: urls){
                Request request = new Request.Builder().url(url).build();
                Response response = null;
                try{
                    response = client.newCall(request).execute();
                    String responseString = response.body().string();
                    allMovies[i] = responseString;
                    publishProgress(responseString);
                    Thread.sleep(2000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values){
            Context context = getApplicationContext();
            CharSequence text = values[0];
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        @Override
        protected void onPostExecute(Void result){

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle("Film Titel");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                stringBuilder.append(allMovies[i]);
                stringBuilder.append("\n");
            }
            dialogBuilder.setMessage(stringBuilder.toString());
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        }

    }

    public class AsyncTaskDemo extends AsyncTask<Integer, Void, String>{

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resultat = Die Parameter waren: ");
            for (Integer i:integers) {
                stringBuilder.append(i);
                stringBuilder.append(",");
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String result){
            Context context = getApplicationContext();
            CharSequence text = result;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }


    }
