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
import edu.usc.jichaozh.ownnavigation.activity.BillDetailActivity;
import edu.usc.jichaozh.ownnavigation.other.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jichao on 2016/11/16 0016.
 */
public class BillChildFragment extends Fragment {
    private List<BillInfo> data;
    private BillListAdapter myAdapter;
    private String url;
    private final String URLFIXED = "http://hwcsci571.rkhkam8tip.us-west-1.elasticbeanstalk.com/myCongressPageAPI.php?";

    public static BillChildFragment newInstance() {
        BillChildFragment fragment = new BillChildFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        //Log.i("oncreate", (bundle == null) + "");
        if (bundle != null) {
            String type = bundle.get("type").toString();
            if (type.equals("active")) {
                url = URLFIXED + "type=bills&flag=active&detail=no";
            }
            else if (type.equals("new")) {
                url = URLFIXED + "type=bills&flag=new&detail=no";
            }
            else {
                url = URLFIXED + "type=bills&flag=active&detail=no";
            }
        }
        else {
            url = URLFIXED + "type=bills&flag=active&detail=no";
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
        myAdapter = new BillListAdapter(getActivity(), data, R.layout.bills);
        BillCommList.setAdapter(myAdapter);
        BillCommList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("item click", myAdapter.getItem(position).toString()+"");
                Intent intent = new Intent();
                intent.setClass(getActivity(), BillDetailActivity.class);
                intent.putExtra("bill_id", myAdapter.getItem(position).toString());
                startActivity(intent);
            }
        });

        if (!getParentFragment().getTag().equals("favorites")) {
            RequestData request = new RequestData(new RequestData.AsyncResponse() {
                @Override
                public void processDone(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<BillInfo>>() {
                    }.getType();
                    List<BillInfo> legislator = gson.fromJson(result, type);

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

    @Override
    public void onResume() {
        super.onResume();
        if (getParentFragment().getTag().equals("favorites")) {
            data.clear();
            Map<String, String> local = (Map<String, String>) LocalStorage.getAll(getActivity());
            Set<String> keySet = local.keySet();
            Log.i("local map bills", local.size() + "");
            for (String item : keySet) {
                if (item.split("_")[0].equals("bill")) {
                    data.add(new BillInfo(local.get(item).split(";")));
                }
            }
            Collections.sort(data, new Comparator<BillInfo>() {
                @Override
                public int compare(BillInfo lhs, BillInfo rhs) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = simpleDateFormat.parse(lhs.introduced_on);
                        date2 = simpleDateFormat.parse(rhs.introduced_on);
                    }
                    catch (ParseException e) {
                        Log.e("date", "error");
                    }
                    return date2.compareTo(date1);
                }
            });
            Log.i("bills resume", data.size() + "");
            myAdapter.notifyDataSetChanged();
        }
    }
}