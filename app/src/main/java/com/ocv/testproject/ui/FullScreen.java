package com.ocv.testproject.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ocv.testproject.R;
import com.ocv.testproject.model.Ipsum;
import com.ocv.testproject.model.TouchImageView;

import org.json.JSONObject;

import java.util.ArrayList;

public class FullScreen extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //retrieves theme
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        //uses TouchImageView for image
        final TouchImageView fullscreenImageView = (TouchImageView) findViewById(R.id.fullScreenImageView);

        Intent callingActivity = getIntent();
        if (callingActivity != null) {
            Intent intent = getIntent();
            str = (String) intent.getSerializableExtra("URL");

            if (str != null && fullscreenImageView != null) {

                Glide.with(this).load(str).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        fullscreenImageView.setImageBitmap(resource);
                    }
                });
            }

        }
    }


}
