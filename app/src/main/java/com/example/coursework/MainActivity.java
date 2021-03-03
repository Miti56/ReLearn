package com.example.coursework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final int mode = Activity.MODE_PRIVATE;
    final String MYPREFS = "MyPreferences_002";

    // create a reference to the shared preferences object
    SharedPreferences mySharedPreferences;

    // obtain an editor to add data to my SharedPreferences object
    SharedPreferences.Editor myEditor;
    View myContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        myContainer = (View)findViewById(R.id.container);
        // create a reference & editor for the shared preferences object
        mySharedPreferences = getSharedPreferences(MYPREFS, 0);
        myEditor = mySharedPreferences.edit();

        // has a Preferences file been already created?
        if (mySharedPreferences != null
                && mySharedPreferences.contains("layoutColor")) {
            // object and key found, show all saved values
            applySavedPreferences();
        } else {
            Toast.makeText(getApplicationContext(), "No Preferences found", Toast.LENGTH_LONG).show();
        }

        // as the app loads show message about status internet
        if (InternetStatus.getInstance(getApplicationContext()).isOnline()) {
            Toast.makeText(getApplicationContext(), "ONLINE", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "NOT ONLINE, INTERNET FEATURES WILL NOT WORK", Toast.LENGTH_LONG).show();
        }

    }

    // function to check if internet is working
    public static class InternetStatus {
        private static InternetStatus instance = new InternetStatus();
        static Context context;
        ConnectivityManager connectivityManager;
        NetworkInfo wifiInfo, mobileInfo;
        boolean connected = false;

        public static InternetStatus getInstance(Context ctx) {
            context = ctx.getApplicationContext();
            return instance;
        }

        public boolean isOnline() {
            try {
                connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
                return connected;


            } catch (Exception e) {
                System.out.println("CheckConnectivity Exception: " + e.getMessage());
                Log.v("connectivity", e.toString());
            }
            return connected;
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onPause() {
        myEditor.putString("DateLastExecution", new Date().toLocaleString());
        myEditor.commit();
        super.onPause();
    }

    // apply settings found in shared prefs
    public void applySavedPreferences() {
        // extract the <key-value> pairs, use default param for missing data
        int layoutColor = mySharedPreferences.getInt("layoutColor",Color.WHITE);
        myContainer.setBackgroundColor(layoutColor);
    }// applySavedPreferences


    // inflate the option menus in top screen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // setup what each option of the menu does
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.help:
                Context context = MainActivity.this;

                // Store SecondActivity.class in a Class object called destinationActivity
                Class destinationActivity = HelpActivity.class;

                // Create an Intent to start SecondActivity
                Intent intent = new Intent(context, destinationActivity);

                // Start the SecondActivity
                startActivity(intent);

                return true;

            case R.id.light_theme:
                myEditor.clear();
                myEditor.putInt("layoutColor", Color.WHITE);

                myEditor.commit();
                applySavedPreferences();
                Toast.makeText(getApplicationContext(),"LIGHT THEME",Toast.LENGTH_SHORT).show();
                return true;


            case R.id.dark_theme:
                myEditor.clear();
                myEditor.putInt("layoutColor", Color.DKGRAY);

                myEditor.commit();
                applySavedPreferences();

                Toast.makeText(getApplicationContext()," DARK THEME",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }






}