package com.example.coursework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursework.ui.home.HomeFragment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class HelpActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpactivity);

        //buttons to go back and save
        final Button button = (Button) findViewById(R.id.buttonNext5);
        button.setOnClickListener(this);
        Button d = (Button) findViewById(R.id.enter);
        d.setOnClickListener(this);

        //input the phone number
        EditText mEdit;
        mEdit   = (EditText)findViewById(R.id.UserIutput);
        mEdit.getText().toString();

    }



    //what to do when buttons clicked
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext5:
                Context context = HelpActivity.this;
                Class destinationActivity = MainActivity.class;

                Intent intent = new Intent(context, destinationActivity);
                startActivity(intent);

                break;

            case R.id.enter:
                EditText mEdit;
                mEdit   = (EditText)findViewById(R.id.UserIutput);
                String data = mEdit.getText().toString();

                writeToFile(data,getApplicationContext());

                Context context2 = HelpActivity.this;
                Class destinationActivity2 = MainActivity.class;

                Intent intent2 = new Intent(context2, destinationActivity2);
                startActivity(intent2);

                Toast.makeText(getApplicationContext(), "Default SMS contact is: " + data, Toast.LENGTH_SHORT).show();
                break;


        }

    }

    //function to write the information of the number in the text file
    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}

