package com.example.coursework.ui.zenzone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coursework.HelpActivity;
import com.example.coursework.MainActivity;
import com.example.coursework.R;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment  {


    private TextView TextViewCountDown;
    private Button ButtonSet1;
    private Button ButtonSet5;
    private Button ButtonSet10;
    private Button ButtonStartPause;
    private CountDownTimer CountDownTimer;
    private boolean TimerRunning;
    private long StartTimeInMillis;
    private long TimeLeftInMillis;
    private long EndTime;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_zenzone, container, false);

        //create view and button for the clock
        TextViewCountDown = root.findViewById(R.id.text_view_countdown);
        ButtonStartPause = root.findViewById(R.id.button_start_pause);
        ButtonSet1 = root.findViewById(R.id.minuteSet);
        ButtonSet5 = root.findViewById(R.id.fiveMinuteSet);
        ButtonSet10 = root.findViewById(R.id.tenMinuteSet);


        ButtonSet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long millisInput = 1 * 60000;
                setTime(millisInput);}

        });

        ButtonSet5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long millisInput = 5 * 60000;
                setTime(millisInput);}
        });

        ButtonSet10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long millisInput = 10 * 60000;
                setTime(millisInput);}
        });

        ButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });


        return root;
    }

    //set time for the clock
    private void setTime(long milliseconds) {
        StartTimeInMillis = milliseconds;
        resetTimer();
    }

    //function to start the time
    private void startTimer() {
        EndTime = System.currentTimeMillis() + TimeLeftInMillis;
        CountDownTimer = new CountDownTimer(TimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                TimerRunning = false;
                updateWatchInterface();
            }
        }.start();
        TimerRunning = true;
        updateWatchInterface();
    }

    // function to pause the timer
    private void pauseTimer() {
        CountDownTimer.cancel();
        TimerRunning = false;
        updateWatchInterface();
    }

    // function to reset the timer
    private void resetTimer() {
        TimeLeftInMillis = StartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    // function to update the timer
    private void updateCountDownText() {

        int minutes = (int) ((TimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (TimeLeftInMillis / 1000) % 60;

        TextViewCountDown.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }


    // function to update the timer view
    private void updateWatchInterface() {
        if (TimerRunning) {
            ButtonSet1.setVisibility(View.INVISIBLE);
            ButtonSet5.setVisibility(View.INVISIBLE);
            ButtonSet10.setVisibility(View.INVISIBLE);
            ButtonStartPause.setText("Pause");
        } else {
            ButtonSet1.setVisibility(View.VISIBLE);
            ButtonSet5.setVisibility(View.VISIBLE);
            ButtonSet10.setVisibility(View.VISIBLE);
            ButtonStartPause.setText("Start");
            if (TimeLeftInMillis < 1000) {
                ButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                ButtonStartPause.setVisibility(View.VISIBLE);
            }

        }
    }

    // function to stop the timer and give warnings to the user
    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("startTimeInMillis", StartTimeInMillis);
        editor.putLong("millisLeft", TimeLeftInMillis);
        editor.putBoolean("timerRunning", TimerRunning);
        editor.putLong("endTime", EndTime);
        editor.apply();

        if (TimerRunning) {
            Toast.makeText(getContext(), "DON'T leave the tab, YOU NEED TO STAY FOCUS ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "NO TIMER RUNNING!: Try to focus in the ZenZone ", Toast.LENGTH_SHORT).show();
        }

        if (CountDownTimer != null) {
            CountDownTimer.cancel();
        }
    }

    // function to start the timer automatically when tab is opened
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        StartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        TimeLeftInMillis = prefs.getLong("millisLeft", StartTimeInMillis);
        TimerRunning = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        updateWatchInterface();
        startTimer();


    }


}