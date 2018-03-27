package com.ISHello.Alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class ISAlarm {
    private static ISAlarm alarm;

    private ISAlarm() {

    }

    public static ISAlarm getInstance() {
        if (alarm == null) {
            alarm = new ISAlarm();
        }
        return alarm;
    }

    public void setAlarmReceiver(int hour, int minute) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(System.currentTimeMillis());
        localCalendar.set(Calendar.HOUR_OF_DAY, hour);// 时
        localCalendar.set(Calendar.MINUTE, minute);// 分
        localCalendar.set(Calendar.SECOND, 0);// 秒
        localCalendar.set(Calendar.MILLISECOND, 0);// 毫秒
        long shutDownTime = localCalendar.getTimeInMillis();

        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy");
        Date CurrentTime = new Date(System.currentTimeMillis());// 当前

        Date localDate2 = new Date(shutDownTime);// 设置
        int i = Integer.parseInt(localSimpleDateFormat.format(CurrentTime));// currentTime
        int j = Integer.parseInt(localSimpleDateFormat.format(localDate2));// shutDownTime
        Log.i("TimerOperationDevice", "-->setAlarmReceiver(),DATA[currentDateYear:" + i + ",shutDowndateYear:" + j + "]");

        if (j < i) {
            int k = i - j;
            Log.i("TimerOperationDevice", "--->setAlarmReceiver(),auto add time by >>[" + k + "]<<day!");
            localCalendar.add(Calendar.FEBRUARY, k);
            shutDownTime = localCalendar.getTimeInMillis();
        }
        if (shutDownTime < System.currentTimeMillis()) {
            Log.i("TimerOperationDevice",
                    "--->setAlarmReceiver(), auto add time by one day!startTime = "
                            + shutDownTime + ", system.time = "
                            + System.currentTimeMillis());
            localCalendar.add(Calendar.DAY_OF_YEAR, 1);//next day
            shutDownTime = localCalendar.getTimeInMillis();
        }
        Log.i("TimerOperationDevice", "--->setAlarmReceiver(), alram time is:"
                + shutDownTime);
    }
}
