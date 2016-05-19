package com.example.sony.citybusmanagement;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity25 extends AppCompatActivity {
    public static final String TAG = "Error";
    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;

    InputStream is1 = null;
    static JSONObject jobject = null;
    static String jstr = "";
    String a,b,c;
    EditText edit1, edit2,edit3;
    Button b1;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main25);
//        RunTask();
        edit1 = (EditText) findViewById(R.id.et1);
        edit2 = (EditText) findViewById(R.id.et2);
        edit3 = (EditText) findViewById(R.id.et3);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        b1 = (Button) findViewById(R.id.bt1);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#004D40"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00695C")));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = edit1.getText().toString();
                b = edit2.getText().toString();
                c = edit3.getText().toString();

                Inserted ins = new Inserted();
                ins.execute();
                Toast.makeText(getApplicationContext(), "inserted", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void RunTask() {
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

            //Toast.makeText(getApplicationContext(),"Connected",8000).show();

        } else {

//            Toast.makeText(getApplicationContext(), "You are not online!!!!", 8000).show();
//            Log.v("Home", "############################You are not online!!!!");
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity25.this).create();

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
                NavUtils.navigateUpFromSameTask(MainActivity25.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
   /* public void sendMessage5(View view)
    {
        Intent intent = new Intent(MainActivity25.this, Main2Activity.class);
        startActivity(intent);
    }*/


public class Inserted extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        String jsonurl = "http://raju1221.dx.am/fbins.php";
        ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
        al.add(new BasicNameValuePair("un", a));
        al.add(new BasicNameValuePair("mail",b));
        al.add(new BasicNameValuePair("des",c));

        Log.e("Setrecord", al.toString());
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(jsonurl);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(al));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            is1 = entity.getContent();


        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is1, "iso-8859-1"), 8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb1 = new StringBuilder();
        String line1 = "";

        try {
            while ((line1 = reader.readLine()) != null) ;
            {
                sb1.append(line1 + "\n");
                Log.e("Hello", sb1.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        jstr = sb1.toString();
        Log.e("kkk", jstr.toString());

        try {
            jobject = new JSONObject(jstr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jobject;


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
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity25.this).create();

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
