package edu.usc.jichaozh.ownnavigation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import edu.usc.jichaozh.ownnavigation.R;
import edu.usc.jichaozh.ownnavigation.other.LocalStorage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Jichao on 2016/11/27 0027.
 */
public class BillDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String bill_id;
    private String billInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bill Info");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        billInfo = bundle.getString("bill_id");
        bill_id = billInfo.split(";")[0];
        RequestBillDetial request = new RequestBillDetial(this);
        request.execute("http://hwcsci571.rkhkam8tip.us-west-1.elasticbeanstalk.com/myCongressPageAPI.php?type=bills&flag=details&detail=" + bill_id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        else {
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
                Log.i("billDetailRequest", "failed");
            }
            //Log.i("legi detail", returnData);
            return returnData;
        }

        protected void onProgressUpdate(String... progress) {
        }

        protected void onPostExecute(String result) {
            findViewById(R.id.loadingPage).setVisibility(View.GONE);
            final ImageView favorite = (ImageView) findViewById(R.id.bill_favorite);
            if (LocalStorage.contains(context, "bill_" + bill_id)) {
                favorite.setImageResource(R.drawable.favorite_selected);
            }
            else {
                favorite.setImageResource(R.drawable.favorite);
            }

            final String[] splitDetail = result.split("\"[,:]\"");
            ((TextView)findViewById(R.id.bill_id)).setText(splitDetail[1]);
            ((TextView)findViewById(R.id.billTitle)).setText(splitDetail[3].replace('\\', '\000'));
            ((TextView)findViewById(R.id.billType)).setText(splitDetail[5]);
            ((TextView)findViewById(R.id.billSponsor)).setText(splitDetail[7]);
            ((TextView)findViewById(R.id.billChamber)).setText(splitDetail[11]);
            ((TextView)findViewById(R.id.billStatus)).setText(splitDetail[13]);
            ((TextView)findViewById(R.id.billIntro)).setText(splitDetail[15]);
            ((TextView)findViewById(R.id.billCongress)).setText(splitDetail[17]);
            ((TextView)findViewById(R.id.billVersion)).setText(splitDetail[19]);
            //Log.i("bill URL", splitDetail[21].substring(0, splitDetail[21].length()-2));
            String billURL = splitDetail[21].substring(0, ((splitDetail[21]).length()-6));
            ((TextView)findViewById(R.id.billURL)).setText(billURL);
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LocalStorage.contains(context, "bill_" + bill_id)) {
                        favorite.setImageResource(R.drawable.favorite);
                        LocalStorage.remove(context, "bill_" + bill_id);
                    }
                    else {
                        favorite.setImageResource(R.drawable.favorite_selected);
                        LocalStorage.put(context, "bill_" + bill_id, billInfo);
                    }
                }
            });
            //Log.i("bills request", "return success");
        }
    }
}
