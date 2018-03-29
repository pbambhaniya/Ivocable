package com.multipz.dictionary.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.multipz.dictionary.Activity.AddWordActivity;
import com.multipz.dictionary.Activity.DetailsActivity;
import com.multipz.dictionary.Activity.HomeActivity;
import com.multipz.dictionary.Activity.ListWordActivity;
import com.multipz.dictionary.Adapter.MathsWordAdapter;
import com.multipz.dictionary.Adapter.HorizontalListAdapter;
import com.multipz.dictionary.Activity.HorizontalListView;
import com.multipz.dictionary.Json.JSONParser;
import com.multipz.dictionary.Model.MathsWordModel;
import com.multipz.dictionary.R;
import com.multipz.dictionary.Json.config;
import com.multipz.dictionary.Interface.onSearch;
import com.multipz.dictionary.Util.Constant_method;
import com.multipz.dictionary.Util.Shared;
import com.skyfishjy.library.RippleBackground;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WordFragment extends Fragment {
    HorizontalListView lv;
    View view;
    private JSONParser jsonparser = new JSONParser();

    private JSONArray MathsWordList = new JSONArray();
    ArrayList<MathsWordModel> mathswordlist = new ArrayList<MathsWordModel>();
    public static ArrayList<MathsWordModel> mathswordlist1 = new ArrayList<MathsWordModel>();
    String subject_name, word, word_desc_id, word_id, subject_id, definition, definition_desc, html_string, definition_image, formula, formula_image, example, example_image, created_date, modified_date, is_status,is_favourite, is_delete;
    ListView listView;
    public static int currentposition = 0;
    String getword = "A";
    TextView No_data;
    int pagecount = 1;
    Shared shared;
    RippleBackground rippleBackground;
    static private boolean isLive = false;

    public int firstVisibleItem, visibleItemCount, totalItemCount;

    private static MathsWordAdapter adapter1;
    private AdView mAdView;

    public WordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_word, container, false);
        shared=new Shared(getActivity());
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_add_word);
        No_data = (TextView) view.findViewById(R.id.No_data_found);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getActivity(), AddWordActivity.class);
                startActivity(intent);
            }
        });

        rippleBackground = (RippleBackground) view.findViewById(R.id.fab_ripple);
        rippleBackground.startRippleAnimation();

        /*FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 0, -((rippleBackground.getWidth()/2)+rippleBackground.getWidth()), -((rippleBackground.getHeight()/2)+rippleBackground.getHeight()));
        rippleBackground.setLayoutParams(params);*/

        if(Constant_method.checkConn(getActivity())) {
            new MathsDetails().execute();
        }else {
            Toast.makeText(getActivity(), "Check Internet Connection ", Toast.LENGTH_SHORT).show();

        }
        init();

        adapter1 = new MathsWordAdapter(getActivity(), mathswordlist1);
        listView.setAdapter(adapter1);

        ListWordActivity.onSearch = new onSearch() {
            @Override
            public void onFilter(String constraint) {
                mathswordlist1.clear();

                if (constraint != null && constraint.toString().length() > 0) {
                    for (MathsWordModel item : mathswordlist) {
                        if (item.getDefinition().toString().toLowerCase().contains(constraint)) {
                            mathswordlist1.add(item);
                        }
                    }
                } else {
                    mathswordlist1.addAll(mathswordlist);
                }
                adapter1.notifyDataSetChanged();
            }
        };

        return view;
    }

    private void init() {

        lv = (HorizontalListView) view.findViewById(R.id.list_horizontal);
        final HorizontalListAdapter adapter = new HorizontalListAdapter(getActivity());
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                String pos = i + "";
                Log.e("position", pos);
                getword = adapter.getItem(i).toString();
                pagecount = 1;
                currentposition = 1;
                Log.e("word", getword);
                adapter.notifyme(i);
//                Toast.makeText(getActivity(), getword, Toast.LENGTH_SHORT).show();
                mathswordlist1.clear();
                mathswordlist.clear();
                adapter1.notifyDataSetChanged();
                new MathsDetails().execute();
            }
        });

        adapter.notifyDataSetChanged();

        listView = (ListView) view.findViewById(R.id.list1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String pos = i + "";
                Log.e("possion", pos);
                currentposition = i;
                MathsWordModel item = mathswordlist1.get(i);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount && i == SCROLL_STATE_IDLE) {
                    pagecount++;
                    new MathsDetails().execute();
                    //get next 10-20 items(your choice)items
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItemm, int visibleItemCountt, int totalItemCountt) {

                firstVisibleItem = firstVisibleItemm;
                visibleItemCount = visibleItemCountt;
                totalItemCount = totalItemCountt;
                       /* if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0)
                        {
                            if(flag_loading == false)
                            {
                                flag_loading = true;
                                new MathsDetails().execute();

                            }

                       }*/
            }
        });
    }

    private class MathsDetails extends AsyncTask<String, String, String> {
        private String url = config.MAIN_API;
        private int success=0;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog = ProgressDialog.show(getActivity(), "iVocabe", "Please wait...", true, false);
            if(pagecount == 1)
            {
                progressDialog = ProgressDialog.show(getActivity(), "iVocabe", "Please wait...", true, false);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            parm.add(new BasicNameValuePair("json", "{\"page\":\""+pagecount+"\",\"user_id\":\""+shared.getString("user id","")+"\",\"word\":\""+getword+"\",\"subject_id\":\""+ HomeActivity.subject_id+"\",\"action\":\"getWordData\"}"));

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
                String action = jsonObject.getString("action");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            if (isLive) {
                mathswordlist1.clear();
                mathswordlist1.addAll(mathswordlist);
            }

            if (success == 1 || success == 0) {

                if(mathswordlist1.size() == 0){
                    No_data.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }else {
                    listView.setVisibility(View.VISIBLE);
                    No_data.setVisibility(View.GONE);
                    if (isLive) {
                        adapter1.notifyDataSetChanged();
                    }
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        isLive = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isLive = false;
    }
}
