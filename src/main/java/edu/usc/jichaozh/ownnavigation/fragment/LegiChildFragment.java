package edu.usc.jichaozh.ownnavigation.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.usc.jichaozh.ownnavigation.R;
import edu.usc.jichaozh.ownnavigation.activity.LegiDetailActivity;
import edu.usc.jichaozh.ownnavigation.other.*;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Jichao on 2016/11/16 0016.
 */
public class LegiChildFragment extends Fragment implements View.OnClickListener{
    private List<LegislatorInfo> data;
    private ListView byStateList;
    private MyListAdapter myAdapter;
    private String url;
    private String type;
    private final String URLFIXED = "http://hwcsci571.rkhkam8tip.us-west-1.elasticbeanstalk.com/myCongressPageAPI.php?";
    private HashMap<String, Integer> index;
    private RequestData request;
    private LinearLayout linearLayout;
    private TextView byStateText;

    public static LegiChildFragment newInstance() {
        LegiChildFragment fragment = new LegiChildFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        //Log.i("oncreate", (bundle == null) + "");
        if (bundle != null) {
            type = bundle.get("type").toString();
            if (type.equals("state")) {
                url = URLFIXED + "type=legislators&flag=state&detail=no";
            }
            else if (type.equals("house")) {
                url = URLFIXED + "type=legislators&flag=house&detail=no";
            }
            else if (type.equals("senate")) {
                url = URLFIXED + "type=legislators&flag=senate&detail=no";
            }
            else {
                url = URLFIXED + "type=legislators&flag=app&detail=no";
            }
        }
        else {
            url = URLFIXED + "type=legislators&flag=state&detail=no";
        }
        //Log.i("oncreate", url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Log.i("createView", getParentFragment().toString());
        View view = inflater.inflate(R.layout.fragment_house_legislators, container, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.side_index);
        byStateText = (TextView) view.findViewById(R.id.legiByStateText);
        byStateList = (ListView) view.findViewById(R.id.legiByStateList);
        data = new ArrayList<>();
        myAdapter = new MyListAdapter(getActivity(), data, R.layout.legislators);
        byStateList.setAdapter(myAdapter);
        byStateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("item click", myAdapter.getItem(position).toString()+"");
                Intent intent = new Intent();
                intent.setClass(getActivity(), LegiDetailActivity.class);
                intent.putExtra("legi_id", myAdapter.getItem(position).toString());
                startActivity(intent);
            }
        });

        request = new RequestData(new RequestData.AsyncResponse() {
            @Override
            public void processDone(String result) {
                Gson gson = new Gson();
                Type token = new TypeToken<List<LegislatorInfo>>() {
                }.getType();
                List<LegislatorInfo> legislator = gson.fromJson(result, token);
                if (type.equals("legi")) {
                    data.clear();
                    for (LegislatorInfo legi : legislator) {
                        if (LocalStorage.contains(getActivity(), "legi_" + legi.bioguide_id)) {
                            data.add(legi);
                        }
                    }
                } else {
                    data.addAll(legislator);
                }
                myAdapter.notifyDataSetChanged();
                Log.i("returned", data.size() + "");
                getPossibleIndex(data);
                displayIndex(linearLayout);
                byStateText.setVisibility(View.GONE);
            }
        }, byStateText);
        request.execute(url);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("legi fragment resume", getParentFragment().getTag() +" " + type);
        myAdapter.notifyDataSetChanged();
        if (getParentFragment().getTag().equals("favorites")) {
            data.clear();
            Map<String, String> local = (Map<String, String>)LocalStorage.getAll(getActivity());
            Set<String> keySet = local.keySet();
            Log.i("local map", local.size() + "");
            for (String item : keySet) {
                if (item.split("_")[0].equals("legi")) {
                    data.add(new LegislatorInfo(local.get(item).split(";")));
                }
            }
            Collections.sort(data, new Comparator<LegislatorInfo>() {
                @Override
                public int compare(LegislatorInfo lhs, LegislatorInfo rhs) {
                    return lhs.name.compareTo(rhs.name);
                }
            });
            Log.i("resume", data.size() + "");
            myAdapter.notifyDataSetChanged();
            getPossibleIndex(data);
            displayIndex(linearLayout);
        }
    }

    private void getPossibleIndex(List<LegislatorInfo> data) {
        index = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            String key;
            if (type.equals("state")) {
                key = data.get(i).state.substring(0, 1);
            }
            else {
                key = data.get(i).name.substring(0, 1);
            }
            if (!index.containsKey(key)) {
                index.put(key, i);
            }
        }
        //Log.i("get index", index.size() + "");
    }

    private void displayIndex(LinearLayout linearLayout) {
        TextView textView;
        List<String> possibleIndex = new ArrayList<>(index.keySet());
        //Log.i("key set", index.keySet().toString());
        Collections.sort(possibleIndex);
        linearLayout.removeAllViews();
        for (String character : possibleIndex) {
            textView = (TextView) getActivity().getLayoutInflater().inflate(R.layout.item_index_side, null);
            textView.setText(character);
            textView.setOnClickListener(this);
            linearLayout.addView(textView);
        }
        //Log.i("display index", "done");
    }

    public void onClick(View view) {
        TextView selected = (TextView) view;
        byStateList.setSelection(index.get(selected.getText()));
    }
}
