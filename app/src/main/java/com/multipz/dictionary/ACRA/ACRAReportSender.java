package com.multipz.dictionary.ACRA;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

/**
 * Created by Admin on 25-07-2017.
 */

public class ACRAReportSender implements ReportSender {


    public ACRAReportSender(){
        Log.e("ACRA", "Report Sender created");
    }
    @Override
    public void send(Context context, CrashReportData crashReportData) throws ReportSenderException {
        Log.e("ACRA", "Trying to send crash report");
        String reportBody = createCrashReport(crashReportData);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        String to[] = {"multipz.ashish@gmail.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_TEXT, reportBody);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ACRA Crash Report");
        context.startActivity(Intent.createChooser(emailIntent, "Send crash to developpers by email ..."));

    }
    private String createCrashReport(CrashReportData crashReportData){
        StringBuilder body = new StringBuilder();
        body.append("Device : " + crashReportData.getProperty(ReportField.BRAND) + " - " + crashReportData.getProperty(ReportField.PHONE_MODEL))
                .append("\n")
                .append("Android Version : " + crashReportData.getProperty(ReportField.ANDROID_VERSION))
                .append("\n")
                .append("App Version : " + crashReportData.getProperty(ReportField.APP_VERSION_CODE))
                .append("\n")
                .append("STACK TRACE : \n" + crashReportData.getProperty(ReportField.STACK_TRACE));
        return body.toString();
    }
}
