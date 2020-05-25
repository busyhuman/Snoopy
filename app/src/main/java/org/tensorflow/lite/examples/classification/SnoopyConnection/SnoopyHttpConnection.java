package org.tensorflow.lite.examples.classification.SnoopyConnection;
import android.util.Log;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

public class SnoopyHttpConnection {

    public static String makeConnection(String url, String type, String reqbody) {

        String result = null;

        try {
            URL uri = new URL(url);
            HttpURLConnection con = (HttpURLConnection) uri.openConnection();

            uri = new URL(url);

            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            con.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=utf-8"); // 페이지 마다 다를 수 있음
            con.setRequestMethod(type);
            if (reqbody != null) {
                con.setDoInput(true);
                con.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(reqbody);
                out.flush();
                out.close();
            }
            con.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp = null;
            StringBuilder sb = new StringBuilder();
            while ((temp = in.readLine()) != null) {
                sb.append(temp).append(" ");
            }
            result = sb.toString();
            in.close();
            con.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // GET의 결과
        return result;
    }
}