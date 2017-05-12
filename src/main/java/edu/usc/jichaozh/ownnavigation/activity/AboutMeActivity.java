package edu.usc.jichaozh.ownnavigation.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import edu.usc.jichaozh.ownnavigation.R;

public class AboutMeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("About Me");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}