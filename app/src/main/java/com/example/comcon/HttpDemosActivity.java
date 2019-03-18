package com.example.comcon;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class HttpDemosActivity extends AppCompatActivity {

    private HttpLoggingInterceptor logger;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        this.client = new OkHttpClient.Builder().addInterceptor(logger).build();
        setContentView(R.layout.activity_http_demos);
    }


    //Button Listeners
    public void onClickPictureLoad(View view){
        try {
            URL url = new URL("http://10.0.2.2:80/hslu/homer.jpg");
            new HttpPictureTask().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void onClickTextLoad(View view){
        new HttpTextTask().execute();
    }

    public void onClickJson(View view){
        new RetrofitTask().execute();
    }


    //Tasks
    private class HttpPictureTask extends AsyncTask<URL,Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(URL... urls) {
            Request request = new Request.Builder().url("http://10.0.2.2:6666/hslu/homer.jpg").build();
            okhttp3.Response response = null;
            try {
                response = client.newCall(request).execute();
                return BitmapFactory.decodeStream(response.body().byteStream());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }finally {
                if(response != null) {
                    response.close();
                }
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result != null) {
                ImageView imageView = (ImageView) findViewById(R.id.httpPicture);
                imageView.setImageBitmap(result);
            }
        }

    }

    private class HttpTextTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            Request request = new Request.Builder().url("http://10.0.2.2:6666/hslu/test.txt").build();
            okhttp3.Response response= null;
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }finally {
                if(response != null) {
                    response.close();
                }
            }

        }


        protected void onPostExecute(String result) {
            if(result != null) {
                TextView textView = (TextView) findViewById(R.id.httpText);
                textView.setText(result);
            }
        }


    }

    private class RetrofitTask extends AsyncTask<Void, Void, List<AcronymDef>>{

        @Override
        protected List<AcronymDef> doInBackground(Void... voids) {
            Retrofit retrofit = new Retrofit.Builder().client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://www.nactem.ac.uk/software/acromine/")
                    .build();

            AcronymService service = retrofit.create(AcronymService.class);
            Response<List<AcronymDef>> response;
            try {
                response = service.getDefinitionsOf("http").execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        protected void onPostExecute(List<AcronymDef> result) {
            TextView textView = (TextView) findViewById(R.id.jsonText);
            if(result != null) {
                for (AcronymDef item: result) {
                    textView.setText(item.printPretty());
                }
            }
        }

    }



}


