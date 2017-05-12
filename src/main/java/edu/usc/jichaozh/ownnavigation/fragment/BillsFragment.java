package edu.usc.jichaozh.ownnavigation.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import edu.usc.jichaozh.ownnavigation.R;
import edu.usc.jichaozh.ownnavigation.other.TabFontColorControl;

public class BillsFragment extends Fragment {
    private FragmentTabHost myTabHost;

    public BillsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myTabHost = new FragmentTabHost(getActivity());
        myTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_bills);

        Bundle argu1 = new Bundle();
        argu1.putString("type", "active");
        myTabHost.addTab(myTabHost.newTabSpec("ActiveBills").setIndicator("Active Bills"),
                BillChildFragment.newInstance().getClass(), argu1);

        Bundle argu2 = new Bundle();
        argu2.putString("type", "new");
        myTabHost.addTab(myTabHost.newTabSpec("NewBills").setIndicator("New Bills"),
                BillChildFragment.newInstance().getClass(), argu2);

        TabFontColorControl.initial(myTabHost.getTabWidget(), 2);

        myTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String str) {
                String currentTag = myTabHost.getCurrentTabTag();
                if (currentTag.equals("ActiveBills")) {
                    TabFontColorControl.setCurrent(myTabHost.getTabWidget(), 0, 2);
                }
                else if (currentTag.equals("NewBills")) {
                    TabFontColorControl.setCurrent(myTabHost.getTabWidget(), 1, 2);
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