package com.example.stealthsyco.sneekrv03;

// Author: Kenneth Smith

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebActivity extends Activity {

    private String uriString;
    private String serverString;
    private String server;
    private int port;
    private String[] parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Bundle bundle = getIntent().getExtras();
        uriString = bundle.getString("text_label");
        Log.d("WebActivity", uriString);
        serverString = bundle.getString("port_label");
        Log.d("Server and Port", serverString);

        parts = serverString.split("\\:");
        server = parts[0];
        port = Integer.parseInt(parts[1]);

        Log.d("Server", server);
        Log.d("Port", "port: " + port);

        //set proxy
        ProxySettings proxy = new ProxySettings();
        proxy.setProxy(this, server, port);

        WebView myWebView = (WebView) findViewById(R.id.webView1);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        if(uriString.isEmpty()){
            myWebView.loadUrl("http://www.whatismyipaddress.com");
        } else {
            myWebView.loadUrl(uriString);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(WebActivity.this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
