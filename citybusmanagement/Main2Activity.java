package com.example.sony.citybusmanagement;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sony.citybusmanagement.adapter.NavDrawerListAdapter;
import com.example.sony.citybusmanagement.model.NavDrawerItem;

import java.util.ArrayList;


public class Main2Activity extends AppCompatActivity {
    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CoordinatorLayout coordinatorLayout;
    // NavigationDrawer title "Nasdaq" in this example
    private CharSequence mDrawerTitle;

    //  App title "Navigation Drawer" in this example
    private CharSequence mTitle;

    // slider menu items details
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter nda;
    public static final String TAG="Error";
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //isNetworkAvailable();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#004D40"));
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        mTitle = mDrawerTitle = getTitle();

        // getting items of slider from array
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // list item in slider at 1 Home Nasdaq details
        navDrawerItems.add(new NavDrawerItem("Admin"));
        // list item in slider at 2 Facebook details
        navDrawerItems.add(new NavDrawerItem("Contact Us"));

        // setting list adapter for Navigation Drawer
        nda = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(nda);

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // Enable action bar icon_luncher as toggle Home Button

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00695C")));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                //R.drawable.ic_drawer,
                R.string.app_name,
                R.string.app_name) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); //Setting, Refresh and Rate App
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                mDrawerList.bringToFront();
                mDrawerList.requestLayout();
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * Slider menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected item
            displayView(position);
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  title/icon
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //called when invalidateOptionsMenu() invoke

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if Navigation drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private void displayView(int position) {
        // update the main content with called Fragment
        Fragment fragment = null;
        switch (position) {
            case 0:
                Intent i = new Intent(getApplicationContext(), MainActivity27.class);
                startActivity(i);
                mDrawerLayout.closeDrawer(mDrawerList);

                break;
            case 1:
                Intent i1 = new Intent(getApplicationContext(), Main28Activity.class);
                startActivity(i1);
                mDrawerLayout.closeDrawer(mDrawerList);

                break;
            case 2:
                Intent i2 = new Intent(getApplicationContext(), Nearby.class);
                startActivity(i2);
                mDrawerLayout.closeDrawer(mDrawerList);


                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {

            Log.e("this is mainActivity", "Error in else case");
        }
}


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void sendMessage1(View view) {
        Intent intent = new Intent(Main2Activity.this, MainActivity22.class);
        startActivity(intent);
    }

    public void sendMessage2(View view) {
        Intent intent = new Intent(Main2Activity.this, MainActivity23.class);
        startActivity(intent);
    }

    public void sendMessage3(View view) {
        Intent intent = new Intent(Main2Activity.this, MainActivity24.class);
        startActivity(intent);
    }

    public void sendMessage4(View view) {
        Intent intent = new Intent(Main2Activity.this, MainActivity25.class);
        startActivity(intent);
    }

    public void sendMessage5(View view) {
        Intent intent = new Intent(Main2Activity.this,MainActivty30.class);
        startActivity(intent);
    }

    public void sendMessage6(View view) {

        finish();
//
//        Intent intent = new Intent(Main2Activity.this,Nearby.class);
//        startActivity(intent);

    }

    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Snackbar snackbar = Snackbar
                     .make(findViewById(R.id.coordinatorLayout), "Press once again to exit!", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();
        }
        back_pressed = System.currentTimeMillis();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG, "onDestory");
        super.onDestroy();

        unregisterReceiver(receiver);

    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.v(LOG_TAG, "Receieved notification about network status");
            isNetworkAvailable(context);

        }


        private boolean isNetworkAvailable(final Context context) {
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if(!isConnected){
                                Log.v(LOG_TAG, "Now you are connected to Internet!");
                                //  networkStatus.setText("Now you are connected to Internet!");
                                //Toast.makeText(getApplicationContext(),"Now you are connected to Internet!",Toast.LENGTH_LONG).show();
                            }
                            return true;
                        }
                    }
                }
            }
            Log.v(LOG_TAG, "You are not connected to Internet!");
            //    networkStatus.setText("You are not connected to Internet!");
            //Toast.makeText(getApplicationContext(),"You are not connected to Internet!",Toast.LENGTH_LONG).show();
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(Main2Activity.this).create();

                alertDialog.setTitle("Problem occurred!!!");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(R.drawable.can1);
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        isNetworkAvailable(context);
                    }
                });
                alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();


                    }
                });

                alertDialog.show();
            } catch (Exception e) {
                Log.d(TAG, "Connectivity Error: " + e.getMessage());
            }

            isConnected = false;
            return false;
        }
    }

}