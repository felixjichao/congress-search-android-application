package edu.usc.jichaozh.ownnavigation.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TabHost;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Downloader;
import edu.usc.jichaozh.ownnavigation.R;
import edu.usc.jichaozh.ownnavigation.activity.MainActivity;
import edu.usc.jichaozh.ownnavigation.other.TabFontColorControl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class LegislatorsFragment extends Fragment {
    private FragmentTabHost myTabHost;

    public LegislatorsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        myTabHost = new FragmentTabHost(getActivity());
        myTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_legislators);

        Bundle argu1 = new Bundle();
        argu1.putString("type", "state");
        myTabHost.addTab(myTabHost.newTabSpec("LegiByState").setIndicator("By States"),
                LegiChildFragment.newInstance().getClass(), argu1);

        Bundle argu2 = new Bundle();
        argu2.putString("type", "house");
        myTabHost.addTab(myTabHost.newTabSpec("LegiHouse").setIndicator("House"),
                LegiChildFragment.newInstance().getClass(), argu2);

        Bundle argu3 = new Bundle();
        argu3.putString("type", "senate");
        myTabHost.addTab(myTabHost.newTabSpec("LegiSenate").setIndicator("Senate"),
                LegiChildFragment.newInstance().getClass(), argu3);

        TabFontColorControl.initial(myTabHost.getTabWidget(), 3);
        myTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String str) {
                String currentTag = myTabHost.getCurrentTabTag();
                if (currentTag.equals("LegiByState")) {
                    TabFontColorControl.setCurrent(myTabHost.getTabWidget(), 0, 3);
                }
                else if (currentTag.equals("LegiHouse")) {
                    TabFontColorControl.setCurrent(myTabHost.getTabWidget(), 1, 3);
                }
                else if (currentTag.equals("LegiSenate")) {
                    TabFontColorControl.setCurrent(myTabHost.getTabWidget(), 2, 3);
                }
            }
        });

        return myTabHost;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}