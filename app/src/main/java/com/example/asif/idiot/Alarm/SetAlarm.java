package com.example.asif.idiot.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.example.asif.idiot.FirebaseHelper;
import com.example.asif.idiot.MainActivity;
import com.example.asif.idiot.Reminder_Time;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;


public class SetAlarm {

    private static String TAG="SetALarm";
    Context context;
    ArrayList<Reminder_Time> reminder_time = new ArrayList<>();

    public SetAlarm(ArrayList<Reminder_Time> reminder_time) {
        this.reminder_time=reminder_time;
        MainActivity m = new MainActivity();
        context=m.getCtx();
        //context=getBaseContext();
    }

    public void AddAlarms(){
        final Calendar calendar = Calendar.getInstance();
        AlarmManager mgrAlarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
        Intent intent = new Intent(context, OnAlarmReceiver.class);


        for(int i = 0; i < reminder_time.size(); ++i)
        {
            Log.d(TAG,"Setting alarms");
            Reminder_Time s = reminder_time.get(i);
            // Loop counter `i` is used as a `requestCode`
            calendar.set(Calendar.HOUR_OF_DAY, s.getS_hour());
            calendar.set(Calendar.MINUTE, s.getS_min());
            calendar.set(calendar.DAY_OF_MONTH,s.getS_date());
            calendar.set(calendar.MONTH,s.getS_month());
            calendar.set(calendar.YEAR,s.getS_year());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent, 0);
            // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
            mgrAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent);

            intentArray.add(pendingIntent);
        }
    }
}
