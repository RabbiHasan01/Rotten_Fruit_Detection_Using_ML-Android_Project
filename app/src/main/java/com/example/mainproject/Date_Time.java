package com.example.mainproject;

import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class Date_Time extends AppCompatActivity implements View.OnClickListener {


    private TimePicker timePicker;

    private Button button;
    private TextView textView;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        getSupportActionBar().setTitle("Date And Time");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timePicker=findViewById(R.id.timePickerId);

        //date
        button= findViewById(R.id.dateButtonId);
        textView= findViewById(R.id.textId);

        button.setOnClickListener(Date_Time.this);

    }

    @Override
    public void onClick(View view) {

        DatePicker datePicker= new DatePicker(Date_Time.this);
        int currentDay=datePicker.getDayOfMonth();
        int currentMonth=datePicker.getMonth();
        int currentYear=datePicker.getYear();
        //textView.setText(currentDay+"/"+(currentMonth+1)+"/"+currentYear);

        datePickerDialog=new DatePickerDialog(Date_Time.this,

                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        textView.setText(day+"/"+(month+1)+"/"+year);
                    }
                }, currentYear, currentMonth, currentDay
        );
        datePickerDialog.show();
    }

}