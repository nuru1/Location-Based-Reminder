package com.example.asif.idiot;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TimeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    FirebaseHelper helper;

    EditText time,date,msg;
    //Spinner spinner;
    int C_hour,C_min;
    int C_year,C_month,C_date;

    int S_hour,S_min;
    int S_year,S_month,S_date,S_ID;
    int S_repeat;
    String S_message,S_type;
    Button save;
    //ArrayAdapter<String> repeatAdapter;

    String[] Days = new String[]{
            "Once",
            "Daily",
            "Every Monday",
            "Every Tuesday",
            "Every Wednesday",
            "Every Thursday",
            "Every Friday",
            "Every Saturday",
            "Every Sunday"
    };

    String[] type=new String[]{};



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        time = (EditText)findViewById(R.id.set_time_button);
        date = (EditText)findViewById(R.id.set_date_button);
        msg= (EditText)findViewById(R.id.add_reminder_msg);
        save = (Button)findViewById(R.id.save_time);
        helper = new FirebaseHelper();

        set_onView();
        set_Current_DateAndTime();
        set_spinners();

        ///Setting Spinners

    }

    private void set_spinners() {

        Spinner spinner = (Spinner) findViewById(R.id.spinner_repeat);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Repeat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Fragment_Suggestions obj=new Fragment_Suggestions();
        type=obj.getLoc_type();

        Spinner spinner_type=(Spinner)findViewById(R.id.spinner_type);
        spinner_type.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(aa);

    }

    private void set_onView() {

        msg.setText("");
        Date d=new Date();
        SimpleDateFormat timeFormat=new SimpleDateFormat("hh:mm a");
        String current_time = timeFormat.format(d);
        time.setText(current_time);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE,  MMM dd, yyyy");
        String current_date=dateFormat.format(d);
        date.setText(current_date);
    }

    private void set_Current_DateAndTime() {
        Date d=new Date();
        SimpleDateFormat Hour=new SimpleDateFormat("H");
        C_hour = Integer.parseInt(Hour.format(d));
        S_hour=C_hour;

        SimpleDateFormat Min=new SimpleDateFormat("mm");
        C_min = Integer.parseInt(Min.format(d));
        S_min=C_min;

        SimpleDateFormat Year=new SimpleDateFormat("yyyy");
        C_year = Integer.parseInt(Year.format(d));
        S_year=C_year;

        SimpleDateFormat month=new SimpleDateFormat("MM");
        C_month = Integer.parseInt(month.format(d));
        S_month=C_month;

        SimpleDateFormat dayofmonth=new SimpleDateFormat("dd");
        C_date = Integer.parseInt(dayofmonth.format(d));
        S_date=C_date;
    }

    public void add_time(View view) {
       final EditText time =(EditText)findViewById(R.id.set_time_button);
        Calendar mCurrentTime= Calendar.getInstance();
        int hour= mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute=mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                time.setText(selectedHour+":"+selectedMinute);
                S_hour=selectedHour;
                S_min=selectedMinute;

            }
        },hour,minute,true);
        timePickerDialog.setTitle("Set Time");
        timePickerDialog.show();

    }

    public void add_date(View view) {
        final EditText date=(EditText)findViewById(R.id.set_date_button);
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                S_year=year;
                S_month=monthOfYear+1;
                S_date=dayOfMonth;
                String selected_date = (S_date +" : "+S_month +" : "+S_year);
                date.setText(selected_date);

            }
        }, yy, mm, dd);
        datePicker.show();
    }

    public void save_time(View view) {

        if (S_year < C_year) {
            Toast.makeText(getApplicationContext(), "Enter correct Time", Toast.LENGTH_LONG).show();
        } else if ((S_year <= C_month) && (C_month < S_month)) {
            Toast.makeText(getApplicationContext(), "Enter correct Time", Toast.LENGTH_LONG).show();
        } else if ((S_year <= C_year) && (S_month <= C_month) && (S_date < C_date)) {
            Toast.makeText(getApplicationContext(), "Enter correct Time", Toast.LENGTH_LONG).show();
        } else if ((S_year <= C_year) && (S_month == C_month) && (S_date == C_date) && (S_hour < C_hour)) {
            Toast.makeText(getApplicationContext(), "Enter correct Time", Toast.LENGTH_LONG).show();
        } else if ((S_year <= C_year) && (S_month <= C_month) && (S_date <= C_date) && (S_hour <= C_hour) && (S_min <= C_min)) {
            Toast.makeText(getApplicationContext(), "Enter correct Time", Toast.LENGTH_LONG).show();
        } else {
            //Data inseertion into DB
            S_message=msg.getText().toString();
           Reminder_Time r= new Reminder_Time();
           r.setId(random());
           r.setS_hour(S_hour);
           r.setS_min(S_min);
           r.setS_year(S_year);
           r.setS_month(S_month);
           r.setS_date(S_date);
           r.setS_message(S_message);
           r.setS_repeat(S_repeat);
           r.setS_Value(1);
           if(helper.save(r)){
               Toast.makeText(getApplicationContext(),"entered!",Toast.LENGTH_SHORT).show();
            set_onView();
            set_Current_DateAndTime();
            Fragment_Reminders fr= new Fragment_Reminders();
               System.out.println("CALLING SET ADAPTER");
              // fr.setAdapter();
            }
            else {
               Toast.makeText(getApplicationContext(),"There is a Problem",Toast.LENGTH_SHORT).show();
           }
        }


        System.out.println("Current hour  "+C_hour);
        System.out.println("Current min  "+C_min);
        System.out.println("Current year  "+C_year);
        System.out.println("Current month  "+C_month);
        System.out.println("Current date  "+C_date);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner sp=(Spinner)adapterView;
        if(sp.getId()==R.id.spinner_repeat){
            S_repeat=i;
        }
        else if(sp.getId()==R.id.spinner_type){
            S_type=type[i];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public String random(){
        Random rn = new Random();
        int n = 9999 - 1111 + 1;
        int i = rn.nextInt() % n;
        String randomNum = String.valueOf(1111 + i);
        return randomNum;
    }
}
