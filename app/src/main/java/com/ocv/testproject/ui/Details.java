package com.ocv.testproject.ui;


import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.support.annotation.RequiresApi;


import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;


import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ocv.testproject.R;
import com.ocv.testproject.model.Ipsum;

import java.util.ArrayList;


public class Details extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    private ShareActionProvider mShareActionProvider;

    private boolean darkTheme;
    private Ipsum dIpsum;
    public TextView title, date, content;
    private ArrayList<String> images;
    String str;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        darkTheme = useDarkTheme;
        //if true, sets it to dark
        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        dIpsum = (Ipsum) intent.getSerializableExtra("ipsum");
        images = dIpsum.getImageArray();

        title = (TextView) findViewById(R.id.dTitle);
        date = (TextView) findViewById(R.id.dDate);
        content = (TextView) findViewById(R.id.dContent);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);

        if (!images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                ImageView imageView = new ImageView(this);
                imageView.setId(i);
                imageView.setPadding(5, 5, 5, 5);
                imageView.setAdjustViewBounds(true);
                imageView.setMinimumHeight(1000);
                imageView.setMinimumWidth(1000);
                imageView.setMaxHeight(1000);
                imageView.setMaxWidth(2000);
                Glide.with(this).load(dIpsum.getImageArray().get(i)).into(imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                final int index = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("TAG", "The index is " + index);
                        str = dIpsum.getImageArray().get(index);
                        Intent intent = new Intent(v.getContext(), FullScreen.class);
                        intent.putExtra("URL", str);
                        v.getContext().startActivity(intent);
                    }
                });
                layout.addView(imageView);
            }
        } else {
            TextView textView = new TextView(this);
            textView.setPadding(2, 2, 2, 2);
            textView.setText("No images to display :(");
            textView.setTextColor(getResources().getColor(R.color.white));
            layout.addView(textView);
        }

        title.setText(dIpsum.getTitle());
        date.setText(dIpsum.getDateStamp());
        content.setText(Html.fromHtml(dIpsum.getContent()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        MenuItem item = menu.findItem(R.id.share);

        ShareActionProvider provider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        MenuItemCompat.setActionProvider(item, mShareActionProvider);

        if (provider != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "stuff");
            shareIntent.setType("text/plain");
            provider.setShareIntent(shareIntent);
        }

        return true;


    }


    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Check this out!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "This is so cool!");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

                supportInvalidateOptionsMenu();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
