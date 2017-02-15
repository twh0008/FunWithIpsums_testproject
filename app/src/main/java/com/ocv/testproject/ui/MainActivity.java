package com.ocv.testproject.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.ocv.testproject.model.Ipsum;
import com.ocv.testproject.model.Parser;
import com.ocv.testproject.R;
import com.ocv.testproject.adapter.CustomAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter customAdapter;
    private List<Ipsum> list;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";


    ArrayList<JSONObject> items = new ArrayList<>();
    private boolean darkTheme;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //TODO Follow the instructions below to complete this project
        //TODO FIX ADAPTER POSITION
        //TODO Fix Theme Changing
        //TODO Fix styles
        //TODO Fix passing object over
        /*
            Take the parsed items in the Arraylist 'items' and display
            them in a list via a RecyclerView. Each item, when clicked,
            should go to a detail page with the ability to show all of
            said items details and images (if there aren't any images it should
            note that as well in the detail page)
         */

        //Saves user preference
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        darkTheme = useDarkTheme;
        //if true, sets it to dark
        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }


        //set up view and toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();

        //execute thread to retrieve json
        new BackgroundThread().execute();
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        customAdapter = new CustomAdapter(this, list);
        recyclerView.setAdapter(customAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logToggle = menu.findItem(R.id.toggle);

        logToggle.setTitle(darkTheme ? "Auburn Theme" : "Alabama Theme");

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggle:
                darkTheme = !darkTheme;

                if (darkTheme) {
                    toggleTheme(darkTheme);

                } else {
                    toggleTheme(false);

                }
                supportInvalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //region AsyncTask
    public class BackgroundThread extends AsyncTask<Void, Void, ArrayList<JSONObject>> {

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                items = Parser.parse();

                for (int i = 0; i < items.size(); i++) {
                    JSONObject jsonObject = items.get(i);
                    String title = jsonObject.getString("title");
                    JSONObject date = jsonObject.getJSONObject("date");
                    String dateString = date.getString("sec");
                    String content = jsonObject.getString("content");

                    ArrayList<String> imageList = new ArrayList<String>();


                    if (jsonObject.getJSONArray("images").length() != 0) {

                        imageList.add(jsonObject.getJSONArray("images").getJSONObject(0).getString("large"));
                        imageList.add(jsonObject.getJSONArray("images").getJSONObject(0).getString("small"));

                    }

                    Ipsum ipsum = new Ipsum(title, dateString, content, imageList);

                    list.add(ipsum);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> jsonObjects) {

            customAdapter.notifyDataSetChanged();

        }

    }


    private void toggleTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();

        Intent intent = getIntent();
        finish();

        startActivity(intent);
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}




