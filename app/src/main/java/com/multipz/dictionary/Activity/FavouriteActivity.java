package com.multipz.dictionary.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.dictionary.Json.JSONParser;
import com.multipz.dictionary.Json.config;
import com.multipz.dictionary.Model.MathsCountModel;
import com.multipz.dictionary.R;
import com.multipz.dictionary.Util.Constant_method;
import com.multipz.dictionary.Util.Shared;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    LinearLayout maths_fav,chemistry_fav,physics_fav,biology_fav;
    ImageView nav_menu,maths_lock,chemistry_lock,physics_lock,biology_lock;
    Shared shared;
    TextView txt_twnmaths, txt_twnchemistry, txt_twnphysics, txt_twnbiology, txt_tfnmaths, txt_tfnchemistry, txt_tfnphysics, txt_tfnbiology;
    JSONArray jsonArray;
    String sub_name;
    String wordcount;
    String formulascount;
    public static int subject_id;
    LinearLayout lin_wmathscount,lin_fmathscount,lin_wchemistrycount,lin_fchemistrycount,lin_wphysicscount,lin_fphysicscount,lin_fbiologycount,lin_wbiologycount;
    ArrayList<MathsCountModel> mathswordcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        shared=new Shared(this);

        mathswordcount = new ArrayList<MathsCountModel>();

        if(Constant_method.checkConn(this)) {
            new MathsWordCount().execute();
        }else {
            Toast.makeText(FavouriteActivity.this, "Check Internet Connection ", Toast.LENGTH_SHORT).show();
        }

        maths_fav=(LinearLayout)findViewById(R.id.lin_maths);
        chemistry_fav=(LinearLayout)findViewById(R.id.lin_chemistry);
        physics_fav=(LinearLayout)findViewById(R.id.lin_physics);
        biology_fav=(LinearLayout)findViewById(R.id.lin_biology);
        nav_menu = (ImageView) findViewById(R.id.nav_menu);
        txt_twnmaths = (TextView) findViewById(R.id.txt_twnmaths);
        txt_tfnmaths = (TextView) findViewById(R.id.txt_tfnmaths);
        txt_twnchemistry = (TextView) findViewById(R.id.txt_twnchemistry);
        txt_twnphysics = (TextView) findViewById(R.id.txt_twnphysics);
        txt_twnbiology = (TextView) findViewById(R.id.txt_twnbiology);
        txt_tfnchemistry = (TextView) findViewById(R.id.txt_tfnchemistry);
        txt_tfnphysics = (TextView) findViewById(R.id.txt_tfnphysics);
        txt_tfnbiology = (TextView) findViewById(R.id.txt_tfnbiology);
        maths_lock=(ImageView)findViewById(R.id.maths_lock);
        physics_lock=(ImageView)findViewById(R.id.physics_lock);
        chemistry_lock=(ImageView)findViewById(R.id.chemistry_lock);
        biology_lock=(ImageView)findViewById(R.id.biology_lock);

        lin_wmathscount=(LinearLayout)findViewById(R.id.lin_wmathscount);
        lin_fmathscount=(LinearLayout)findViewById(R.id.lin_fmathscount);
        lin_wchemistrycount=(LinearLayout)findViewById(R.id.lin_wchemistrycount);
        lin_fchemistrycount=(LinearLayout)findViewById(R.id.lin_fchemistrycount);
        lin_wphysicscount=(LinearLayout)findViewById(R.id.lin_wphysicscount);
        lin_fphysicscount=(LinearLayout)findViewById(R.id.lin_fphysicscount);
        lin_wbiologycount=(LinearLayout)findViewById(R.id.lin_wbiologycount);
        lin_fbiologycount=(LinearLayout)findViewById(R.id.lin_fbiologycount);

        maths_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialogue();
            }
        });

        chemistry_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialogue();
            }
        });

        physics_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showdialogue();
            }
        });

        biology_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showdialogue();
            }
        });

        nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

    }

    private class MathsWordCount extends AsyncTask<String, String, String> {
        private String url = config.MAIN_API;
        private int success=0;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FavouriteActivity.this, "iVocabe", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            parm.add(new BasicNameValuePair("json", "{\"user_id\":\""+shared.getString("user id","")+"\",\"action\":\"favCount\"}"));

            JSONObject jsonObject = new JSONParser().makeHttpRequest(url, "POST", parm);
            Log.e("url", url);
            Log.e("Response:", "" + jsonObject + "");
            try {
                success = jsonObject.getInt("status");

                Log.e("Response:", "" + success);
                if (success == 1) {
                    jsonArray = jsonObject.getJSONArray("data");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (success == 1) {

                for (int i1 = 0; i1 < jsonArray.length(); i1++) {

                    JSONObject jsonObject1 = null;
                    try {
                        jsonObject1 = jsonArray.getJSONObject(i1);
                        mathswordcount.add(new MathsCountModel(jsonObject1.getString("subject_id"),
                                jsonObject1.getString("subject_name"),
                                jsonObject1.getString("word_count"),
                                jsonObject1.getString("formula_count")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setdataofView();
            }
//                txt_maths.setText(sub_name);
        }
    }

    private void setdataofView() {
        for (int i=0; i<mathswordcount.size(); i++){
            if (mathswordcount.get(i).getSubject_id().contentEquals("1")){
                txt_twnmaths.setText(mathswordcount.get(i).getWord_count());
                txt_tfnmaths.setText(mathswordcount.get(i).getFormula_count());
                maths_fav.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                maths_lock.setVisibility(View.GONE);
                lin_wmathscount.setVisibility(View.VISIBLE);
                lin_fmathscount.setVisibility(View.VISIBLE);

                maths_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subject_id=1;
                        Intent maths=new Intent(FavouriteActivity.this,ListFavouriteActivity.class);
                        startActivity(maths);
                    }
                });
            }

            else if (mathswordcount.get(i).getSubject_id().contentEquals("3")){
                txt_twnchemistry.setText(mathswordcount.get(i).getWord_count());
                txt_tfnchemistry.setText(mathswordcount.get(i).getFormula_count());
                chemistry_fav.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                chemistry_lock.setVisibility(View.GONE);
                lin_wchemistrycount.setVisibility(View.VISIBLE);
                lin_fchemistrycount.setVisibility(View.VISIBLE);
                chemistry_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subject_id=3;
                        Intent maths=new Intent(FavouriteActivity.this,ListFavouriteActivity.class);
                        startActivity(maths);
                    }
                });
            }

            else if (mathswordcount.get(i).getSubject_id().contentEquals("2")){
                txt_twnphysics.setText(mathswordcount.get(i).getWord_count());
                txt_tfnphysics.setText(mathswordcount.get(i).getFormula_count());
                physics_fav.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                physics_lock.setVisibility(View.GONE);
                lin_wphysicscount.setVisibility(View.VISIBLE);
                lin_fphysicscount.setVisibility(View.VISIBLE);
                physics_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subject_id=2;
                        Intent maths=new Intent(FavouriteActivity.this,ListFavouriteActivity.class);
                        startActivity(maths);
                    }
                });
            }

            else if (mathswordcount.get(i).getSubject_id().contentEquals("4")){
                txt_twnbiology.setText(mathswordcount.get(i).getWord_count());
                txt_tfnbiology.setText(mathswordcount.get(i).getFormula_count());
                biology_fav.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                biology_lock.setVisibility(View.GONE);
                lin_wbiologycount.setVisibility(View.VISIBLE);
                lin_wbiologycount.setVisibility(View.VISIBLE);
                biology_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subject_id=4;
                        Intent maths=new Intent(FavouriteActivity.this,ListFavouriteActivity.class);
                        startActivity(maths);
                    }
                });
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
       finish();
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

    @Override
    protected void onResume() {
        if(Constant_method.checkConn(this)) {
            new MathsWordCount().execute();
        }else {
            Toast.makeText(FavouriteActivity.this, "Check Internet Connection ", Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }
}
