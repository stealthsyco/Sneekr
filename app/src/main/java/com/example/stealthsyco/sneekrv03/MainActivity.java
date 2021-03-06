package com.example.stealthsyco.sneekrv03;

// Author - Kenneth Smith


import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity {

    // String to hold proxy numbers
    private String info;

    // Progress Dialog
    private ProgressDialog pDialog;

    // ArrayList to hold the servers before implementing them into the ListView
    ArrayList<HashMap<String, String>> serverList;

    // ListAdapter for onResume() and onPostExecute()
    ListAdapter adapter;

    // Tag for use later
    private static final String TAG_INFO = "info";

    int onResumeCount = 0;
    String url = "http://68.63.210.188/androidConnect/getProxies.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.split_view);

        // Set info to null
        info = null;
        // Initialize the ArrayList
        serverList = new ArrayList<HashMap<String, String>>();
        adapter = null;

        // Call new background process
        FetchData create = new FetchData();
        create.execute();

        // When an item is clicked, save the data into the variable info for later,,,
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> map = serverList.get(position);
                info = map.get("info");
            }

        });

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view){

        String server = info;
        if(info == null) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Oops");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Please select a proxy from the list")
                    .setCancelable(false)
                    .setPositiveButton("Alright...", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            dialog.dismiss();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else {

            EditText et = (EditText) findViewById(R.id.address);
            String webAddress = et.getText().toString();
            Intent intent = new Intent(MainActivity.this, WebActivity.class);
            intent.putExtra("text_label", webAddress);
            intent.putExtra("port_label", server);
            intent.putExtra("onResumeCount", onResumeCount);

            startActivity(intent);
        }
    }


    // Creates a new thread to connect and load the information asynchronously

    class FetchData extends AsyncTask<ArrayList<String>, ArrayList<String>, ArrayList<String>> {
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading proxies. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            //setup stuff here...
        }

        @Override
        protected ArrayList<String> doInBackground(ArrayList<String>... strings) {
            ArrayList<String> items = new ArrayList<String>();

            try {

                URL conURL = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) conURL.openConnection();
                urlConnection.setRequestMethod("GET");
                //urlConnection.setRequestProperty("Content-length", "0");
                urlConnection.setUseCaches(false);
                urlConnection.setAllowUserInteraction(false);
                urlConnection.setConnectTimeout(25000);
                urlConnection.setReadTimeout(25000);
                urlConnection.connect();
                int status = urlConnection.getResponseCode();
                System.out.println(status);
                switch (status){
                    case 200:
                    case 201:
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String next;
                        while ((next = bufferedReader.readLine()) != null) {

                            Log.d("MyActivity", "First Loop Going...");
                            JSONObject ja = new JSONObject(next);
                            JSONArray servers = ja.getJSONArray("Servers");
                            urlConnection.disconnect();
                            Log.d("MyActivity", "It made it here");
                            for (int i = 0; i < servers.length(); i++) {

                                Log.d("MyActivity", "Second loop going...");

                                JSONObject c = servers.getJSONObject(i);
                                String info = c.getString(TAG_INFO);

                                //Create new HashMap
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(TAG_INFO, info);

                                serverList.add(map);
                            }
                        }
                        break;

                    default:
                        Log.d("MyActivity", "We had an error code besides 201 or 200, fix the server");

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("MyActivity", "Malformed URL");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("MyActivity", "Caught IOException");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("MyActivity", "JSON isn't in the correct form");
            } catch (Exception e){
                e.printStackTrace();
                Log.d("MyActivity", "I don't know what this means?");

            }

            return items;
        }

        protected void onPostExecute(ArrayList<String> result){

            pDialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {

                    Log.d("Main", "Does this happen?");
                    adapter = new SimpleAdapter(MainActivity.this, serverList, R.layout.list_item, new String[] { TAG_INFO, TAG_INFO }, new int[] { R.id.info, R.id.info } );
                    setListAdapter(adapter);

                }
            });
        }

    }
}
