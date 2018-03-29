package com.multipz.dictionary.Util;

import java.io.InputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class Constant_method {
    public static boolean load=true;
    static SimpleDateFormat formatterTime = new SimpleDateFormat("KK:mm aa");
    static SimpleDateFormat formatterDate = new SimpleDateFormat("MMM dd,yyyy");

    public static String Get_id(Context c) {
        TelephonyManager telephonyManager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);

        String IMEI = telephonyManager.getDeviceId();
        //String id = Secure.getString(c.getContentResolver(),Secure.ANDROID_ID);
        //System.out.println(" Ud id " + id);
        return IMEI;
    }

    public static boolean checkConn(Context ctx) {
        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public String cu_time() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String getDate(String strDate) {
        Long datetimestamp = Long.parseLong(strDate.replaceAll("\\D", ""));
        Timestamp stamp = new Timestamp(datetimestamp);
        Date date = new Date(stamp.getTime());
        //Integer datetimestamp = Integer.parseInt(strDate.replaceAll("\\D", ""));
        //Date date = new Date(datetimestamp);
        return formatterDate.format(date);
    }

    public static String getDateDDMMYYYY(String strDate) {
        Long datetimestamp = Long.parseLong(strDate.replaceAll("\\D", ""));
        Timestamp stamp = new Timestamp(datetimestamp);
        Date date = new Date(stamp.getTime());
        return new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(date);
    }

    public static String getTime(String strDate) {
        Long datetimestamp = Long.parseLong(strDate.replaceAll("\\D", ""));
        Timestamp stamp = new Timestamp(datetimestamp);
        Date date = new Date(stamp.getTime());
        //Integer datetimestamp = Integer.parseInt(strDate.replaceAll("\\D", ""));
        //Date date = new Date(datetimestamp);
        formatterTime.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        return formatterTime.format(date);
    }

    public static String getDateFormate(String strDate, String strFormate) {
        Long datetimestamp = Long.parseLong(strDate.replaceAll("\\D", ""));
        Timestamp stamp = new Timestamp(datetimestamp);
        Date date = new Date(stamp.getTime());
        return new SimpleDateFormat(strFormate, Locale.getDefault()).format(date);
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                .matches();
    }

    public final static boolean isPasswordValid(String email) {

        boolean isValid = false;

        String expression = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
