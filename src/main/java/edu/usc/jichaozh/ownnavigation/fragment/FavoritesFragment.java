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

public class FavoritesFragment extends Fragment {
    private FragmentTabHost myTabHost;

    public FavoritesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myTabHost = new FragmentTabHost(getActivity());
        myTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_favorites);

        Bundle argu1 = new Bundle();
        argu1.putString("type", "legi");
        myTabHost.addTab(myTabHost.newTabSpec("legi").setIndicator("Legislators"),
                LegiChildFragment.newInstance().getClass(), argu1);

        Bundle argu2 = new Bundle();
        argu2.putString("type", "bills");
        myTabHost.addTab(myTabHost.newTabSpec("bills").setIndicator("Bills"),
                BillChildFragment.newInstance().getClass(), argu2);

        Bundle argu3 = new Bundle();
        argu3.putString("type", "comm");
        myTabHost.addTab(myTabHost.newTabSpec("comm").setIndicator("Committees"),
                CommChildFragment.newInstance().getClass(), argu3);

        TabFontColorControl.initial(myTabHost.getTabWidget(), 3);
        myTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String str) {
                String currentTag = myTabHost.getCurrentTabTag();
                if (currentTag.equals("legi")) {
                    TabFontColorControl.setCurrent(myTabHost.getTabWidget(), 0, 3);
                }
                else if (currentTag.equals("bills")) {
                    TabFontColorControl.setCurrent(myTabHost.getTabWidget(), 1, 3);
                }
                else if (currentTag.equals("comm")) {
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

    public void onResume() {
        super.onResume();
        //Log.i("Favorite", "resume");
    }
}