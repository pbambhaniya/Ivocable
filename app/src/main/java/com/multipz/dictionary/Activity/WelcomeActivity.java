package com.multipz.dictionary.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.multipz.dictionary.Json.JSONParser;
import com.multipz.dictionary.R;
import com.multipz.dictionary.Util.Constant_method;
import com.multipz.dictionary.Util.Shared;
import com.multipz.dictionary.Json.config;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    boolean lastPageChange = false;
    private JSONParser jsonparser = new JSONParser();
    String user_login_id;
    String token;
    Shared shared;
    Context context;
    private View view;
    private static final int STORAGE_PERMISSION_CODE = 1;
    TelephonyManager mTelephony;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        shared = new Shared(this);

        if (shared.getBoolean("isLogin",false)){
            launchHomeScreen();
        }

       /* if (!shared.isFirstTimeLaunch1()) {
            launchHomeScreen();
            //finish();
        }*/

        token = shared.getString("notification", "");

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("iVocabe");

        setContentView(R.layout.activity_welcome);
        // getSupportActionBar().hide();

        if(Constant_method.checkConn(this)) {
            if (checkPermission())
            {
                new Configdata().execute();
            }
             else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},144);
            }

        } else
        {
            Toast.makeText(context, "Check Internet Connection ", Toast.LENGTH_SHORT).show();
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);

        layouts = new int[]{
                R.layout.welcome1,
                R.layout.welcome2,
                R.layout.welcome3 };

        // adding bottom dots
        addBottomDots(0);
        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 144:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        new Configdata().execute();

                    //permission granted successfully

                } else {
                    //permission denied
                }
                break;
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        shared.setFirstTimeLaunch1(false);
        startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
        finish();
    }

    //  viewpager change listener
    private ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            Log.e("call", "onpageselected");
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            Log.e("call", "onpagescroll");
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            Log.e("call", "onpagescrollstatechnage");
            int lastIdx = myViewPagerAdapter.getCount() - 1;

            int curItem = viewPager.getCurrentItem();
            if (curItem == lastIdx && arg0 == 1) {
                lastPageChange = true;

                // i put this here since onPageScroll gets called a couple of times.
                launchHomeScreen();
                lastPageChange = false;
            } else {
                lastPageChange = false;
            }

        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private class Configdata extends AsyncTask {

        private String url = config.MAIN_API;
        private int success;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(WelcomeActivity.this, "iVocabe", "Please wait...", true, false);
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            parm.add(new BasicNameValuePair("json", "{\"device_id\":\"" + Constant_method.Get_id(context) + "\",\"device_type\":\"A\",\"device_token\":\""+token+"\",\"social_id\":\"\",\"social_type\":\"\",\"action\":\"userLogin\"}\n"));

            JSONObject jsonObject = jsonparser.makeHttpRequest(url, "POST", parm);
            Log.e("Response:", "" + jsonObject + "");

            try {

                success = jsonObject.getInt("status");
                Log.e("Response:", "" + success);

                if (success == 1)
                {
                    JSONObject object = jsonObject.getJSONObject("data");
                    user_login_id = object.getString("user_login_id");
                    shared.putString("user id",user_login_id);

                    shared.putBoolean("isLogin", true);

//                    String email = object.getString("email");
//                    String password = object.getString("password");
//                    String device_id = object.getString("device_id");
//                    String device_type = object.getString("device_type");
//                    String created_date = object.getString("created_date");
//                    String modified_date = object.getString("modified_date");
//                    String is_status = object.getString("is_status");
//                    String is_delete = object.getString("is_delete");
                    /* ConfigModel model = new ConfigModel(user_login_id, email, password, device_id, device_type, created_date, modified_date, is_status, is_delete);
                    configlist.add(model); */
                }

                String userlogin = jsonObject.getString("action");

            }
              catch (Exception e1)
            {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                progressDialog.dismiss();
                if (success == 1) {

                }
            }catch (Exception e){}

        }
    }



}