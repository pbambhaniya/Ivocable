package com.multipz.dictionary.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.multipz.dictionary.Adapter.HorizontalListAdapter;
import com.multipz.dictionary.Json.JSONParser;
import com.multipz.dictionary.Json.config;
import com.multipz.dictionary.Model.MathsCountModel;
import com.multipz.dictionary.Model.MathsWordModel;
import com.multipz.dictionary.R;
import com.multipz.dictionary.Util.Constant_method;
import com.multipz.dictionary.Util.Shared;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView nav_img_maths, nav_img_chemisrty, nav_img_physics, nav_img_biology, nav_img_rate, nav_img_favorite, nav_img_share, nav_img_close;
    TextView nav_txt_maths, nav_txt_chemisrty, nav_txt_physics, nav_txt_biology, nav_txt_rate, nav_txt_favorite, nav_txt_share;
    ImageView img_maths, img_chemistry, img_physics, img_biology;
    TextView txt_maths, txt_chemistry, txt_physics, txt_biology;
    TextView txt_twmaths, txt_twchemistry, txt_twphysics, txt_twbiology, txt_tfmaths, txt_tfchemistry, txt_tfphysics, txt_tfbiology;
    TextView txt_twnmaths, txt_twnchemistry, txt_twnphysics, txt_twnbiology, txt_tfnmaths, txt_tfnchemistry, txt_tfnphysics, txt_tfnbiology;
    RelativeLayout ll_maths, ll_chemistry, ll_physics, ll_biology,ll_nav_img_maths,ll_nav_img_chemisrty,ll_nav_img_physics,ll_nav_img_biology;
    LinearLayout ll_nav_imgactivity, ll_nav_img_share,ll_nav_img_favorite, ll_nav_about;
    JSONArray jsonArray;

    ImageView nav_menu,maths_lock,chemistry_lock,physics_lock,biology_lock,chemistry_drawer_lock,physics_drawer_lock,biology_drawer_lock,maths_drawer_lock;
    Context context;
    CoordinatorLayout coordinatorLayout;
    LinearLayout lin_maths,lin_chemistry,lin_physics,lin_biology;

    private JSONArray MathsWordList = new JSONArray();
    String wordcount;
    String formulascount,is_status;
    public static int subject_id;
    public static String sub_name;
    ArrayList<MathsCountModel> mathswordcount;
    Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context=this;
        shared=new Shared(this);

        mathswordcount = new ArrayList<MathsCountModel>();
        if(Constant_method.checkConn(this)) {
            try {
                new MathsWord().execute();
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            Toast.makeText(context, "Check Internet Connection ", Toast.LENGTH_SHORT).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        lin_physics=(LinearLayout)findViewById(R.id.lin_physics);
        lin_maths=(LinearLayout)findViewById(R.id.lin_maths);
        lin_biology=(LinearLayout)findViewById(R.id.lin_biology);
        lin_chemistry=(LinearLayout)findViewById(R.id.lin_chemistry);

        maths_lock=(ImageView)findViewById(R.id.maths_lock);
        physics_lock=(ImageView)findViewById(R.id.physics_lock);
        chemistry_lock=(ImageView)findViewById(R.id.chemistry_lock);
        biology_lock=(ImageView)findViewById(R.id.biology_lock);

        chemistry_drawer_lock=(ImageView)findViewById(R.id.chemistry_drawer_lock);
        physics_drawer_lock=(ImageView)findViewById(R.id.physics_drawer_lock);
        biology_drawer_lock=(ImageView)findViewById(R.id.biology_drawer_lock);
        maths_drawer_lock=(ImageView)findViewById(R.id.maths_drawer_lock);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        init();

        nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        nav_img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        ll_maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(Gravity.LEFT);

                if (mathswordcount.get(0).getIs_status().contentEquals("Y")) {
                    subject_id = Integer.parseInt(mathswordcount.get(0).getSubject_id());
                    sub_name = "Maths";
                    if (null != HorizontalListAdapter.selected_word) {
                        HorizontalListAdapter.selected_word = "A";
                    }
                    Intent mathsintent = new Intent(HomeActivity.this, ListWordActivity.class);
                    mathsintent.putExtra("subject_name", sub_name);
                    startActivity(mathsintent);
                }
                else {
                    showdialogue();
                }
            }
        });

        ll_chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(Gravity.LEFT);
                if (mathswordcount.get(2).getIs_status().contentEquals("Y")) {
                    subject_id = Integer.parseInt(mathswordcount.get(2).getSubject_id());
                    sub_name = "Chemistry";
                    if (null != HorizontalListAdapter.selected_word) {
                        HorizontalListAdapter.selected_word = "A";
                    }
                    Intent chemistryintent = new Intent(HomeActivity.this, ListWordActivity.class);
                    chemistryintent.putExtra("subject_name", sub_name);
                    startActivity(chemistryintent);
                }
                else {
                    showdialogue();
                }

            }
        });

        ll_physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(Gravity.LEFT);
                if (mathswordcount.get(1).getIs_status().contentEquals("Y")) {
                    subject_id = Integer.parseInt(mathswordcount.get(1).getSubject_id());
                    sub_name="Physics";
                    if (null != HorizontalListAdapter.selected_word) {
                        HorizontalListAdapter.selected_word = "A";
                    }
                    Intent physicsintent = new Intent(HomeActivity.this, ListWordActivity.class);
                    physicsintent.putExtra("subject_name", sub_name);
                    startActivity(physicsintent);
                }else{
                    showdialogue();
                }
            }
        });

        ll_biology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(Gravity.LEFT);
                if (mathswordcount.get(3).getIs_status().contentEquals("Y")) {
                    subject_id = Integer.parseInt(mathswordcount.get(3).getSubject_id());
                    sub_name="Biology";
                    if (null != HorizontalListAdapter.selected_word) {
                        HorizontalListAdapter.selected_word = "A";
                    }
                    Intent physicsintent = new Intent(HomeActivity.this, ListWordActivity.class);
                    physicsintent.putExtra("subject_name", sub_name);
                    startActivity(physicsintent);
                }
                else {
                    showdialogue();
                }
            }
        });

        ll_nav_img_maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_maths.performClick();
            }
        });

        ll_nav_img_chemisrty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ll_chemistry.performClick();
            }
        });

        ll_nav_img_physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_physics.performClick();
            }
        });

        ll_nav_img_biology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_biology.performClick();
            }
        });

        ll_nav_img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                String appPackageName = HomeActivity.this.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.TEXT", "Check out App at: https://play.google.com/store/apps/details?id=" + appPackageName);
                sendIntent.setType("text/plain");
                HomeActivity.this.startActivity(sendIntent);
            }
        });

        ll_nav_img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent fav=new Intent(HomeActivity.this,FavouriteActivity.class);
                startActivity(fav);
            }
        });

        ll_nav_imgactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent myactivity=new Intent(HomeActivity.this,MyActivity.class);
                startActivity(myactivity);
            }
        });

        ll_nav_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                drawer.closeDrawer(Gravity.LEFT);
                Intent About=new Intent(HomeActivity.this,AboutActivity.class);
                startActivity(About);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init() {
        nav_menu = (ImageView) findViewById(R.id.nav_menu);
        ll_maths = (RelativeLayout) findViewById(R.id.ll_maths);
        ll_chemistry = (RelativeLayout) findViewById(R.id.ll_chemistry);
        ll_physics = (RelativeLayout) findViewById(R.id.ll_physics);
        ll_biology = (RelativeLayout) findViewById(R.id.ll_biology);

        ll_nav_img_maths = (RelativeLayout) findViewById(R.id.ll_nav_imgmaths);
        ll_nav_img_chemisrty = (RelativeLayout) findViewById(R.id.ll_nav_imgchemistry);
        ll_nav_img_physics = (RelativeLayout) findViewById(R.id.ll_nav_imgphysics);
        ll_nav_img_biology = (RelativeLayout) findViewById(R.id.ll_nav_imgbiology);
        ll_nav_imgactivity = (LinearLayout) findViewById(R.id.ll_nav_imgactivity);
        ll_nav_img_share = (LinearLayout) findViewById(R.id.ll_nav_imgshare);
        ll_nav_img_favorite = (LinearLayout) findViewById(R.id.ll_nav_imgfavorite);
        ll_nav_about = (LinearLayout) findViewById(R.id.ll_nav_about);

        nav_img_maths = (ImageView) findViewById(R.id.nav_imgmaths);
        nav_img_chemisrty = (ImageView) findViewById(R.id.nav_imgchemistry);
        nav_img_physics = (ImageView) findViewById(R.id.nav_imgphysics);
        nav_img_biology = (ImageView) findViewById(R.id.nav_imgbiology);
//        nav_img_rate = (ImageView) findViewById(R.id.nav_imgrate);
        nav_img_share = (ImageView) findViewById(R.id.nav_imgshare);
        nav_img_favorite = (ImageView) findViewById(R.id.nav_imgfavorite);
        nav_img_close = (ImageView) findViewById(R.id.nav_close);

        img_maths = (ImageView) findViewById(R.id.img_maths);
        img_chemistry = (ImageView) findViewById(R.id.img_chemistry);
        img_physics = (ImageView) findViewById(R.id.img_physics);
        img_biology = (ImageView) findViewById(R.id.img_biology);

        nav_txt_maths = (TextView) findViewById(R.id.nav_txtmaths);
        nav_txt_chemisrty = (TextView) findViewById(R.id.nav_txtchemistry);
        nav_txt_physics = (TextView) findViewById(R.id.nav_txtphysics);
        nav_txt_biology = (TextView) findViewById(R.id.nav_txtbiology);
//        nav_txt_rate = (TextView) findViewById(R.id.nav_txtrate);
        nav_txt_share = (TextView) findViewById(R.id.nav_txtshare);
        nav_txt_favorite = (TextView) findViewById(R.id.nav_txtfavourite);

        txt_maths = (TextView) findViewById(R.id.txt_maths);
        txt_chemistry = (TextView) findViewById(R.id.txt_chemistry);
        txt_physics = (TextView) findViewById(R.id.txt_physics);
        txt_biology = (TextView) findViewById(R.id.txt_biology);

        txt_twmaths = (TextView) findViewById(R.id.txt_twmaths);
        txt_twchemistry = (TextView) findViewById(R.id.txt_twchemistry);
        txt_twphysics = (TextView) findViewById(R.id.txt_twphysics);
        txt_twbiology = (TextView) findViewById(R.id.txt_twbiology);
        txt_twnmaths = (TextView) findViewById(R.id.txt_twnmaths);
        txt_twnchemistry = (TextView) findViewById(R.id.txt_twnchemistry);
        txt_twnphysics = (TextView) findViewById(R.id.txt_twnphysics);
        txt_twnbiology = (TextView) findViewById(R.id.txt_twnbiology);

        txt_tfmaths = (TextView) findViewById(R.id.txt_tfmaths);
        txt_tfchemistry = (TextView) findViewById(R.id.txt_tfchemistry);
        txt_tfphysics = (TextView) findViewById(R.id.txt_tfphysics);
        txt_tfbiology = (TextView) findViewById(R.id.txt_tfbiology);
        txt_tfnmaths = (TextView) findViewById(R.id.txt_tfnmaths);
        txt_tfnchemistry = (TextView) findViewById(R.id.txt_tfnchemistry);
        txt_tfnphysics = (TextView) findViewById(R.id.txt_tfnphysics);
        txt_tfnbiology = (TextView) findViewById(R.id.txt_tfnbiology);


        Typeface light = Typeface.createFromAsset(getAssets(), "Lato-Light.ttf");
//        txt_twmaths.setTypeface(light);
        txt_twchemistry.setTypeface(light);
        txt_twphysics.setTypeface(light);
        txt_twbiology.setTypeface(light);
        txt_twnmaths.setTypeface(light);
        txt_twnchemistry.setTypeface(light);
        txt_twnphysics.setTypeface(light);
        txt_twnbiology.setTypeface(light);

//        txt_tfmaths.setTypeface(light);
        txt_tfchemistry.setTypeface(light);
        txt_tfphysics.setTypeface(light);
        txt_tfbiology.setTypeface(light);
        txt_tfnmaths.setTypeface(light);
        txt_tfnchemistry.setTypeface(light);
        txt_tfnphysics.setTypeface(light);
        txt_tfnbiology.setTypeface(light);


        Typeface regular = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
        txt_maths.setTypeface(regular);
        txt_chemistry.setTypeface(regular);
        txt_physics.setTypeface(regular);
        txt_biology.setTypeface(regular);

    }

    public void showdialogue(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.comming_soon);

        LinearLayout okay=(LinearLayout)dialog.findViewById(R.id.okay);
        TextView header=(TextView)dialog.findViewById(R.id.head_dialogue);
        TextView desc=(TextView)dialog.findViewById(R.id.head_desc);
        TextView okay_txt=(TextView)dialog.findViewById(R.id.txt_okay);

        Typeface regular = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
        header.setTypeface(regular);
        desc.setTypeface(regular);
        okay_txt.setTypeface(regular);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private class MathsWord extends AsyncTask<String, String, String> {

        private String url = config.MAIN_API;
        private int success = 0;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(HomeActivity.this, "iVocabe", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            parm.add(new BasicNameValuePair("json", "{\"action\":\"getCountData\"}"));

            JSONObject jsonObject = new JSONParser().makeHttpRequest(url, "POST", parm);
            Log.e("url", url);
            Log.e("Response:", "" + jsonObject + "");
            try {
                success = jsonObject.getInt("status");

                Log.e("Response:", "" + success);
                if (success == 1) {
                    jsonArray = jsonObject.getJSONArray("data");

                    for (int i1 = 0; i1 < jsonArray.length(); i1++) {

                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = jsonArray.getJSONObject(i1);

                            mathswordcount.add(new MathsCountModel(jsonObject1.getString("subject_id"),
                                    jsonObject1.getString("subject_name"),
                                    jsonObject1.getString("is_status"),
                                    jsonObject1.getString("word_count"),
                                    jsonObject1.getString("formula_count")));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    shared.putString("wordcount",jsonArray.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (success == 1) {

                for (int i1 = 0; i1 < mathswordcount.size(); i1++) {
                    viewStatusControl(i1);
                }
            } else {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
//                txt_maths.setText(sub_name);
            progressDialog.dismiss();
        }
    }


    void viewStatusControl(int position){

        if (position == 0){
            if (mathswordcount.get(position).getIs_status().contentEquals("Y")) {
                lin_maths.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                maths_lock.setVisibility(View.GONE);
                maths_drawer_lock.setVisibility(View.GONE);
                txt_twnmaths.setText(mathswordcount.get(position).getWord_count());
                txt_tfnmaths.setText(mathswordcount.get(position).getFormula_count());

                txt_twmaths.setVisibility(View.VISIBLE);
                txt_twnmaths.setVisibility(View.VISIBLE);
                txt_tfmaths.setVisibility(View.VISIBLE);
                txt_tfnmaths.setVisibility(View.VISIBLE);

            } else {
                lin_maths.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                maths_lock.setVisibility(View.VISIBLE);
                maths_drawer_lock.setVisibility(View.VISIBLE);

                txt_twmaths.setVisibility(View.GONE);
                txt_twnmaths.setVisibility(View.GONE);
                txt_tfmaths.setVisibility(View.GONE);
                txt_tfnmaths.setVisibility(View.GONE);
            }
        }
        else if (position == 1){
            if (mathswordcount.get(position).getIs_status().contentEquals("Y")) {
                lin_physics.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                physics_lock.setVisibility(View.GONE);
                physics_drawer_lock.setVisibility(View.GONE);
                txt_twnphysics.setText(mathswordcount.get(position).getWord_count());
                txt_tfnphysics.setText(mathswordcount.get(position).getFormula_count());

                txt_twphysics.setVisibility(View.VISIBLE);
                txt_twnphysics.setVisibility(View.VISIBLE);
                txt_tfphysics.setVisibility(View.VISIBLE);
                txt_tfnphysics.setVisibility(View.VISIBLE);
            } else {
                lin_physics.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                physics_lock.setVisibility(View.VISIBLE);
                physics_drawer_lock.setVisibility(View.VISIBLE);

                txt_twphysics.setVisibility(View.GONE);
                txt_twnphysics.setVisibility(View.GONE);
                txt_tfphysics.setVisibility(View.GONE);
                txt_tfnphysics.setVisibility(View.GONE);
            }
        }
        else if (position == 2){
            if (mathswordcount.get(position).getIs_status().contentEquals("Y")) {
                lin_chemistry.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                chemistry_lock.setVisibility(View.GONE);
                chemistry_drawer_lock.setVisibility(View.GONE);
                txt_twnchemistry.setText(mathswordcount.get(position).getWord_count());
                txt_tfnchemistry.setText(mathswordcount.get(position).getFormula_count());

                txt_twchemistry.setVisibility(View.VISIBLE);
                txt_twnchemistry.setVisibility(View.VISIBLE);
                txt_tfchemistry.setVisibility(View.VISIBLE);
                txt_tfnchemistry.setVisibility(View.VISIBLE);
            } else {
                lin_chemistry.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                chemistry_lock.setVisibility(View.VISIBLE);
                chemistry_drawer_lock.setVisibility(View.VISIBLE);

                txt_twchemistry.setVisibility(View.GONE);
                txt_twnchemistry.setVisibility(View.GONE);
                txt_tfchemistry.setVisibility(View.GONE);
                txt_tfnchemistry.setVisibility(View.GONE);
            }
        }
        else if (position == 3){
            if (mathswordcount.get(position).getIs_status().contentEquals("Y")) {
                lin_biology.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                biology_lock.setVisibility(View.GONE);
                biology_drawer_lock.setVisibility(View.GONE);
                txt_twnbiology.setText(mathswordcount.get(position).getWord_count());
                txt_tfnbiology.setText(mathswordcount.get(position).getFormula_count());

                txt_twbiology.setVisibility(View.VISIBLE);
                txt_twnbiology.setVisibility(View.VISIBLE);
                txt_tfbiology.setVisibility(View.VISIBLE);
                txt_tfnbiology.setVisibility(View.VISIBLE);
            } else {
                lin_biology.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                biology_lock.setVisibility(View.VISIBLE);
                biology_drawer_lock.setVisibility(View.VISIBLE);

                txt_twbiology.setVisibility(View.GONE);
                txt_twnbiology.setVisibility(View.GONE);
                txt_tfbiology.setVisibility(View.GONE);
                txt_tfnbiology.setVisibility(View.GONE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitDialogue();
        }

    }

    public void exitDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ivocabelogo);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
       app_launched(this);
    }

//    public void showRateDialog() {
//        final Dialog dialog=new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setContentView(R.layout.dialogue);
//
//        LinearLayout nothanks=(LinearLayout)dialog.findViewById(R.id.nothanks);
//        LinearLayout later=(LinearLayout)dialog.findViewById(R.id.later);
//        LinearLayout rate=(LinearLayout)dialog.findViewById(R.id.ratenow);
//
//        nothanks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        later.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                   dialog.hide();
//                dialog.dismiss();
//            }
//        });
//
//        rate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
//                Intent rate = new Intent(Intent.ACTION_VIEW, uri);
//
//                rate.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                try {
//                    startActivity(rate);
//                } catch (ActivityNotFoundException e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
//                }
//            }
//        });
//
////        Typeface regular = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
////        nothanks.setTypeface(regular);
////        later.setTypeface(regular);
////        rate.setTypeface(regular);
//
//        dialog.show();
//    }

    private final static int DAYS_UNTIL_PROMPT = 2;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 5;//Min number of launches

    public void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                if (Constant_method.load)
                {
                    Constant_method.load=false;
                    showRateDialog(mContext, editor);
                }
            }
        }

        editor.commit();
    }

    public void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setContentView(R.layout.dialogue);
        LinearLayout nothanks=(LinearLayout)dialog.findViewById(R.id.nothanks);
        LinearLayout later=(LinearLayout)dialog.findViewById(R.id.later);
        LinearLayout rate=(LinearLayout)dialog.findViewById(R.id.ratenow);

        nothanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });

        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                   dialog.hide();
                dialog.dismiss();
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent rate = new Intent(Intent.ACTION_VIEW, uri);

                rate.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(rate);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }
            }
        });

        dialog.show();
    }

}
