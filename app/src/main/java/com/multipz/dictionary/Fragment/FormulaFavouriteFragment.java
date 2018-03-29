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
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.dictionary.Activity.FavouriteActivity;
import com.multipz.dictionary.Activity.FormulaDetailsActivity;
import com.multipz.dictionary.Activity.FormulaFavouriteDetailsActivity;
import com.multipz.dictionary.Activity.WordFavouriteDetailsActivity;
import com.multipz.dictionary.Adapter.FormulaExpandableListAdapter;
import com.multipz.dictionary.Adapter.MathsWordAdapter;
import com.multipz.dictionary.Json.JSONParser;
import com.multipz.dictionary.Json.config;
import com.multipz.dictionary.Model.MathsFormulasModel;
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
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormulaFavouriteFragment extends Fragment {

    View view;
    ExpandableListView expListView;
    List<String> listDataHeader;
    public static List<String> listDataHeader1;
    private JSONParser jsonparser = new JSONParser();
    Shared shared;
    public static int currentposition = 0, currentChild = 0;
    public static HashMap<String, List<MathsFormulasModel>> listDataChild;

    private static FormulaExpandableListAdapter adapter;
    ArrayList<MathsFormulasModel> mathsformulaitem;

    private JSONArray MathsFormulasList = new JSONArray();
    private JSONArray MathsFormulasTitle = new JSONArray();
    private JSONArray MathsFormulasSubTitle = new JSONArray();
    TextView No_data;

    public static ArrayList<MathsFormulasModel> mathsformulaslist1 = new ArrayList<MathsFormulasModel>();
    String type_id, type_name, sub_type, type, formula_type_id, sub_type_name, formulas_title, desc_title, formula_desc_id, html_string, formulas, is_fav, subject_id;
    String token;
    private static MathsWordAdapter adapter1;


    public FormulaFavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_formula_favourite, container, false);

        shared = new Shared(getActivity());
        token = shared.getString("notification", "");

        expListView = (ExpandableListView) view.findViewById(R.id.list_formulas);
        No_data = (TextView) view.findViewById(R.id.No_data_found);
        listDataHeader = new ArrayList<String>();
        listDataHeader1 = new ArrayList<String>();
        listDataChild = new HashMap<String, List<MathsFormulasModel>>();

        String user_id = shared.getString("user id", "");

       /* if (Constant_method.checkConn(getActivity())) {

            if (user_id.contentEquals("")) {
                new Configdata().execute();
            } else {
                new MathsFormulasList().execute();
            }

        } else {
            Toast.makeText(getActivity(), "Check Internet Connection ", Toast.LENGTH_SHORT).show();
        }*/

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                // TODO Auto-generated method stub
                String pos = childPosition + "";
                Log.e("possion", pos);

                currentposition = groupPosition;
                currentChild = childPosition;

                MathsFormulasModel item = listDataChild.get(listDataHeader1.get(groupPosition)).get(childPosition);
                Intent intent = new Intent(getActivity(), FormulaFavouriteDetailsActivity.class);
                intent.putExtra("html string", item.getHtml_string());
                intent.putExtra("sub type", item.getSub_type_name());
                intent.putExtra("subject id", item.getSubject_id());
                intent.putExtra("formula desc id", item.getFormula_desc_id());
                intent.putExtra("formula", item.getFormulas_title());
                intent.putExtra("is favourite", item.getIs_favourite());
//                intent.putExtra("description", item.getDefinition_desc());
                Log.e("html string", item.getHtml_string());
                startActivity(intent);

                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("call","");
        String user_id=shared.getString("user id","");
        if(Constant_method.checkConn(getActivity())) {

            if(user_id.contentEquals("")) {
                new Configdata().execute();
            }else {
                new MathsFormulasList().execute();
            }

        }else {
            Toast.makeText(getActivity(), "Check Internet Connection ", Toast.LENGTH_SHORT).show();
        }
    }




    private class MathsFormulasList extends AsyncTask<String, String, String> {

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
            parm.add(new BasicNameValuePair("json", "{\"page\":\"1\",\"subject_id\":\"" + FavouriteActivity.subject_id + "\",\"user_id\":\"" + shared.getString("user id", "") + "\",\"action\":\"getFormulaFavourite\"}"));

            JSONObject jsonObject = new JSONParser().makeHttpRequest(url, "POST", parm);
            Log.e("url", url);
            Log.e("Response:", "" + jsonObject + "");

            listDataHeader.clear();
            listDataChild.clear();

            try {
                success = jsonObject.getInt("status");
                Log.e("Response:", "" + success);

                if (success == 1) {
                    MathsFormulasList = jsonObject.getJSONArray("data");
                    JSONObject c = MathsFormulasList.getJSONObject(0);

                    subject_id = c.getString("subject_id");
                    String subject_name = c.getString("subject_name");

                    MathsFormulasTitle = c.getJSONArray("subType");

                    for (int i1 = 0; i1 < MathsFormulasTitle.length(); i1++) {
                        JSONObject jitem = MathsFormulasTitle.getJSONObject(i1);

                        formula_type_id = jitem.getString("formula_type_id");
                        subject_id = jitem.getString("subject_id");
                        type = jitem.getString("type");

                        listDataHeader.add(type);
                        MathsFormulasSubTitle = jitem.getJSONArray("info");

                        mathsformulaitem = new ArrayList<MathsFormulasModel>();

                        for (int i2 = 0; i2 < MathsFormulasSubTitle.length(); i2++) {
                            JSONObject jsubitem = MathsFormulasSubTitle.getJSONObject(i2);
                            formula_desc_id = jsubitem.getString("formula_desc_id");
                            formula_type_id = jsubitem.getString("formula_type_id");
                            sub_type_name = jsubitem.getString("sub_type");
                            formulas_title = jsubitem.getString("formula");
//                            formulas = jsubitem.getString("formula");
                            is_fav = jsubitem.getString("is_favourite");
//                            desc_title = jsubitem.getString("desc_title");
                            html_string = jsubitem.getString("html_string");

                            MathsFormulasModel model = new MathsFormulasModel(formula_type_id, type, sub_type_name, html_string, formulas_title, formula_desc_id, is_fav, subject_id);
                            mathsformulaitem.add(model);

                        }
                        listDataChild.put(listDataHeader.get(i1), mathsformulaitem);
                    }
                    listDataHeader1.clear();
                    listDataHeader1.addAll(listDataHeader);
                }

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
                adapter = new FormulaExpandableListAdapter(getActivity(), listDataHeader1, listDataChild);
                expListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (MathsFormulasTitle.length() <= 0) {
                    No_data.setVisibility(View.VISIBLE);
                }
            } else if (success == 0) {
                adapter = new FormulaExpandableListAdapter(getActivity(), listDataHeader1, listDataChild);
                expListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                No_data.setVisibility(View.VISIBLE);
            }
        }
    }


    private class Configdata extends AsyncTask {

        private String url = config.MAIN_API;
        private int success;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "iVocabe", "Please wait...", true, false);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            parm.add(new BasicNameValuePair("json", "{\"device_id\":\"" + token + "\",\"device_type\":\"A\",\"device_token\":\"" + Constant_method.Get_id(getActivity()) + "\",\"social_id\":\"\",\"social_type\":\"\",\"action\":\"userLogin\"}\n"));

            JSONObject jsonObject = jsonparser.makeHttpRequest(url, "POST", parm);
            Log.e("Response:", "" + jsonObject + "");

            try {
                success = jsonObject.getInt("status");

                Log.e("Response:", "" + success);
                if (success == 1) {
                    JSONObject object = jsonObject.getJSONObject("data");
                    String user_login_id = object.getString("user_login_id");
                    shared.putString("user id", user_login_id);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            if (success == 1) {

            }
        }
    }


}
