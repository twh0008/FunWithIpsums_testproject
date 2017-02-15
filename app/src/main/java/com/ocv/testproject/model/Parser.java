package com.ocv.testproject.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Parser {

    protected static InputStream getInputStream(String sourceUrl) throws IOException {
        InputStream inputStream;

        URL url = new URL(sourceUrl);
        URLConnection connection = url.openConnection();
        String redirect = connection.getHeaderField("Location");
        if (redirect != null) {
            connection = new URL(redirect).openConnection();
        }

        inputStream = connection.getInputStream();

        return inputStream;
    }

    protected static String getStringFromStream(InputStream inputStream) {
        String result = "";
        try {
            if (inputStream != null) {
                Writer writer = new StringWriter();

                char[] buffer = new char[1024];
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(
                            inputStream, "UTF-8"), 1024);
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                } finally {
                    inputStream.close();
                }
                result = writer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static ArrayList<JSONObject> parse() throws IOException, JSONException{
        ArrayList<JSONObject> items = new ArrayList<>();

        InputStream stream = getInputStream("https://apps.myocv.com/feed/blog/a12722222/ipsumList");
        String source = getStringFromStream(stream);

        JSONArray root = new JSONArray(source);
        for (int i = 0; i < root.length(); i++) {
            items.add(root.getJSONObject(i));
        }

        return items;
    }

}
