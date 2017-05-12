package edu.usc.jichaozh.ownnavigation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.usc.jichaozh.ownnavigation.R;
import edu.usc.jichaozh.ownnavigation.activity.CommDetailActivity;
import edu.usc.jichaozh.ownnavigation.other.*;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Jichao on 2016/11/25 0025.
 */
public class CommChildFragment extends Fragment {
    private List<CommInfo> data;
    private CommListAdapter myAdapter;
    private String url;
    private final String URLFIXED = "http://hwcsci571.rkhkam8tip.us-west-1.elasticbeanstalk.com/myCongressPageAPI.php?";

    public static CommChildFragment newInstance() {
        CommChildFragment fragment = new CommChildFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String type = bundle.get("type").toString();
            if (type.equals("house")) {
                url = URLFIXED + "type=committees&flag=house&detail=no";
            }
            else if (type.equals("senate")) {
                url = URLFIXED + "type=committees&flag=senate&detail=no";
            }
            else if (type.equals("joint")) {
                url = URLFIXED + "type=committees&flag=joint&detail=no";
            }
            else {
                url = URLFIXED + "type=committees&flag=app&detail=no";
            }
        }
        else {
            url = URLFIXED + "type=committees&flag=app&detail=no";
        }
        //Log.i("oncreate", url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.i("createView", url);
        View view = inflater.inflate(R.layout.tab_bill_comm, container, false);
        final TextView BillCommText = (TextView) view.findViewById(R.id.BillCommText);
        final ListView BillCommList = (ListView) view.findViewById(R.id.BillCommList);
        data = new ArrayList<>();
        myAdapter = new CommListAdapter(getActivity(), data, R.layout.committees);
        BillCommList.setAdapter(myAdapter);
        BillCommList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("item click", myAdapter.getItem(position).toString()+"");
                Intent intent = new Intent();
                intent.setClass(getActivity(), CommDetailActivity.class);
                intent.putExtra("comm_id", myAdapter.getItem(position).toString());
                startActivity(intent);
            }
        });

        if (!getParentFragment().getTag().equals("favorites")) {
            RequestData request = new RequestData(new RequestData.AsyncResponse() {
                @Override
                public void processDone(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<CommInfo>>() {
                    }.getType();
                    List<CommInfo> legislator = gson.fromJson(result, type);

                    data.addAll(legislator);
                    myAdapter.notifyDataSetChanged();
                    Log.i("returned", data.size() + "");
                    BillCommText.setVisibility(View.GONE);
                }
            }, BillCommText);
            request.execute(url);
        }
        return view;
    }

    public void onResume() {
        super.onResume();
        if (getParentFragment().getTag().equals("favorites")) {
            data.clear();
            Map<String, String> local = (Map<String, String>) LocalStorage.getAll(getActivity());
            Set<String> keySet = local.keySet();
            Log.i("local map comm", local.size() + "");
            for (String item : keySet) {
                if (item.split("_")[0].equals("comm")) {
                    data.add(new CommInfo(local.get(item).split(";")));
                }
            }
            Collections.sort(data, new Comparator<CommInfo>() {
                @Override
                public int compare(CommInfo lhs, CommInfo rhs) {
                    return lhs.name.compareTo(rhs.name);
                }
            });
            Log.i("comm resume", data.size() + "");
            myAdapter.notifyDataSetChanged();
        }
    }
}
