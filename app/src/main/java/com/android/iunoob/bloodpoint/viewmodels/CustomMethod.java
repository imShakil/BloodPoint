package com.android.iunoob.bloodpoint.viewmodels;

import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomMethod {

    private static UserData getData;

    public CustomMethod() {
    }

    public static String GetDateFormat(long Year)
    {
        long Day = Year % 100;
        Year /= 100;
        long Month = Year % 100;
        Year /= 100;
        return (Day +"/"+Month+"/"+Year);
    }

    public static long GetDateInInt(long Year, long Month, long Day)
    {
        Year *= 100;
        Year += Month;
        Year *= 100;
        Year += Day;

        return Year;
    }


    public static String GetDateFromMilis(long TimeInMilis){

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(TimeInMilis);

        return c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);

    }

    public static String GetDateAndTime(long TimeInMillis, String DateFormate)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(TimeInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormate);
        return dateFormat.format(c.getTime());
    }

    public static boolean CheckPost(long T2)
    {
        long T1 = Calendar.getInstance().getTimeInMillis();
        T1 /= 1000; //sec
        T1 /= 3600; //hr
        T1 /= 24; //days

        T2 /= 1000;
        T2 /= 3600;
        T2 /= 24;

        return T1-T2<=7;
    }


    public static String GetS(long x)
    {
        return x>1?"s":"";
    }
}
