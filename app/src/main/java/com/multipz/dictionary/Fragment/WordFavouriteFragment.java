package com.multipz.dictionary.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.dictionary.Activity.DetailsActivity;
import com.multipz.dictionary.Activity.FavouriteActivity;
import com.multipz.dictionary.Activity.WordFavouriteDetailsActivity;
import com.multipz.dictionary.Adapter.MathsWordAdapter;
import com.multipz.dictionary.Json.JSONParser;
import com.multipz.dictionary.Json.config;
import com.multipz.dictionary.Model.MathsWordModel;
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

public class WordFavouriteFragment extends Fragment {

    private JSONParser jsonparser = new JSONParser();

    private JSONArray MathsWordList = new JSONArray();
    public static ArrayList<MathsWordModel> mathswordlist = new ArrayList<MathsWordModel>();
    public static int currentposition = 0;

    String subject_name, word, word_desc_id, word_id, subject_id, definition, definition_desc, html_string, definition_image, formula, formula_image, example, example_image, created_date, modified_date, is_status,is_favourite, is_delete;

    ListView listView;
    Shared shared;
    String token;
    boolean isValid=false;
    String user_id;
    TextView No_data;

    public int firstVisibleItem, visibleItemCount, totalItemCount;

    public static MathsWordAdapter adapter1;


    public WordFavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_word_favourite, container, false);
        shared=new Shared(getActivity());
        token = shared.getString("notification", "");

        listView = (ListView) view.findViewById(R.id.list1);
        No_data = (TextView) view.findViewById(R.id.No_data_found);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String pos = i + "";
                Log.e("possion", pos);
                currentposition = i;
                MathsWordModel item = mathswordlist.get(i);
                Intent intent = new Intent(getActivity(), WordFavouriteDetailsActivity.class);
                intent.putExtra("html string", item.getHtml_string());
                intent.putExtra("definition", item.getDefinition());
                intent.putExtra("description", item.getDefinition_desc());
                intent.putExtra("subject id", item.getSubject_id());
                intent.putExtra("desc id", item.getWord_desc_id());
                intent.putExtra("is favourite", item.getIs_favourite());
                Log.e("html string", item.getHtml_string());
                startActivity(intent);
            }
        });

        String user_id=shared.getString("user id","");

        if(Constant_method.checkConn(getActivity())) {
            if(user_id.contentEquals("")) {
                new Configdata().execute();
            }else {
                new MathsDetails().execute();
            }
        }else
        {
            Toast.makeText(getActivity(), "Check Internet " +
                    "Connection ", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String user_id=shared.getString("user id","");
        if(Constant_method.checkConn(getActivity())) {

            if(user_id.contentEquals("")) {
                new Configdata().execute();
            }else {
                new MathsDetails().execute();
            }

        }else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private class MathsDetails extends AsyncTask<String, String, String> {

        private String url = config.MAIN_API;
        private int success;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "iVocabe", "Please wait...", true, false);

        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            parm.add(new BasicNameValuePair("json", "{\"page\":\"1\",\"subject_id\":\""+ FavouriteActivity.subject_id+"\",\"user_id\":\""+shared.getString("user id","")+"\",\"action\":\"getWordFavourite\"}"));
            mathswordlist.clear();

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

                        subject_name = c.getString("subject_name");
//                        word = c.getString("word");
                        word_desc_id = c.getString("word_desc_id");
                        word_id = c.getString("word_id");
                        subject_id = c.getString("subject_id");
                        definition = c.getString("def_title");
                        definition_desc = c.getString("def_desc");
                        html_string = c.getString("html_string");
                        definition_image = c.getString("def_img");
                        formula = c.getString("formula");
                        formula_image = c.getString("formula_img");
                        example = c.getString("example");
                        example_image = c.getString("example_img");
                        created_date = c.getString("created_date");
                        modified_date = c.getString("modified_date");
                        is_status = c.getString("is_status");
                        is_delete = c.getString("is_delete");
                        is_favourite = c.getString("is_favourite");

                        MathsWordModel model = new MathsWordModel(definition, html_string, definition_desc,subject_id,word_desc_id,is_favourite);
                        mathswordlist.add(model);
                    }
                }
//                String action = jsonObject.getString("action");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (success == 1) {
                adapter1 = new MathsWordAdapter(getActivity(), mathswordlist);
                listView.setAdapter(adapter1);
            }else if(success == 0) {
                adapter1 = new MathsWordAdapter(getActivity(), mathswordlist);
                listView.setAdapter(adapter1);
                No_data.setVisibility(View.VISIBLE);
            }
        }
    }

    private class Configdata extends AsyncTask {

        private String url = config.MAIN_API;
        private int success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            parm.add(new BasicNameValuePair("json", "{\"device_id\":\"" + token + "\",\"device_type\":\"A\",\"device_token\":\""+Constant_method.Get_id(getActivity())+"\",\"social_id\":\"\",\"social_type\":\"\",\"action\":\"userLogin\"}\n"));

            JSONObject jsonObject = jsonparser.makeHttpRequest(url, "POST", parm);
            Log.e("Response:", "" + jsonObject + "");
            try {
                success = jsonObject.getInt("status");
                Log.e("Response:", "" + success);

                if (success == 1) {
                     JSONObject object = jsonObject.getJSONObject("data");
                     String user_login_id = object.getString("user_login_id");
                     shared.putString("user id",user_login_id);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (success == 1) {

            }
        }
    }


}
