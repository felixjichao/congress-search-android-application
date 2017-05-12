package edu.usc.jichaozh.ownnavigation.other;

/**
 * Created by Jichao on 2016/11/24 0024.
 */
public class BillInfo {
    public String bill_id;
    public String title;
    public String introduced_on;

    public BillInfo(String[] info) {
        bill_id = info[0];
        title = info[1];
        introduced_on = info[2];
    }

    @Override
    public String toString() {
        return bill_id + ";" + title + ";" + introduced_on;
    }
}
