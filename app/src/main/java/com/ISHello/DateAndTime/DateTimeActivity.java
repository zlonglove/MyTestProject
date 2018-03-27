/**
 *
 */
package com.ISHello.DateAndTime;


import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.MainActivity;
import com.example.ishelloword.R;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author zhanglong
 */
public class DateTimeActivity extends BaseActivity {
    private final String TAG = "DateTimeActivity";
    private TextView dateTextView;
    private TextView timeTextView;
    private GregorianCalendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.i(TAG, "--->onCreate()");
        setContentView(R.layout.date_time_activity);
        dateTextView = (TextView) this.findViewById(R.id.date_title);
        timeTextView = (TextView) this.findViewById(R.id.time_title);
        calendar = new GregorianCalendar();

        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();
            }
        }, "2000-11-22 17:34", "2020-12-1 15:20");
        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）；
        //timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.setIsLoop(false);
        //timeSelector.disScrollUnit(TimeSelector.SCROLLTYPE.HOUR, TimeSelector.SCROLLTYPE.MINUTE);
        timeSelector.show();
    }

    public void getDate(View view) {
        Log.i(TAG, "--->Date Button Click");
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    dateTextView.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        }
        datePickerDialog.show();
    }

    public void getTime(View view) {
        Log.i(TAG, "--->Time Button Click");
        if (timePickerDialog == null) {
            timePickerDialog = new TimePickerDialog(this, new OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // TODO Auto-generated method stub
                    timeTextView.setText(hourOfDay + ":" + minute);
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        }
        timePickerDialog.show();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i(TAG, "--->onDistory()");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        Log.i(TAG, "--->onNewIntent()");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i(TAG, "--->onPause()");
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Log.i(TAG, "--->onRestart()");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.i(TAG, "--->onResume()");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.i(TAG, "--->onStart()");
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.i(TAG, "--->onStop()");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
