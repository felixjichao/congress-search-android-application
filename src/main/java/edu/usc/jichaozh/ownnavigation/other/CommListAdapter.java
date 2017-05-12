package edu.usc.jichaozh.ownnavigation.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.usc.jichaozh.ownnavigation.R;

import java.util.List;

/**
 * Created by Jichao on 2016/11/25 0025.
 */
public class CommListAdapter extends BaseAdapter {
    private List<CommInfo> data;
    private int listViewItem;
    LayoutInflater layoutInflater;
    Context context;

    public CommListAdapter(Context context, List<CommInfo> data, int listViewItem) {
        this.context = context;
        this.data = data;
        this.listViewItem = listViewItem;
        layoutInflater = LayoutInflater.from(context);
        //Log.i("adapter", data.size()+"");
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
        TextView idBillComm = null;
        ImageView imageView = null;
        TextView titleName = null;
        TextView dateChamber = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(listViewItem, null);
            imageView = (ImageView) convertView.findViewById(R.id.detailArrow);
            idBillComm = (TextView) convertView.findViewById(R.id.id_bill_comm);
            titleName = (TextView) convertView.findViewById(R.id.title_name);
            dateChamber = (TextView) convertView.findViewById(R.id.date_chamber);
            convertView.setTag(new DataWrapper(imageView, idBillComm, titleName, dateChamber));
        }
        else {
            DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
            idBillComm = dataWrapper.idBillComm;
            imageView = dataWrapper.imageView;
            titleName = dataWrapper.titleName;
            dateChamber = dataWrapper.dateChamber;
        }
        CommInfo comm = data.get(position);
        idBillComm.setText(comm.committee_id);
        imageView.setImageResource(R.drawable.detail);
        titleName.setText(comm.name);
        dateChamber.setText(comm.chamber);
        return convertView;
    }

    private final class DataWrapper {
        public TextView idBillComm;
        public ImageView imageView;
        public TextView titleName;
        public TextView dateChamber;

        public DataWrapper(ImageView imageView, TextView textView,TextView textView1, TextView textView2) {
            idBillComm = textView;
            this.imageView = imageView;
            this.titleName = textView1;
            this.dateChamber = textView2;
        }
    }
}
