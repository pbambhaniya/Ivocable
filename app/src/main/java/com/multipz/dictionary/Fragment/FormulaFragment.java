package com.multipz.dictionary.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.dictionary.Activity.AddWordActivity;
import com.multipz.dictionary.Activity.DetailsActivity;
import com.multipz.dictionary.Activity.FormulaDetailsActivity;
import com.multipz.dictionary.Activity.HomeActivity;
import com.multipz.dictionary.Activity.HorizontalListView;
import com.multipz.dictionary.Activity.ListWordActivity;
import com.multipz.dictionary.Adapter.FormulaExpandableListAdapter;
import com.multipz.dictionary.Adapter.HorizontalListAdapter;
import com.multipz.dictionary.Adapter.MathsWordAdapter;
import com.multipz.dictionary.Interface.onSearch;
import com.multipz.dictionary.Json.JSONParser;
import com.multipz.dictionary.Json.config;
import com.multipz.dictionary.Model.MathsFormulasModel;
import com.multipz.dictionary.Model.MathsWordModel;
import com.multipz.dictionary.R;
import com.multipz.dictionary.Util.Constant_method;
import com.multipz.dictionary.Util.Shared;
import com.skyfishjy.library.RippleBackground;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FormulaFragment extends Fragment {

    View view;
    ExpandableListView expListView;
    List<String> listDataHeader;
    public  static  List<String> listDataHeader1;
    Shared shared;
    public static HashMap<String, List<MathsFormulasModel>> listDataChild;

    private static FormulaExpandableListAdapter adapter;
    ArrayList<MathsFormulasModel> mathsformulaitem;
    public static int currentposition = 0, currentChild=0;

    private JSONArray MathsFormulasList = new JSONArray();
    private JSONArray MathsFormulasTitle = new JSONArray();
    private JSONArray MathsFormulasSubTitle = new JSONArray();
    public static ArrayList<MathsFormulasModel> mathsformulaslist = new ArrayList<MathsFormulasModel>();
    TextView No_data;

    String type_id,type_name,sub_type,type,formula_type_id,sub_type_name,formulas_title,desc_title,formula_desc_id,html_string,formulas,is_fav,subject_id;


    public FormulaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shared=new Shared(getActivity());

        view = inflater.inflate(R.layout.fragment_formula, container, false);
        expListView = (ExpandableListView) view.findViewById(R.id.list_formulas);
        No_data = (TextView) view.findViewById(R.id.No_data_found);

        listDataHeader = new ArrayList<String>();
        listDataHeader1 = new ArrayList<String>();
        listDataChild = new HashMap<String, List<MathsFormulasModel>>();


        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_add_word);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getActivity(), AddWordActivity.class);
                startActivity(intent);
            }
        });

        if(Constant_method.checkConn(getActivity())) {
            new MathsFormulasList().execute();
        } else {
            Toast.makeText(getActivity(), "Check Internet Connection ", Toast.LENGTH_SHORT).show();
        }

        adapter = new FormulaExpandableListAdapter(getActivity(), listDataHeader1, listDataChild);
        expListView.setAdapter(adapter);

        RippleBackground rippleBackground = (RippleBackground) view.findViewById(R.id.fab_ripple);
        rippleBackground.startRippleAnimation();

        ListWordActivity.onSearch = new onSearch() {
            @Override
            public void onFilter(String constraint) {
                listDataHeader1.clear();
                if (constraint != null && constraint.toString().length() > 0) {
                    for (String item : listDataHeader) {
                        if (item.toString().toLowerCase().contains(constraint)) {
                            listDataHeader1.add(item);
                        }
                    }
                } else {
                    listDataHeader1.addAll(listDataHeader);
                }

                adapter.notifyDataSetChanged();
            }
        };

//        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v,
//                                        int groupPosition, long id) {
//                // Toast.makeText(getApplicationContext(),
//                // "Group Clicked " + listDataHeader.get(groupPosition),
//                // Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
//
//        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getActivity(),
//                        listDataHeader.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getActivity(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(
//                        getActivity(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                listDataHeader.get(groupPosition)).get(
//                                childPosition).getSub_type_name(), Toast.LENGTH_SHORT)
//                        .show();

                String pos = childPosition + "";

                Log.e("possion", pos);
                currentposition = groupPosition;
                currentChild = childPosition;

                MathsFormulasModel item = listDataChild.get(listDataHeader1.get(groupPosition)).get(childPosition);
                Intent intent = new Intent(getActivity(), FormulaDetailsActivity.class);
                intent.putExtra("html string", item.getHtml_string());
                intent.putExtra("sub type", item.getSub_type_name());
                intent.putExtra("subject id", item.getSubject_id());
                intent.putExtra("formula desc id", item.getFormula_desc_id());
                intent.putExtra("formula",item.getFormulas_title());
                intent.putExtra("is favourite", item.getIs_favourite());
//                intent.putExtra("description", item.getDefinition_desc());
                Log.e("html string", item.getHtml_string());
                startActivity(intent);
                return false;
            }
        });

       return view;
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
            parm.add(new BasicNameValuePair("json", "{\"page\":\"1\",\"subject_id\":\""+ HomeActivity.subject_id+"\",\"user_id\":\""+shared.getString("user id","")+"\",\"action\":\"getFormulaData\"}"));

            JSONObject jsonObject = new JSONParser().makeHttpRequest(url, "POST", parm);
            Log.e("url", url);
            Log.e("Response:", "" + jsonObject + "");
            try {
                success = jsonObject.getInt("status");
                mathsformulaslist.clear();
                Log.e("Response:", "" + success);
                if (success == 1) {
                    MathsFormulasList = jsonObject.getJSONArray("data");
                    JSONObject c = MathsFormulasList.getJSONObject(0);

                    subject_id=c.getString("subject_id");
                    String subject_name=c.getString("subject_name");
                    MathsFormulasTitle = c.getJSONArray("subType");

                    for(int i1 = 0; i1 < MathsFormulasTitle.length(); i1++)
                    {
                        JSONObject jitem = MathsFormulasTitle.getJSONObject(i1);
                        formula_type_id = jitem.getString("formula_type_id");
                        subject_id = jitem.getString("subject_id");
                        type = jitem.getString("type");

                        listDataHeader.add(type);
                        MathsFormulasSubTitle = jitem.getJSONArray("info");
                        mathsformulaitem = new ArrayList<MathsFormulasModel>();
                        for(int i2 = 0; i2 < MathsFormulasSubTitle.length(); i2++)
                        {
                            JSONObject jsubitem = MathsFormulasSubTitle.getJSONObject(i2);
                            formula_desc_id = jsubitem.getString("formula_desc_id");
                            formula_type_id = jsubitem.getString("formula_type_id");
                            sub_type_name = jsubitem.getString("sub_type");
                            formulas_title = jsubitem.getString("formula");
//                            formulas = jsubitem.getString("formula");
                            is_fav = jsubitem.getString("is_favourite");
//                            desc_title = jsubitem.getString("desc_title");
                            html_string = jsubitem.getString("html_string");

                            MathsFormulasModel model = new MathsFormulasModel(formula_type_id,type,sub_type_name,html_string,formulas_title,formula_desc_id,is_fav,subject_id);
                            mathsformulaitem.add(model);
                        }
                        listDataChild.put(listDataHeader.get(i1),mathsformulaitem);
                    }

                    listDataHeader1.clear();
                    listDataHeader1.addAll(listDataHeader);
                }else {
                    No_data.setVisibility(View.VISIBLE);
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
            if (success == 1)
            {
                adapter.notifyDataSetChanged();
            }
        }
    }

}
