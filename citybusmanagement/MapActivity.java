package com.example.sony.citybusmanagement;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MapActivity extends AppCompatActivity {
    private GoogleMap gMap;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        gMap= ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();

        final String editFrom = getIntent().getStringExtra("FROM");
        final String editTo = getIntent().getStringExtra("TO");
        String l1=getIntent().getExtras().getString("l1");
        String l2=getIntent().getExtras().getString("l2");
        String l3=getIntent().getExtras().getString("l3");
        String l4=getIntent().getExtras().getString("l4");
        new RotaTask(this, gMap, editFrom, editTo,l1,l2,l3,l4).execute();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#004D40"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00695C")));

    }

}
