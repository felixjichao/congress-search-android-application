package edu.usc.jichaozh.ownnavigation.other;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.usc.jichaozh.ownnavigation.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jichao on 2016/11/25 0025.
 */
public class BillListAdapter extends BaseAdapter {
    private List<BillInfo> data;
    private int listViewItem;
    LayoutInflater layoutInflater;
    Context context;

    public BillListAdapter(Context context, List<BillInfo> data, int listViewItem) {
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
            idBillComm = (TextView) convertView.findViewById(R.id.id_bill_comm);
            imageView = (ImageView) convertView.findViewById(R.id.detailArrow);
            titleName = (TextView) convertView.findViewById(R.id.title_name);
            dateChamber = (TextView) convertView.findViewById(R.id.date_chamber);
            convertView.setTag(new DataWrapper(idBillComm, imageView, titleName, dateChamber));
        }
        else {
            DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
            idBillComm = dataWrapper.idBillComm;
            imageView = dataWrapper.imageView;
            titleName = dataWrapper.titleName;
            dateChamber = dataWrapper.dateChamber;

        }
        BillInfo bill = data.get(position);
        idBillComm.setText(bill.bill_id);
        imageView.setImageResource(R.drawable.detail);
        titleName.setText(bill.title);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = null;
        try {
            date = simpleDateFormat.parse(bill.introduced_on);
        }
        catch (ParseException e) {
            Log.e("date", "error");
        }
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        dateChamber.setText(simpleDateFormat2.format(date));
        return convertView;
    }

    private final class DataWrapper {
        public TextView idBillComm;
        public ImageView imageView;
        public TextView titleName;
        public TextView dateChamber;

        public DataWrapper(TextView textView, ImageView imageView, TextView textView1, TextView textView2) {
            idBillComm = textView;
            this.imageView = imageView;
            this.titleName = textView1;
            this.dateChamber = textView2;
        }
    }
}