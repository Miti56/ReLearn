package com.example.coursework.ui.home;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import static android.R.id.message;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.coursework.HelpActivity;
import com.example.coursework.MainActivity;
import com.example.coursework.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class HomeFragment extends Fragment implements View.OnClickListener {
    TextView textview;

    private HomeViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        //WebpageLongPress
        Button b = (Button) root.findViewById(R.id.onClickOpenWebpageButton);
        b.setOnClickListener(this);
        registerForContextMenu(b);
        //GO somewhere Button LOng Press
        Button btn = (Button) root.findViewById(R.id.btnShow);
        registerForContextMenu(btn);
        btn.setOnClickListener(this);
        //Share ETA long press
        Button d = (Button) root.findViewById(R.id.onClickShareTextButton);
        registerForContextMenu(d);
        d.setOnClickListener(this);

        //clock for the home
        textview = root.findViewById(R.id.textview);
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:MM");
        textview.setText(formatter.format(today));

        return root;


    }


    //function to open google maps
    private void showMap(Uri geoLocation) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        startActivity(intent);

    }

    //function to share text with chooser
    private void shareText(String textToShare) {
        Context mcontext = getActivity();
        String mimeType = "text/plain";
        String title = "Im leaving from work, shold be there in 10 mins";
        // Use ShareCompat.IntentBuilder to build the Intent and start the chooser
        ShareCompat.IntentBuilder
                .from((Activity) mcontext)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(textToShare)
                .startChooser();

    }

    //function to send SMS
    protected void sendSMS() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", new String(readFromFile(getActivity())));
        smsIntent.putExtra("sms_body", "Hello, Im leaving now, will be back later! ");

        //try sending sms
        try {
            startActivity(smsIntent);
            Log.i("Finished sending SMS...", "");
            //i fail open app sms at least
        } catch (android.content.ActivityNotFoundException ex) {
            Context mcontext = getActivity();
            startNewActivity(mcontext, "com.google.android.apps.messaging");
            Toast.makeText((Activity) mcontext, "SMS failed, Google Messenger App not supported", Toast.LENGTH_SHORT).show();
        }
    }




    //context menu for long press button 1
    @Override
    public void onCreateContextMenu(ContextMenu menu, View root, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, root, menuInfo);
        Button btn = root.findViewById(R.id.btnShow);
        Button b = root.findViewById(R.id.onClickOpenWebpageButton);
        Button d = root.findViewById(R.id.onClickShareTextButton);

        //setup for each long press
        if (root == btn) {
            menu.setHeaderTitle("Let's go");
            menu.add(0, root.getId(), 0, "GO HOME");
            menu.add(0, root.getId(), 0, "GO WORK");
            menu.add(0, root.getId(), 0, "GO FOOTBALL");
            menu.add(0, root.getId(), 0, "GO PARK");
        } else if (root == b) {
            menu.setHeaderTitle("Enjoy!");
            menu.add(0, root.getId(), 0, "GO INSTA");
            menu.add(0, root.getId(), 0, "GO NEWS");
            menu.add(0, root.getId(), 0, "GO YT");
            menu.add(0, root.getId(), 0, "GO TWITCH");
        } else if (root == d) {
            menu.setHeaderTitle("Better Safe than Sorry!");
            menu.add(0, root.getId(), 0, "SHARE GENERAL");

        }
    }

    //what to do when long press button
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle() == "GO HOME") {
            Context mcontext = getActivity();
            Toast.makeText((Activity) mcontext, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            String addressString = "Home";

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("geo")
                    .appendPath("0,0")
                    .appendQueryParameter("q", addressString);
            Uri addressUri = builder.build();
            showMap(addressUri);
            return true;
        } else if (item.getTitle() == "GO WORK") {
            Context mcontext = getActivity();
            Toast.makeText((Activity) mcontext, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            String addressString = "Work";

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("geo")
                    .appendPath("0,0")
                    .appendQueryParameter("q", addressString);
            Uri addressUri = builder.build();
            showMap(addressUri);
            return true;
        } else if (item.getTitle() == "GO FOOTBALL") {
            Context mcontext = getActivity();
            Toast.makeText((Activity) mcontext, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            String addressString = "Exeter City Football Club, Exeter UK";

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("geo")
                    .appendPath("0,0")
                    .appendQueryParameter("q", addressString);
            Uri addressUri = builder.build();
            showMap(addressUri);
            return true;
        } else if (item.getTitle() == "GO PARK") {
            Context mcontext = getActivity();
            Toast.makeText((Activity) mcontext, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            String addressString = "Parks Nearby";

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("geo")
                    .appendPath("0,0")
                    .appendQueryParameter("q", addressString);
            Uri addressUri = builder.build();
            showMap(addressUri);
            return true;
        } else if (item.getTitle() == "SHARE GENERAL") {
            String textThatYouWantToShare = "Hello! I'm heading out.";
            shareText(textThatYouWantToShare);
            return true;
        } else if (item.getTitle() == "GO INSTA") {
            Context mcontext = getActivity();
            Toast.makeText((Activity) mcontext, "INSTA ", Toast.LENGTH_SHORT).show();
            startNewActivity(mcontext, "com.instagram.android");
            return true;
        } else if (item.getTitle() == "GO NEWS") {
            Context mcontext = getActivity();
            Toast.makeText((Activity) mcontext, "NEWS ", Toast.LENGTH_SHORT).show();
            startNewActivity(mcontext, "bbc.mobile.news.ww");
            return true;
        } else if (item.getTitle() == "GO YT") {
            Context mcontext = getActivity();
            Toast.makeText((Activity) mcontext, "YT ", Toast.LENGTH_SHORT).show();
            startNewActivity(mcontext, "com.google.android.youtube");
            return true;
        } else if (item.getTitle() == "GO TWITCH") {
            Context mcontext = getActivity();
            Toast.makeText((Activity) mcontext, "TWITCH ", Toast.LENGTH_SHORT).show();
            startNewActivity(mcontext, "tv.twitch.android.app");
            return true;
        } else {
            return false;
        }
    }


    //start the app that the user wants
    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        Context mcontext = getActivity();
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            Toast.makeText((Activity) mcontext, "APP NOT FOUND: Opening Google PLay ", Toast.LENGTH_SHORT).show();
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));

        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //function to read data from text file
    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        Toast.makeText(getActivity(), "Sending SMS to: " + ret , Toast.LENGTH_SHORT).show();
        return ret;
    }



    //what to do when button click
    @Override
    public void onClick(View root) {
        final int min = 1;
        final int max = 4;
        final int random = new Random().nextInt((max - min) + 1) + min;

        switch (root.getId()) {
            case R.id.btnShow:
                String addressString = "Places Nearby";
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("geo")
                        .appendPath("0,0")
                        .appendQueryParameter("q", addressString);
                Uri addressUri = builder.build();
                showMap(addressUri);
                break;
            case R.id.onClickShareTextButton:
                String fileNumber = readFromFile(getActivity());
                sendSMS();
                break;
            case R.id.onClickOpenWebpageButton :
                int b = random;
                if (b == 1) {
                    Context mcontext = getActivity();
                    Toast.makeText((Activity) mcontext, "INSTA ", Toast.LENGTH_SHORT).show();
                    startNewActivity(mcontext, "com.instagram.android");

                } else if (b == 2) {
                    Context mcontext = getActivity();
                    Toast.makeText((Activity) mcontext, "NEWS ", Toast.LENGTH_SHORT).show();
                    startNewActivity(mcontext, "bbc.mobile.news.ww");

                } else if (b == 3) {
                    Context mcontext = getActivity();
                    Toast.makeText((Activity) mcontext, "YT ", Toast.LENGTH_SHORT).show();
                    startNewActivity(mcontext, "com.google.android.youtube");

                } else if (b == 4) {
                    Context mcontext = getActivity();
                    Toast.makeText((Activity) mcontext, "TWITCH ", Toast.LENGTH_SHORT).show();
                    startNewActivity(mcontext, "tv.twitch.android.app");
                }
                break;

        }
    }
}


