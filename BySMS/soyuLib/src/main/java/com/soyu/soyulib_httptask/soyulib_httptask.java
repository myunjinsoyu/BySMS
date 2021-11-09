package com.soyu.soyulib_httptask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class soyulib_httptask extends AsyncTask<String, Void, String> {
    private static final String TASK_TAG = "soyulib_httptask";
    private Handler handler = null;
    private String flag = "";

    public soyulib_httptask() { }
    public soyulib_httptask(Handler mHandler) {
        this.handler = mHandler;
    }

    @Override
    protected void onPreExecute(){

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        String result = "";
        HttpURLConnection conn = null;
        try {
            Log.d(TASK_TAG, "args[0] = " + args[0]);
            Log.d(TASK_TAG, "args[1] = " + args[1]);
            this.flag = args[0];
            String urlString = this.flag; // SERVER_ADDRESS 설정 필요, 현재 빈 값
            Log.d(TASK_TAG, "urlString = " + urlString);
            URL url = new URL(urlString);

            // Open connection
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);         // 입력 스트림 사용 여부
            conn.setDoOutput(false);       // 출력 스트림 사용 여부
            conn.setUseCaches(false);      // 캐시 사용 여부
            conn.setReadTimeout(20000);    // 타임아웃 설정 (단위: ms)
            conn.setRequestMethod("POST"); // 요청 방식

            // POST 값 전달
            String params = "";
            params += args[1];
            PrintWriter output = new PrintWriter(conn.getOutputStream());
            output.print(params);
            output.close();

            // Response 받기
            @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            for (;;) {
                String line = br.readLine();
                if(line == null) break;
                sb.append(line).append("\n");
            }

            br.close();
            conn.disconnect();
            conn = null;

            result = sb.toString();
        } catch (MalformedURLException mURLException) {
            // 잘못된 URL 예외 발생
            mURLException.printStackTrace();
        } catch (java.io.IOException IOException) {
            // 입출력 예외 발생
            IOException.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    protected void onPostExecute(String result) {
        Message message = new Message();
        message.obj = (this.flag + "|" + result.trim());
        this.handler.sendMessage(message); // sendMessage on a null object reference
    }
}
