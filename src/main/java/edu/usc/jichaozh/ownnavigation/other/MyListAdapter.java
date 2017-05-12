package edu.usc.jichaozh.ownnavigation.other;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import edu.usc.jichaozh.ownnavigation.R;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jichao on 2016/11/24 0024.
 */
public class MyListAdapter extends BaseAdapter implements SectionIndexer {
    private List<LegislatorInfo> data;
    private int listViewItem;
    LayoutInflater layoutInflater;
    Context context;

    public MyListAdapter(Context context, List<LegislatorInfo> data, int listViewItem) {
        this.context = context;
        this.data = data;
        this.listViewItem = listViewItem;
        layoutInflater = LayoutInflater.from(context);
        Log.i("adapter", data.size()+"");

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.i("adapter", "getView");
        ImageView imageView = null;
        TextView legiName = null;
        TextView legiInfo = null;
        ImageView detail = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(listViewItem, null);
            imageView = (ImageView) convertView.findViewById(R.id.legiImg);
            legiName = (TextView) convertView.findViewById(R.id.legiName);
            legiInfo = (TextView) convertView.findViewById(R.id.legiInfo);
            detail = (ImageView) convertView.findViewById(R.id.detailArrow);
            convertView.setTag(new DataWrapper(imageView, legiName, legiInfo, detail));
        }
        else {
            DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
            imageView = dataWrapper.imageView;
            legiName = dataWrapper.legiName;
            legiInfo = dataWrapper.legiInfo;
            detail = dataWrapper.detail;
        }
        LegislatorInfo legislator = data.get(position);
        String imageURL = "https://theunitedstates.io/images/congress/225x275/" + legislator.bioguide_id + ".jpg";
        Picasso.with(context).load(imageURL).into(imageView);

        detail.setImageResource(R.drawable.favorite);
        Map<String, String> local = (Map<String, String>)LocalStorage.getAll(context);
        Set<String> keySet = local.keySet();
        //Log.i("local map", local.size() + "");
        for (String item : keySet) {
            if (item.split("_")[0].equals("legi")) {
                Log.i("current legi", item);
                if (item.substring(5).equals(legislator.bioguide_id)) {
                    detail.setImageResource(R.drawable.favorite_selected);
                }
            }
        }

        /*detail.setImageResource(R.drawable.favorite);*/
        legiName.setText(legislator.name);
        String party = legislator.party;
        party = party.substring(6, 7).toUpperCase();
        legiInfo.setText("(" + party + ")" + legislator.state + " - " + legislator.district);
        //Log.i("getView", legislator.bill_id);
        return convertView;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).state.charAt(0) == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getSectionForPosition(int position) {
        if (data.size() != 0) {
            return data.get(position).state.charAt(0);
        }
        return 0;
    }

    private final class DataWrapper {
        public ImageView imageView;
        public TextView legiName;
        public TextView legiInfo;
        public ImageView detail;

        public DataWrapper(ImageView imageView, TextView textView1, TextView textView2, ImageView imageView2) {
            this.imageView = imageView;
            this.legiName = textView1;
            this.legiInfo = textView2;
            detail = imageView2;
        }
    }
}
