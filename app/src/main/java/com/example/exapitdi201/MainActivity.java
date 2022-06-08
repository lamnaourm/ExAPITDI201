package com.example.exapitdi201;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t = findViewById(R.id.t);
        ReadAPiPays p = new ReadAPiPays();
        p.execute("https://gorest.co.in/public/v2/users");
    }

    class ReadAPiPays extends AsyncTask<String, Void,String> {

        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("En cours de chargement");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progress.dismiss();

            String res = "";
            try {
                JSONArray arr = new JSONArray(s);

                for(int i=0;i<arr.length();i++){
                    res += arr.getJSONObject(i).getString("name") + "\n";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            t.setText(res);
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder res=new StringBuilder();
            try {
                String url = strings[0];
                URL Myurl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)Myurl.openConnection();
                connection.setRequestMethod("GET");

                InputStreamReader input = new InputStreamReader(connection.getInputStream());
                BufferedReader buffer = new BufferedReader(input);
                String line="";

                while((line=buffer.readLine())!=null){
                    res.append(line);
                }

                input.close();
                buffer.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return res.toString();
        }
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}