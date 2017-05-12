package edu.usc.jichaozh.ownnavigation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import edu.usc.jichaozh.ownnavigation.fragment.*;
import edu.usc.jichaozh.ownnavigation.R;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    public static int navItemIndex = 0;
    private static final String TAG_LEGISLATORS = "legislators";
    private static final String TAG_BILLS = "bills";
    private static final String TAG_COMMITTEES = "committees";
    private static final String TAG_FAVORITES = "favorites";
    public static String CURRENT_TAG = TAG_LEGISLATORS;
    private String[] myTitle;
    private boolean loadLegiLatorsFragment = true;
    private Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        myTitle = getResources().getStringArray(R.array.nav_item_activity_titles);
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_LEGISLATORS;
            loadLegislatorsFragment();
        }
    }

    private void loadLegislatorsFragment() {
        selectNavMenu();
        setToolbarTitle();
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getLegislatorsFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            myHandler.post(mPendingRunnable);
        }
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getLegislatorsFragment() {
        switch (navItemIndex) {
            case 0:
                return new LegislatorsFragment();
            case 1:
                return new BillsFragment();
            case 2:
                return new CommitteesFragment();
            case 3:
                return new FavoritesFragment();
            default:
                return new LegislatorsFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(myTitle[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_legislators:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_LEGISLATORS;
                        break;
                    case R.id.nav_bills:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_BILLS;
                        break;
                    case R.id.nav_committees:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_COMMITTEES;
                        break;
                    case R.id.nav_favorites:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_FAVORITES;
                        break;
                    case R.id.nav_about_me:
                        startActivity(new Intent(MainActivity.this, AboutMeActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadLegislatorsFragment();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        if (loadLegiLatorsFragment) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_LEGISLATORS;
                loadLegislatorsFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}