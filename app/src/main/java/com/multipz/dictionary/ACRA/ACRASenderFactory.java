package com.multipz.dictionary.ACRA;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderFactory;

/**
 * Created by Admin on 25-07-2017.
 */

public class ACRASenderFactory implements ReportSenderFactory {

    public ACRASenderFactory(){
        Log.e("ACRA", "Create Sender Factory");
    }
    @NonNull
    @Override
    public ReportSender create(Context context, ACRAConfiguration acraConfiguration) {
        Log.e("ACRA", "Return Report Sender");
        return new ACRAReportSender();
    }
}
