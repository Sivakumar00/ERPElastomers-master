package com.saratrozan.io;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import io.paperdb.Paper;

public class UrlConsoleActivity extends AppCompatActivity {

    EditText editText;
    Button btnSubmit;
    ImageView imageView;
    int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_console);
        Paper.init(this);
        imageView=(ImageView)findViewById(R.id.imageView);
        editText=(EditText)findViewById(R.id.edtIpaddress);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);



        String IP=Paper.book().read("IP");
        if(IP!=null){
            editText.setText(IP);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter=counter+1;
                if(counter==3){

                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(UrlConsoleActivity.this);

                    // set the message to display
                    alertbox.setTitle("Developer Team");
                    alertbox.setMessage("Thayalan G R"+"\n"+"Raaja"+"\n"+"Sivakumar A");
                    alertbox.setIcon(R.drawable.ic_image);
                    alertbox.setCancelable(true);

                    // add a neutral button to the alert box and assign a click listener
                    alertbox.setNeutralButton("Okay", new DialogInterface.OnClickListener() {

                        // click listener on the alert box
                        public void onClick(DialogInterface arg0, int arg1) {
                            // the button was clicked
                            counter=0;
                        }
                    });

                    // show it
                    alertbox.show();
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp=editText.getText().toString();
                if(isConnected()) {
                    if (!TextUtils.isEmpty(temp)) {
                        Paper.book().write("IP", editText.getText().toString());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Enter the IP Address", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UrlConsoleActivity.this, "Please Connect To the Network.. (Mobile Data or WIFI", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Are you really want to Exit from app?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }
}
