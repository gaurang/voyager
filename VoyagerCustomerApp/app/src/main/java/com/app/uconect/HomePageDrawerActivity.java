package com.app.uconect;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.app.uconect.websocket.de.roderick.weberknecht.ListenerSubscription;
import com.app.uconect.websocket.de.roderick.weberknecht.ListenerWSNetwork;
import com.app.uconect.websocket.de.roderick.weberknecht.Stomp;
import com.app.uconect.websocket.de.roderick.weberknecht.Subscription;

import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class HomePageDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        LinearLayout navigationView = (LinearLayout) findViewById(R.id.nav_view);
        navigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //navigationView.setNavigationItemSelectedListener(this);
        int width = getResources().getDisplayMetrics().widthPixels;
        Log.d("width ", width + "");
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = width;
        navigationView.setLayoutParams(params);
        connectWebSocket();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        connection(urlname + ":8080/uc/portfolio", "/topic/greeting");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("http://192.168.1.119:8080/uc/portfolio");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        connection(urlname + ":8080/uc/portfolio", "/topic/greeting");

    }

    String urlname = "ws://192.168.1.119";

    private void connection(String hostUrl, String testUrl) {
//        Map<String, String> headersSetup = new HashMap<String, String>();
//        Stomp stomp = new Stomp(hostUrl, headersSetup, new ListenerWSNetwork() {
//            @Override
//            public void onState(int state) {
//                Log.d("connect123 body", state + "");
//            }
//        });
//        stomp.connect();
//        stomp.subscribe(new Subscription(testUrl, new ListenerSubscription() {
//            @Override
//            public void onMessage(Map<String, String> headers, String body) {
//                Log.d("connect123 body", body);
//            }
//        }));
        new AsyncTaskcall().execute();
    }
    Stomp stomp;
    class AsyncTaskcall extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("connect123 AsyncT", "onPostExecute");
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            Socket socket=new Socket("");
            Map<String, String> headersSetup = new HashMap<String, String>();
            headersSetup.put("Connection", "Upgrade");
            headersSetup.put("heart-beat", "0,0");
              stomp = new Stomp(urlname + ":8080/uc/portfolio", headersSetup, new ListenerWSNetwork() {
                @Override
                public void onState(int state) {
                    Log.d("connect123 body", state + "");
                }
            });
            stomp.connect();
            stomp.subscribe(new Subscription("/topic/greeting", new ListenerSubscription() {
                @Override
                public void onMessage(Map<String, String> headers, String body) {
                    Log.d("connect123 body", body);
                }
            }));

            return null;
        }
    }

    public void sendMessage(View view) {
//        connection("ws://192.168.1.102:8080/uc/portfolio", "/topic/greeting");
        Map<String, String> headersSetup = new HashMap<String, String>();
        headersSetup.put("Connection", "Upgrade");
        headersSetup.put("heart-beat", "0,0");
        stomp.send("/topic/greeting",headersSetup,"{\"name\":\"\"}");
    }


}
