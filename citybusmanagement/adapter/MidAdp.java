package com.example.sony.citybusmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sony.citybusmanagement.R;
import com.example.sony.citybusmanagement.model.DispMid;

import java.util.ArrayList;

/**
 * Created by SONY on 16-03-2016.
 */
public class MidAdp extends BaseAdapter {
    Context context;
    ArrayList<DispMid> arrayList;
    TextView name;
    public MidAdp(Context context, ArrayList<DispMid> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final DispMid s=arrayList.get(position);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.listdesign,null);

        name= (TextView) convertView.findViewById(R.id.txt1);
        name.setText(s.getMid());



        return convertView;
    }
}
