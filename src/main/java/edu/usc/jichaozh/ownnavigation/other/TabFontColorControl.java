package edu.usc.jichaozh.ownnavigation.other;

import android.graphics.Color;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jichao on 2016/11/27 0027.
 */
public class TabFontColorControl {
    public static void initial(TabWidget target, int numOfTabs) {
        setCurrent(target, 0, numOfTabs);
    }

    public static void setCurrent(TabWidget target, int index, int numOfTabs) {
        ArrayList<Integer> all = new ArrayList<>();
        for (int i = 0; i < numOfTabs; i++) {
            all.add(i);
        }
        for (Integer i : all) {
            if (i == index) {
                ((TextView)target.getChildAt(i).findViewById(android.R.id.title)).setTextColor(Color.parseColor("#212121"));
            }
            else {
                ((TextView)target.getChildAt(i).findViewById(android.R.id.title)).setTextColor(Color.parseColor("#737373"));
            }
        }
    }
}
