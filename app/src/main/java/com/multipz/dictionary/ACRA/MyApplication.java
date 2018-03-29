package com.multipz.dictionary.ACRA;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.multipz.dictionary.R;

import org.acra.*;
import org.acra.annotation.*;
import org.acra.config.ACRAConfiguration;
import org.acra.config.ConfigurationBuilder;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Admin on 25-07-2017.
 */
@ReportsCrashes(mailTo = "multipz.ashish@gmail.com", mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)
//@ReportsCrashes(reportSenderFactoryClasses = {ACRASenderFactory.class})
public class MyApplication extends Application {

        @Override
        public void onCreate() {

//            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                    .setDefaultFontPath("fonts/Lato-Medium.ttf")
//                    .setFontAttrId(R.attr.fontPath)
//                    .build()
//            );
            super.onCreate();
            //ACRA.init(this);
            FacebookSdk.sdkInitialize(getApplicationContext());
        }

}
