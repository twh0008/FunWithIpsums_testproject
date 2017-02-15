package com.ocv.testproject.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.text.format.DateFormat;


/**
 * Created by Hearts on 2/1/2017.
 */
public class Ipsum implements Serializable {

    private String title, content, date;
    private ArrayList<String> imageArray;

    public Ipsum(String title, String date, String content, ArrayList<String> imageArray) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.imageArray = imageArray;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getImageArray() {
        return imageArray;
    }

    public void setImageArray(ArrayList<String> imageArray) {
        this.imageArray = imageArray;
    }


    public String getDateStamp() {

        String milli = this.getDate();
        //String milli = this.getDateObject().getString("sec");
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        long longTime = Long.parseLong(milli) * 1000;

        cal.setTimeInMillis(longTime);
        String date = DateFormat.format("MM/dd/yyyy", cal).toString();

        return date.toString();


    }
}
