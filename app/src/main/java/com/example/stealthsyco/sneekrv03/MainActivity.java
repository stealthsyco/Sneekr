package com.example.stealthsyco.sneekrv03;

// Author - Kenneth Smith

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.LinearLayout.LayoutParams;


public class MainActivity extends Activity {

    private RelativeLayout mLayout;
    private EditText mEditText;
    private ToggleButton mButton;
    private EditText toggleText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = (RelativeLayout) findViewById(R.id.mainPage);
        mEditText = (EditText) findViewById(R.id.editText);

        //toggleText = (EditText) findViewById(R.id.toggleText);

        mButton = (ToggleButton) findViewById(R.id.toggle);
        mButton.setOnClickListener(onClick());
        //TextView textView = new TextView(this);
        //textView.setText("New text");


    }


    private View.OnClickListener onClick(){
        return new View.OnClickListener() {

            @Override
            public void onClick(View v){
                if(mButton.isChecked()) {
                    mLayout.addView(createNewTextView(mEditText.getText().toString()));
                    mLayout.addView(createNewEditText());
                } else {

                    mLayout.removeView(textView);
                    mLayout.removeView(mEditText);
                }
            }
        };
    }

    private TextView createNewTextView(String text){
        final LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText("New text: " + text);
        return textView;
    }

    private EditText createNewEditText(){
        final LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mEditText = new EditText(this);
        mEditText.setLayoutParams(lparams);
        return mEditText;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    public void sendMessage(View view){

        String server;

        EditText et = (EditText) findViewById(R.id.editText);
        String theText = et.getText().toString();
        Intent intent = new Intent(MainActivity.this, WebActivity.class);
        intent.putExtra("text_label", theText);

        if(mButton.isChecked()) {
            server = mEditText.getText().toString();
        } else {
            server = "199.200.120.36:3127";
        }

        intent.putExtra("port_label", server);
        startActivity(intent);
    }


    public void onToggleClicked(View view){

    }
}
