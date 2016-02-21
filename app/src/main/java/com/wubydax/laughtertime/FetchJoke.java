package com.wubydax.laughtertime;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.wubydax.backend.myApi.MyApi;
import com.wubydax.jokedisplayinglibrary.DisplayJokeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Anna Berkovitch on 21/02/2016 for Udacity requirements.
 * Part of Build-It_bigger project
 */
public class FetchJoke extends AsyncTask<Void, Void, String> {
    Context c;
    private MyApi myApi;
    private ProgressDialog progressDialog;
    FetchJokeListener fetchJokeListener = null;


    public FetchJoke(Context context) {
        c = context;
    }

    @Override
    protected void onPreExecute() {
        if (c != c.getApplicationContext()) {
            progressDialog = ProgressDialog.show(c, c.getString(R.string.progress_dialog_title), c.getString(R.string.progress_dialog_message), true, false);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        String finalJoke = "";
        if (myApi == null) {
            myApi = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(BuildConfig.BASE_ROOT_URL + "/_ah/api/")
                    .build();
            String joke = getRandomJoke();
            try {
                finalJoke = myApi.getJoke(joke).execute().getJoke();
            } catch (IOException e) {
                finalJoke = e.getMessage();
            }
        }
        return finalJoke;
    }

    private String getRandomJoke() {
        String randomJoke = "";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {

            URL url = new URL(c.getString(R.string.random_jokes_url));

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                    buffer.append("\n");
                }


                randomJoke = buffer.toString();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return getJokeFromJson(randomJoke);
    }

    private String getJokeFromJson(String jsonString) {
        String finalJoke;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            finalJoke = jsonObject.getString("joke");
        } catch (JSONException e) {
            finalJoke = "Joke not found";
        }
        return finalJoke;
    }

    @Override
    protected void onPostExecute(String s) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (fetchJokeListener != null) {
            this.fetchJokeListener.onComplete(s);
        }
        startJokeShowingActivity(s);
    }

    private void startJokeShowingActivity(String joke) {
        Intent openJokeDisplay = new Intent(c, DisplayJokeActivity.class);
        Bundle extras = new Bundle();
        extras.putString("JOKE_STRING", joke);
        openJokeDisplay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        openJokeDisplay.putExtras(extras);
        c.startActivity(openJokeDisplay);
    }

    public FetchJoke setListener(FetchJokeListener listener) {
        this.fetchJokeListener = listener;
        return this;
    }

    public interface FetchJokeListener {
        void onComplete(String result);
    }
}

