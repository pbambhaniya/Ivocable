package com.multipz.dictionary.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.multipz.dictionary.Adapter.MathsWordAdapter;
import com.multipz.dictionary.Adapter.MyActivityAdapter;
import com.multipz.dictionary.Json.JSONParser;
import com.multipz.dictionary.Json.config;
import com.multipz.dictionary.Model.MathsCountModel;
import com.multipz.dictionary.Model.MathsWordModel;
import com.multipz.dictionary.Model.MyActivityModel;
import com.multipz.dictionary.R;
import com.multipz.dictionary.Util.Shared;
import com.skyfishjy.library.RippleBackground;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends AppCompatActivity {

    private JSONParser jsonparser = new JSONParser();
    JSONArray subjectArray;
    private JSONArray MathsWordList = new JSONArray();
    ArrayList<MyActivityModel> mathswordlist = new ArrayList<MyActivityModel>();
    ListView listView;
    String title,html_string,is_word_status;
    private static MyActivityAdapter adapter;
    Shared shared;
    ImageView back;
    Button btn_submit;
    private static final String SHOWCASE_ID = "ivocabe";
    RippleBackground rippleBackground;
    ArrayList<MathsCountModel> mathswordcount;
    TextView maths,chemistry,physics,biology,no_data,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        shared=new Shared(this);
        mathswordcount = new ArrayList<MathsCountModel>();
        listView = (ListView)findViewById(R.id.list1);
        back = (ImageView)findViewById(R.id.nav_back);

        no_data=(TextView)findViewById(R.id.No_data_found);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab_add_word);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                showdialogue();
            }
        });

        rippleBackground = (RippleBackground)findViewById(R.id.fab_ripple);
        rippleBackground.startRippleAnimation();

        new getAddWord().execute();

        try {
            subjectArray=new JSONArray(shared.getString("wordcount","[]"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String pos = i + "";
                Log.e("possion", pos);
                MyActivityModel item = mathswordlist.get(i);
                Intent intent = new Intent(MyActivity.this, MyWordActivity.class);
                intent.putExtra("html string", item.getHtml_string());
                intent.putExtra("title", item.getDef_title());
                Log.e("html string", item.getHtml_string());
                startActivity(intent);
            }
        });
    }

    public void showdialogue(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.myactivity_dialogue);

        maths=(TextView)dialog.findViewById(R.id.txt_maths);
        physics=(TextView)dialog.findViewById(R.id.txt_physics);
        chemistry=(TextView)dialog.findViewById(R.id.txt_chemistry);
        biology=(TextView)dialog.findViewById(R.id.txt_biology);
        cancel=(TextView)dialog.findViewById(R.id.txt_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        for (int i1 = 0; i1 < subjectArray.length(); i1++) {
            JSONObject jsonObject1 = null;
            try {
                jsonObject1 = subjectArray.getJSONObject(i1);

                mathswordcount.add(new MathsCountModel(jsonObject1.getString("subject_id"),
                        jsonObject1.getString("subject_name"),
                        jsonObject1.getString("is_status"),
                        jsonObject1.getString("word_count"),
                        jsonObject1.getString("formula_count")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(mathswordcount.get(0).getIs_status().contentEquals("Y")){
            maths.setVisibility(View.VISIBLE);
        }else {
            maths.setVisibility(View.GONE);
        }

        if(mathswordcount.get(1).getIs_status().contentEquals("Y")){
            physics.setVisibility(View.VISIBLE);
        }else {
            physics.setVisibility(View.GONE);
        }

        if(mathswordcount.get(2).getIs_status().contentEquals("Y")){
            chemistry.setVisibility(View.VISIBLE);
        }else {
            chemistry.setVisibility(View.GONE);
        }

        if(mathswordcount.get(3).getIs_status().contentEquals("Y")){
            biology.setVisibility(View.VISIBLE);
        }else {
            biology.setVisibility(View.GONE);
        }

        Typeface regular = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
        maths.setTypeface(regular);
        physics.setTypeface(regular);
        chemistry.setTypeface(regular);
        biology.setTypeface(regular);

        maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.subject_id=1;
                Intent intent = new Intent(MyActivity.this, AddWordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.subject_id=2;
                Intent intent = new Intent(MyActivity.this, AddWordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.subject_id=3;
                Intent intent = new Intent(MyActivity.this, AddWordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        biology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.subject_id=4;
                Intent intent = new Intent(MyActivity.this, AddWordActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    private class getAddWord extends AsyncTask<String, String, String> {

        private String url = config.MAIN_API;
        private int success;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MyActivity.this, "iVocabe", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            parm.add(new BasicNameValuePair("json", "{\"user_id\":\""+shared.getString("user id","")+"\",\"action\":\"userActivity\"}\n"));

            JSONObject jsonObject = new JSONParser().makeHttpRequest(url, "POST", parm);
            Log.e("url", url);
            Log.e("Response:", "" + jsonObject + "");

            try {
                success = jsonObject.getInt("status");
                Log.e("Response:", "" + success);

                if (success == 1) {
                    MathsWordList = jsonObject.getJSONArray("data");
                    for (int i1 = 0; i1 < MathsWordList.length(); i1++) {
                        JSONObject c = MathsWordList.getJSONObject(i1);

                        title = c.getString("def_title");
//                        word = c.getString("word");
                        html_string = c.getString("html_string");
                        is_word_status = c.getString("is_word_status");

                        MyActivityModel model = new MyActivityModel(title, html_string,is_word_status);
                        mathswordlist.add(model);
                    }
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            if (success == 1)
            {
                adapter = new MyActivityAdapter(MyActivity.this, mathswordlist);
                listView.setAdapter(adapter);
            }else if(success==0) {
                no_data.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
