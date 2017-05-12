package edu.usc.jichaozh.ownnavigation.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.squareup.picasso.Picasso;
import edu.usc.jichaozh.ownnavigation.R;
import edu.usc.jichaozh.ownnavigation.other.LocalStorage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Jichao on 2016/11/25 0025.
 */
public class LegiDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String legi_id;
    private String legiInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legi_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Legislator Info");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        legiInfo = bundle.getString("legi_id");
        legi_id = legiInfo.split(";")[0];
        //type = bundle.getString("legi_id").split(" ")[1];
        //TextView textView = (TextView) findViewById(R.id.legi_detail_text);
        //textView.setText(legi_id);

//        TableLayout tableLayout = (TableLayout) findViewById(R.id.legi_detail);
//        TableRow t1 = new TableRow(this);
        RequestBillDetial request = new RequestBillDetial(this);
        request.execute("http://hwcsci571.rkhkam8tip.us-west-1.elasticbeanstalk.com/myCongressPageAPI.php?type=legislators&flag=details&detail=" + legi_id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (type.equals("legi")) {
//            return super.onOptionsItemSelected(item);
//        }
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class RequestBillDetial extends AsyncTask<String, String, String> {
        String returnData;
        Context context;

        public RequestBillDetial(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
        }

        protected String doInBackground(String... urls) {
            HttpClient httpClient = new DefaultHttpClient();
            String i = urls[0];

            HttpGet httpGet = new HttpGet(i);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                returnData = EntityUtils.toString(entity);
            } catch (Exception ex) {
                Log.i("legiRequest", "failed");
            }
            //Log.i("legi detail", returnData);
            return returnData;
        }

        protected void onProgressUpdate(String... progress) {
        }

        protected void onPostExecute(String result) {
            findViewById(R.id.loadingPage).setVisibility(View.GONE);
            final ImageView favorite = (ImageView) findViewById(R.id.legi_favorite);
            if (LocalStorage.contains(context, "legi_" + legi_id)) {
                favorite.setImageResource(R.drawable.favorite_selected);
            }
            else {
                favorite.setImageResource(R.drawable.favorite);
            }

            final String[] splitDetail = result.split("\"[,:]\"");
            final String facebook = splitDetail[31];
            final String twitter = splitDetail[33];
            final String website = splitDetail[29];

            findViewById(R.id.legi_facebook).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (facebook.equals("N.A.")) {
                        Toast toast=Toast.makeText(getApplicationContext(), "No Available Facebook", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://www.facebook.com/" + facebook));
                        startActivity(intent);
                    }
                }
            });
            findViewById(R.id.legi_twitter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twitter.equals("N.A.")) {
                        Toast toast=Toast.makeText(getApplicationContext(), "No Available Twitter", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://twitter.com/" + twitter));
                        startActivity(intent);
                    }
                }
            });
            findViewById(R.id.legi_website).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (facebook.equals("N.A.")) {
                        Toast toast=Toast.makeText(getApplicationContext(), "No Available Website", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(website));
                        startActivity(intent);
                    }
                }
            });

            String imageURL = "https://theunitedstates.io/images/congress/225x275/" + splitDetail[1] + ".jpg";
            ImageView imageView = (ImageView)findViewById(R.id.legiPhoto);
            Picasso.with(context).load(imageURL).into(imageView);
            String party = splitDetail[15].substring(6, 7);
            if (party.equals("r")) {
                ((ImageView)findViewById(R.id.legiPartyPic)).setImageResource(R.drawable.r);
            }
            else if (party.equals("d")) {
                ((ImageView)findViewById(R.id.legiPartyPic)).setImageResource(R.drawable.d);
            }
            else {
                ((ImageView)findViewById(R.id.legiPartyPic)).setImageResource(R.drawable.i);
            }
            ((TextView)findViewById(R.id.legiParty)).setText(splitDetail[13]);
            ((TextView)findViewById(R.id.legiName)).setText(splitDetail[3]);
            ((TextView)findViewById(R.id.legiEmail)).setText(splitDetail[5]);
            ((TextView)findViewById(R.id.legiChamber)).setText(splitDetail[7]);
            ((TextView)findViewById(R.id.legiContact)).setText(splitDetail[11]);
            ((TextView)findViewById(R.id.legiStartTerm)).setText(splitDetail[17]);
            ((TextView)findViewById(R.id.legiEndTerm)).setText(splitDetail[19]);
            ((ProgressBar)findViewById(R.id.legiTerm)).incrementProgressBy(
                                    Integer.parseInt(splitDetail[20].split("[\":,]")[2]));
            (findViewById(R.id.legiTerm)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.barPercentage)).setText(splitDetail[20].split("[\":,]")[2] + "%");
            ((TextView)findViewById(R.id.legiOffice)).setText(splitDetail[21]);
            ((TextView)findViewById(R.id.legiState)).setText(splitDetail[23]);
            ((TextView)findViewById(R.id.legiFax)).setText(splitDetail[25]);
            ((TextView)findViewById(R.id.legiBirthday)).setText(splitDetail[27]);
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LocalStorage.contains(context, "legi_" + legi_id)) {
                        favorite.setImageResource(R.drawable.favorite);
                        LocalStorage.remove(context, "legi_" + legi_id);
                    }
                    else {
                        favorite.setImageResource(R.drawable.favorite_selected);
                        LocalStorage.put(context, "legi_" + legi_id, legiInfo);
                    }
                }
            });
            //Log.i("bills request", "return success");
        }
    }
}