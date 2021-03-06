package com.example.sony.citybusmanagement;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sony.citybusmanagement.adapter.MidAdp;
import com.example.sony.citybusmanagement.adapter.NoAdp;
import com.example.sony.citybusmanagement.model.DispMid;
import com.example.sony.citybusmanagement.model.DispNo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity22 extends AppCompatActivity {
    String[] an={"1", "13", "13/1", "40/1","60", "52", "136", "800",};
    Button b1;
    Spinner sp;
    String sps;
    ListView lv;
    TextView tv,t1,t2;
    static String json="";
    static InputStream is=null;
    String l1;
    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    public static final String TAG = "Error";

    Dialog d;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
//        RunTask();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        d=new Dialog(MainActivity22.this);
        d.setTitle("No Bus");

        d.setContentView(R.layout.listdesign);
        tv=(TextView)d.findViewById(R.id.txt1);
        t1=(TextView)findViewById(R.id.tt1);
        t2=(TextView)findViewById(R.id.tt2);

        tv.setText("No bus found!!");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        lv=(ListView)findViewById(R.id.lv1);
        sp=(Spinner)findViewById(R.id.spin);

//        new getBusId().execute();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String t = parent.getItemAtPosition(position).toString();
                ((TextView) findViewById(R.id.tw1)).setText(t);
                sps = sp.getSelectedItem().toString();
                AsyncTaskJson aj = new AsyncTaskJson();

                aj.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String t =(parent.getItemAtPosition(position).toString());
//                Log.i("T result:", "[" + t + "]");
//
//                l1=t;
//                AsyncTaskJson1 aj1=new AsyncTaskJson1();
//
//                aj1.execute();
//
////                String names[] ={"A","B","C","D"};
////                AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(MainActivity22.this);
////                LayoutInflater inflater = getLayoutInflater();
////                View convertView = (View) inflater.inflate(R.layout.list, null);
////                alertDialog1.setView(convertView);
////                alertDialog1.setTitle("Stops");
////                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
////                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,names);
////                lv.setAdapter(adapter);
////                alertDialog1.show();
////
//
//            }
//        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#004D40"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00695C")));

    }

    private void RunTask() {
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

            //Toast.makeText(getApplicationContext(),"Connected",8000).show();

        } else {

//            Toast.makeText(getApplicationContext(), "You are not online!!!!", 8000).show();
//            Log.v("Home", "############################You are not online!!!!");
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity22.this).create();

                alertDialog.setTitle("Problem occurred!!!");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(R.drawable.can1);
                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });
                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       RunTask();

                    }
                });
                alertDialog.show();
            } catch (Exception e) {
                String TAG="error";
                Log.d(TAG, "Show Dialog: "+e.getMessage());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                NavUtils.navigateUpFromSameTask(MainActivity22.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class AsyncTaskJson extends AsyncTask<String,Void,String> {

        String jsonurl="http://raju1221.dx.am/dispbus.php";

        Dialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading= ProgressDialog.show(MainActivity22.this,"Please Wait","Loading..");
        }

        @Override
        protected String doInBackground(String[] params) {

            ArrayList<NameValuePair> al=new ArrayList<NameValuePair>();

            al.add(new BasicNameValuePair("busno",sps));

            DefaultHttpClient httpClient=new DefaultHttpClient();

            HttpPost httpPost=new HttpPost(jsonurl);
            try{
                httpPost.setEntity(new UrlEncodedFormEntity(al));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                HttpResponse response=httpClient.execute(httpPost);

                HttpEntity entity=response.getEntity();

                is=entity.getContent();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);

                StringBuilder sb=new StringBuilder();

                String line="";

                while ((line=reader.readLine())!=null)
                {
                    sb.append(line+"\n");
                }

                is.close();

                json=sb.toString();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();
            String str=result.trim();
            ArrayList<DispMid> arrayList=new ArrayList<DispMid>();

            if (str.equalsIgnoreCase("norecord"))
            {
                String[] temp={"Select Bus Number from the list!!!"};
                ArrayAdapter<String> ad=new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,temp);
                lv.setAdapter(ad);
                t1.setText("Source");
                t2.setText("Destination");
            }
            else
            {
                try {
                    Log.i("tagconvertstr", "["+result+"]");
                    JSONObject jo=new JSONObject(result);

                    JSONArray jsonArray=jo.getJSONArray("businfo");

                    JSONObject c1=jsonArray.getJSONObject(0);
                    String src=c1.getString("busSource");
                    String des=c1.getString("busDestination");

                    t1.setText(src);
                    t2.setText(des);

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject c=jsonArray.getJSONObject(i);

                        DispMid s1=new DispMid();

                        String name=c.getString("middlers");

                        s1.setMid(name);

                        arrayList.add(s1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MidAdp midAdp=new MidAdp(getApplicationContext(),arrayList);
                midAdp.notifyDataSetChanged();
                lv.setAdapter(midAdp);
            }
        }
    }
public class getBusId extends  AsyncTask<String,Void,String>
{

    ArrayList<String> arrayList=new ArrayList<>();
    String jsonurl="http://raju1221.dx.am/fetchBusId.php";
    public getBusId() {
        super();
    }

    @Override
    protected String doInBackground(String[] params) {

    DefaultHttpClient httpClient=new DefaultHttpClient();

    HttpPost httpPost=new HttpPost(jsonurl);
    try {
        HttpResponse response=httpClient.execute(httpPost);

        HttpEntity entity=response.getEntity();

        is=entity.getContent();

    } catch (IOException e) {
        e.printStackTrace();
    }

    try {
        BufferedReader reader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);

        StringBuilder sb=new StringBuilder();

        String line="";

        while ((line=reader.readLine())!=null)
        {
            sb.append(line+"\n");
        }

        is.close();

        json=sb.toString();

    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return json;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            Log.i("tagconvertstr", "["+result+"]");
            JSONObject jo=new JSONObject(result);

            JSONArray jsonArray=jo.getJSONArray("busno");
            arrayList.add("Select Bus No");

            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject c=jsonArray.getJSONObject(i);

                String name=c.getString("busNo");

                arrayList.add(name);

            }
            ArrayAdapter aa=new ArrayAdapter(MainActivity22.this,android.R.layout.simple_spinner_item,arrayList);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp.setAdapter(aa);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


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
            return arrayList.get(position).getMid();
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
              lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      String value=parent.getItemAtPosition(position).toString();
                      l1=value;
                      //Toast.makeText(context,value,Toast.LENGTH_LONG).show();
                      AsyncTaskJson1 aj1=new AsyncTaskJson1();
                      aj1.execute();
                  }
              });


            return convertView;
        }
    }




    public class AsyncTaskJson1 extends AsyncTask<String,Void,String> {

        String jsonurl="http://raju1221.dx.am/temp.php";

        Dialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading= ProgressDialog.show(MainActivity22.this, "Please Wait", "Loading..");
        }

        @Override
        protected String doInBackground(String[] params) {

            ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();

            al.add(new BasicNameValuePair("mid",l1.toString()));
            Log.i("L data:", "[" + l1.toString() + "]");


            DefaultHttpClient httpClient=new DefaultHttpClient();

            HttpPost httpPost=new HttpPost(jsonurl);
            try{
                httpPost.setEntity(new UrlEncodedFormEntity(al));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                HttpResponse response=httpClient.execute(httpPost);

                HttpEntity entity=response.getEntity();

                is=entity.getContent();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);

                StringBuilder sb=new StringBuilder();

                String line="";

                while ((line=reader.readLine())!=null)
                {
                    sb.append(line+"\n");
                }

                is.close();

                json=sb.toString();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();
            ArrayList<DispNo> arrayList=new ArrayList<DispNo>();
            AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(MainActivity22.this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.list, null);
            alertDialog1.setView(convertView);
            alertDialog1.setTitle("This buses runs here:");
            ListView lv1 = (ListView) convertView.findViewById(R.id.listView1);
            Log.i("Result:", "[" + result + "]");

            String str=result.trim();

            if (str.equalsIgnoreCase("norecord"))
            {
                String[] temp={"Enter Stop name!!!"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,temp);
                lv1.setAdapter(adapter);
                alertDialog1.show();

            }
            else
            {
                try {
                    Log.i("tagconvertstr", "[" + result + "]");
                    JSONObject jo=new JSONObject(result);

                    JSONArray jsonArray=jo.getJSONArray("busno");

//                    JSONObject c1=jsonArray.getJSONObject(0);
//                    String src=c1.getString("busSource");
//                    String des=c1.getString("busDestination");


                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject c=jsonArray.getJSONObject(i);

                        DispNo s1=new DispNo();

                        String name=c.getString("busNo");

                        s1.setNo(name);

                        arrayList.add(s1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NoAdp midAdp=new NoAdp(getApplicationContext(),arrayList);
                midAdp.notifyDataSetChanged();
                lv1.setAdapter(midAdp);
                alertDialog1.show();

            }
        }
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
                                new getBusId().execute();

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
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity22.this).create();

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
