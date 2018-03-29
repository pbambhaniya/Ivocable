package com.multipz.dictionary.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.multipz.dictionary.Util.Shared;

/**
 * Created by Admin on 28-06-2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String REG_TOKEN = "REG_TOKEN";

    Shared shared;

    @Override
    public void onTokenRefresh() {

        shared = new Shared(this);

        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN, recent_token);
        shared.putString("notification", recent_token);

    }
}
