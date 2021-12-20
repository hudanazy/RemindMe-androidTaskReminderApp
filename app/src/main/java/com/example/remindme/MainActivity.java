package com.example.remindme;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import com.allyants.notifyme.NotifyMe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {

    public EditText txt_title;
    public EditText txt_description;
    public static EditText txt_time, txt_date;

    Button btn_schedule;
    Switch swtch_imp;

    public static boolean is_imp;

    //OnCreate methond of the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_title = findViewById(R.id.txt_title);
        txt_description = findViewById(R.id.txt_description);
        txt_time = findViewById(R.id.txt_time);
        txt_date = findViewById(R.id.txt_date);
        btn_schedule = findViewById(R.id.btn_schedule);
        swtch_imp = (Switch) findViewById(R.id.swtch_imp);

        swtch_imp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Assign switch checked status to variable
                is_imp = b;
                if (b)
                    Toast.makeText(MainActivity.this, "important", Toast.LENGTH_SHORT).show();
            }
        });

        txt_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Open time picker on click
                showTruitonTimePickerDialog(txt_time);
            }
        });

        txt_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Open date picker on click
                showTruitonDatePickerDialog(txt_date);
            }
        });
        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Schedule button click
                String title = txt_title.getText().toString();
                String description = txt_description.getText().toString();
                String date1 = txt_date.getText().toString();
                String time = txt_time.getText().toString();

                Calendar calendar = Calendar.getInstance();
                calendar.set(datedialog.getDatePicker().getYear(), datedialog.getDatePicker().getMonth() + 1, datedialog.getDatePicker().getDayOfMonth(),
                        s_hour, s_min, 0);
                long futureTime = calendar.getTimeInMillis();

                Calendar c = Calendar.getInstance();


                long sub = c2.getTimeInMillis() - c.getTimeInMillis();

                //Validation start
                if (title.length() <= 0) {
                    Toast.makeText(MainActivity.this, "Please enter Title", Toast.LENGTH_SHORT).show();
                } else if (description.length() <= 0) {
                    Toast.makeText(MainActivity.this, "Please enter Description", Toast.LENGTH_SHORT).show();
                } else if (date1.length() <= 0) {
                    Toast.makeText(MainActivity.this, "Please enter Date", Toast.LENGTH_SHORT).show();
                } else if (time.length() <= 0) {
                    Toast.makeText(MainActivity.this, "Please enter Time", Toast.LENGTH_SHORT).show();
                } else {
                    if (sub <= 0) {
                        Toast.makeText(MainActivity.this, "You can not select past time", Toast.LENGTH_SHORT).show();
                    } else {
                        txt_date.setText("");
                        txt_title.setText("");
                        txt_time.setText("");
                        txt_description.setText("");
                        //If there is not any error with form, schedule notification
                        boolean is_imp = swtch_imp.isChecked();
                        scheduleNotification(title, description, MainActivity.this, sub, 1, futureTime, is_imp);
                        Toast.makeText(MainActivity.this, "Notification has been scheduled", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

    }
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("title", txt_title.getText().toString());
        editor.putString("description", txt_description.getText().toString());
        editor.putString("date1", txt_date.getText().toString());
        editor.putString("time", txt_time.getText().toString());
        editor.putBoolean("switch",swtch_imp.isChecked());
        editor.commit();
    }

    public static DatePickerDialog datedialog;

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            datedialog = new DatePickerDialog(getContext(), this, year, month, day);
            datedialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return datedialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            txt_date.setText(day + "/" + (month + 1) + "/" + year);

        }
    }

    public static int s_hour, s_min;
    public static TimePickerDialog timePickerDialog;

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {
        Calendar c;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            timePickerDialog = new TimePickerDialog(getActivity(), this,
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
            return timePickerDialog;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            txt_time.setText(hourOfDay + ":" + minute);
            s_hour = hourOfDay;
            s_min = minute;
            c2 = Calendar.getInstance();
            c2.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c2.set(Calendar.MINUTE, minute);
            c2.set(Calendar.SECOND, 0);

        }
    }

    public static Calendar c2;


    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }


    public static NotificationCompat.Builder builder;

    public void scheduleNotification(String title, String content, Context context, long delay, int notificationId, long futuremilli, boolean is_imp) {//delay is after how much time(in millis) from current time you want to schedule the notification
        builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        if (is_imp) {
            Log.e("Main", "in if");
            builder.setPriority(Notification.PRIORITY_MAX);

        } else {
            Log.e("Main", "in else");
            builder.setPriority(Notification.PRIORITY_LOW);
        }

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
        notificationIntent.putExtra("is_imp", is_imp);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences= getDefaultSharedPreferences(getApplicationContext());
        txt_title.setText(sharedPreferences.getString("title",""));
        txt_description.setText(sharedPreferences.getString("description",""));
        txt_date.setText(sharedPreferences.getString("date1",""));
        txt_time.setText(sharedPreferences.getString("time",""));
        swtch_imp.setChecked(sharedPreferences.getBoolean("switch",false));
    }

    public static class MyNotificationPublisher extends BroadcastReceiver {

        public static String NOTIFICATION_ID = "notification_id";
        public static String NOTIFICATION = "notification";

        //onReceive method called on receiving the notification
        @Override
        public void onReceive(final Context context, Intent intent) {


            //Send notificiation
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //Notification notification = intent.getParcelableExtra(NOTIFICATION);
            //int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "CH1";
                NotificationChannel channel;
                boolean is_imp_inner = intent.getBooleanExtra("is_imp", false);
                Log.e("Main", "is_imp_inner " + is_imp_inner);
                if (is_imp)
                channel = new NotificationChannel(
                        channelId,
                        "Important",
                        NotificationManager.IMPORTANCE_HIGH);
                else
                    channel = new NotificationChannel(
                            channelId,
                            "low",
                            NotificationManager.IMPORTANCE_LOW);

                notificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            }
            notificationManager.notify(0, builder.build());
        }
    }
}