package com.saratrozan.io;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import io.paperdb.Paper;

public class MainActivity extends Activity {


    private WebView wv1;
    ImageView imageView;
    String ipAddress;
    SwipeRefreshLayout swipeLayout;
    AnimatedCircleLoadingView animatedCircleLoadingView;
    FloatingActionButton floatingActionButton;
    RelativeLayout relativeLayout;
    String current_URL;
    TextView textView;
    boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        Paper.init(this);
        textView=(TextView)findViewById(R.id.textView);
        current_URL=Paper.book().read("IP").toString();
        floatingActionButton=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wv1.reload();
            }
        });

        relativeLayout=(RelativeLayout)findViewById(R.id.layout);
        animatedCircleLoadingView=(AnimatedCircleLoadingView)findViewById(R.id.circle_loading_view);
        animatedCircleLoadingView.startIndeterminate();
        imageView=(ImageView)findViewById(R.id.img_menu);
        if(!isConnected()){
            Toast.makeText(this, "Please Connect to Network.. (Mobile Data or WIFI)", Toast.LENGTH_LONG).show();
        }

        wv1=(WebView)findViewById(R.id.webView);
        wv1.setVisibility(View.GONE);
        wv1.setWebViewClient(new MyBrowser());
        Paper.init(this);
        ipAddress=Paper.book().read("IP");
        if(ipAddress==null){
            startActivity(new Intent(getApplicationContext(),UrlConsoleActivity.class));
        }

        else{
            wv1.getSettings().setLoadsImagesAutomatically(true);
            wv1.getSettings().setJavaScriptEnabled(true);
            wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            wv1.loadUrl("http://"+ipAddress);

            Log.e("url","http://"+ipAddress);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UrlConsoleActivity.class));
            }
        });

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            current_URL=url;
            if(isConnected())
            return super.shouldOverrideUrlLoading(view, url);
            else{
                Toast.makeText(MainActivity.this, "Please Connect to the Network.. (Mobile Data or WIFI)", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            view.loadUrl("about:blank");
            Toast.makeText(getApplicationContext(),error.getDescription(),Toast.LENGTH_LONG).show();
            Intent intent=new Intent(new Intent(getApplicationContext(),UrlConsoleActivity.class));
            intent.putExtra("error","toast");
            startActivity(intent);
            finish();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            wv1.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            floatingActionButton.setVisibility(View.VISIBLE);
            animatedCircleLoadingView.stopOk();

        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (wv1.canGoBack()) {
                        wv1.goBack();
                    } else {
                        Intent intent=new Intent(getApplicationContext(),UrlConsoleActivity.class);
                       // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
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