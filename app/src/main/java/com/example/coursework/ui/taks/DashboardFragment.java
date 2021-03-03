package com.example.coursework.ui.taks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.coursework.AddTaskActivity;
import com.example.coursework.R;
import com.example.coursework.seeList;

public class DashboardFragment extends Fragment implements View.OnClickListener{

    private DashboardViewModel dashboardViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        //create buttons to go to the list and add an item
        Button z = (Button) root.findViewById(R.id.goList);
        z.setOnClickListener(this);
        Button x = (Button) root.findViewById(R.id.addItem);
        x.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View root) {

        switch (root.getId()) {
            case R.id.goList:
                Context context =getContext();

                // Store seeList.class in a Class object called destinationActivity
                Class destinationActivity = seeList.class;

                // Create an Intent to start destination activity
                Intent intent = new Intent(context, destinationActivity);

                // Start the SecondActivity
                startActivity(intent);
                break;
            case R.id.addItem:
                Context context2 =getContext();

                // Store addtaskactivity.class in a Class object called destinationActivity
                Class destinationActivity2 = AddTaskActivity.class;

                // Create an Intent to start SecondActivity
                Intent intent2 = new Intent(context2, destinationActivity2);

                // Start the SecondActivity
                startActivity(intent2);
                break;
        }
    }
}