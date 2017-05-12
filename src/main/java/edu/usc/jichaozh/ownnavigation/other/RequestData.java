package edu.usc.jichaozh.ownnavigation.other;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Jichao on 2016/11/24 0024.
 */
public class RequestData extends AsyncTask<String, String, String> {
    //String url;
    String returnData;
    TextView textView;

    public interface AsyncResponse {
        void processDone(String result);
    }

    AsyncResponse delegate = null;

    public RequestData(AsyncResponse callback, TextView textView) {
        delegate = callback;
        this.textView = textView;
    }
    @Override
    protected void onPreExecute() {
        textView.setText("Congress API");
    }
    protected String doInBackground(String... urls) {
        HttpClient httpClient = new DefaultHttpClient();
        String i = urls[0];

        HttpGet httpGet = new HttpGet(i);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            returnData = EntityUtils.toString(entity);
        }
        catch (Exception ex) {
            Log.i("httpRequest", "failed");
        }
        Log.i("async", "request success");
        return returnData;
    }

    protected void onProgressUpdate(String... progress) {
    }

    protected void onPostExecute(String result) {
        delegate.processDone(result);
        //Log.i("async request", "return success");
    }
}
