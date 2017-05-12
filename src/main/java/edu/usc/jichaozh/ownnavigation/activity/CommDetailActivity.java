package edu.usc.jichaozh.ownnavigation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import edu.usc.jichaozh.ownnavigation.R;
import edu.usc.jichaozh.ownnavigation.other.LocalStorage;

/**
 * Created by Jichao on 2016/11/27 0027.
 */
public class CommDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String comm_id;
    private String commInfo;
    private Context context = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Committee Info");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        commInfo = bundle.getString("comm_id");
        comm_id = commInfo.split(";")[0];

        String result = commInfo;
        final ImageView favorite = (ImageView) findViewById(R.id.comm_favorite);
        if (LocalStorage.contains(this, "comm_" + comm_id)) {
            favorite.setImageResource(R.drawable.favorite_selected);
        }
        else {
            favorite.setImageResource(R.drawable.favorite);
        }
        final String[] splitDetail = result.split(";");
        ((TextView)findViewById(R.id.comm_id)).setText(splitDetail[0]);
        ((TextView)findViewById(R.id.commName)).setText(splitDetail[1]);
        ImageView chamberImg = (ImageView) findViewById(R.id.commChamberImg);
        if (splitDetail[2].substring(6, 7).equals("h")) {
            chamberImg.setImageResource(R.drawable.h);
        }
        else {
            chamberImg.setImageResource(R.drawable.s);
        }
        ((TextView)findViewById(R.id.commChamber)).setText(splitDetail[3]);
        ((TextView)findViewById(R.id.commParent)).setText(splitDetail[4]);
        ((TextView)findViewById(R.id.commContact)).setText(splitDetail[5]);
        ((TextView)findViewById(R.id.commOffice)).setText(splitDetail[6]);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LocalStorage.contains(context, "comm_" + comm_id)) {
                    favorite.setImageResource(R.drawable.favorite);
                    LocalStorage.remove(context, "comm_" + comm_id);
                }
                else {
                    favorite.setImageResource(R.drawable.favorite_selected);
                    LocalStorage.put(context, "comm_" + comm_id, commInfo);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
