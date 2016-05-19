package com.example.sony.citybusmanagement;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sony.citybusmanagement.adapter.BusAdp;
import com.example.sony.citybusmanagement.model.DispBus;

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

public class MainActivity26 extends AppCompatActivity {
    private AutoCompleteTextView atv;
    private AutoCompleteTextView atv1;
    private EditText e1,e2,e3,e4;
    private Button btnGo;
    static String json="";
    static InputStream is=null;
    public static final String TAG = "Error";
    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;

    Double l1,l2,l3,l4;
Button bt;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main26);
//        e1=(EditText)findViewById(R.id.lat1);
//        e2=(EditText)findViewById(R.id.lng1);
//        e3=(EditText)findViewById(R.id.lat2);
//        e4=(EditText)findViewById(R.id.lng2);
        atv = (AutoCompleteTextView) findViewById(R.id.et_place);
        atv1 = (AutoCompleteTextView) findViewById(R.id.et_place1);
        btnGo = (Button) findViewById(R.id.btn_show);
        new getBusId().execute();
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(atv.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Enter the starting point", Toast.LENGTH_SHORT).show();
                } else if ("".equals(atv1.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Enter the destination point", Toast.LENGTH_SHORT).show();
                } else {
                    new AsyncTaskJson().execute();
                    Intent intent = new Intent(MainActivity26.this, MapActivity.class);
                    intent.putExtra("FROM", atv.getText().toString().trim());
                    intent.putExtra("TO", atv1.getText().toString().trim());
                    intent.putExtra("l1",l1);
                    intent.putExtra("l2",l2);
                    intent.putExtra("l3",l3);
                    intent.putExtra("l4",l4);

                    v.getContext().startActivity(intent);
                }
            }
        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#004D40"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00695C")));

    }

    public class getBusId extends AsyncTask<String,Void,String>
    {
        ArrayList<String> arrayList=new ArrayList<>();
        ArrayList<String> arrayList1=new ArrayList<>();

        String jsonurl="http://192.168.1.162/test/fetchMapMid.php";
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
                Log.i("tagconvertstr", "[" + result + "]");
                JSONObject jo=new JSONObject(result);

                JSONArray jsonArray=jo.getJSONArray("mid");

                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject c=jsonArray.getJSONObject(i);

                    String src=c.getString("middlers");

                    arrayList.add(src);

                    String des=c.getString("middlers");

                    arrayList1.add(des);
                }

                ArrayAdapter aa=new ArrayAdapter(MainActivity26.this,android.R.layout.simple_spinner_dropdown_item,arrayList);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                atv.setThreshold(1);
                atv.setAdapter(aa);
                ArrayAdapter aa1=new ArrayAdapter(MainActivity26.this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
                aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                atv1.setThreshold(1);
                atv1.setAdapter(aa1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class AsyncTaskJson extends AsyncTask<String,Void,String> {
        String jsonurl="http://192.168.1.162/test/fetchMapLat.php";

        Dialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading= ProgressDialog.show(MainActivity26.this, "Please Wait", "Loading..");
        }

        @Override
        protected String doInBackground(String[] params) {
            ArrayList<NameValuePair> al=new ArrayList<NameValuePair>();

            al.add(new BasicNameValuePair("src",atv.getText().toString()));

            al.add(new BasicNameValuePair("des",atv1.getText().toString()));

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
            ArrayList<DispBus> arrayList=new ArrayList<DispBus>();

            if (str.equalsIgnoreCase("norecord"))
            {
                Toast.makeText(getApplicationContext(),"No data",Toast.LENGTH_LONG).show();
            }
            else
            {
                try {
                    JSONObject jo=new JSONObject(result);
                    Log.i("onPostExecute:", "[" + result + "]");

                    JSONArray jsonArray=jo.getJSONArray("src");
                        JSONObject c=jsonArray.getJSONObject(0);

                        String latSrc=c.getString("lat");
                        String lngSrc=c.getString("lng");
                    Log.i("lat1:", "[" +latSrc+"]");
                    Log.i("lng1:", "[" +lngSrc+"]");

                    JSONArray jsonArray1=jo.getJSONArray("des");
                    JSONObject c1=jsonArray1.getJSONObject(0);

                    String latDes=c1.getString("lat");
                    String lngDes=c1.getString("lng");
                    Log.i("lat2:", "[" +latDes+"]");
                    Log.i("lng2:", "[" +lngDes+"]");
             l1=Double.parseDouble(latSrc);
             l2=Double.parseDouble(lngSrc);
             l3=Double.parseDouble(latDes);
             l4=Double.parseDouble(lngDes);
                    Log.i("l1:", "[" +l1+"]");
                    Log.i("l2:", "[" +l2+"]");
                    Log.i("l3:", "[" +l3+"]");
                    Log.i("l4:", "[" +l4+"]");

                } catch (JSONException e) {
                   e.printStackTrace();
                }
//                atv.setText(l1+","+l2);
//                atv1.setText(l3+","+l4);
//
//                ArrayAdapter aa=new ArrayAdapter(MainActivity26.this,android.R.layout.simple_spinner_dropdown_item,arrayList);
//                ArrayAdapter aa1=new ArrayAdapter(MainActivity26.this,android.R.layout.simple_spinner_dropdown_item,arrayList);
//              aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                atv.setThreshold(1);

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
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity26.this).create();

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