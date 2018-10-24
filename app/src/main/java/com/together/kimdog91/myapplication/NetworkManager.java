package com.together.kimdog91.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkManager extends BaseActivity {

    private final static String serverIP = "http://13.125.71.127:54705";

    private JSONObject sendJsonObject = null;
    private JSONArray receiveJsonArray = null;
    private Activity activity;

    public void NetworkManager() {

    }

    // 상위 액티비티 전달을 위한 함수
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    // string -> JSON Object
    public JSONObject dataToJsonFormat(String[] name, String[] data) {
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < name.length; i++) {
                jsonObject.accumulate(name[i], data[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JSONArray executePost(String url, String[] name, String[] data) {
        return executePost(url, dataToJsonFormat(name, data));
    }

    public JSONArray executePost(String url, JSONObject data) {
        sendJsonObject = data;

        try {
            progressON(this.activity, "Loading...");
            // 동기화를 위해 get() 함수 이용
            receiveJsonArray = new JSONArray(new JSONTask().execute(serverIP + url).get());

            progressOFF();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return receiveJsonArray;
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground( String... urls) {
            try{

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cash-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream os = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
                    writer.write(sendJsonObject.toString());
                    writer.flush();
                    writer.close();

                    InputStream is = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is));
                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null ) {
                        buffer.append(line);
                    }

                    return buffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null) {
                        con.disconnect();
                    }
                    try {
                        if( reader != null ) {
                            reader.close();
                        }
                    } catch (IOException e ) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            /*
            try {
                // 받아오는 형태는 JSON 배열
                receiveJsonArray = new JSONArray(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            */
        }
    }
}
