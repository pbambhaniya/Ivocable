package com.multipz.dictionary.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.multipz.dictionary.Fragment.FormulaFavouriteFragment;
import com.multipz.dictionary.Fragment.FormulaFragment;
import com.multipz.dictionary.Json.JSONParser;
import com.multipz.dictionary.Json.config;
import com.multipz.dictionary.R;
import com.multipz.dictionary.Util.Shared;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FormulaFavouriteDetailsActivity extends AppCompatActivity {

    WebView webView;
    ImageView back;
    TextView title;
    String getsubtype, getdesc, getformulas, getformulasdesc,getsub_id,getis_fav,getis_fav1;
    Shared shared;
    boolean isFavourite=false;
    String def, def_desc, word;
    boolean lastPageChange = false;
    Context context;

    private ViewPager viewPager;
    ImageView fab_share,fab_favourite,fab_favourite1;
    private DetailsPagerAdapter detailPagerAdapter;
    private InterstitialAd mInterstitialAd;

    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;

    static {
        TRANSFORM_CLASSES = new ArrayList<>();
        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
    }

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula_favourite_details);
        context=this;
        mAdView = (AdView)findViewById(R.id.adView);
        /*AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        shared=new Shared(this);

        fab_share=(ImageView)findViewById(R.id.fab_share);
        fab_favourite=(ImageView)findViewById(R.id.fab_favourite);
        fab_favourite1=(ImageView)findViewById(R.id.fab_favourite1);

        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.TEXT", "Word : " + FormulaFragment.listDataChild.get(FormulaFragment.listDataHeader1.get(FormulaFragment.currentposition)).get(FormulaFragment.currentChild).getSub_type_name() +
                        (FormulaFragment.listDataChild.get(FormulaFragment.listDataHeader1.get(FormulaFragment.currentposition)).get(FormulaFragment.currentChild).getFormulas_title().contentEquals("")?"":"\n\nDescription : " +  FormulaFragment.listDataChild.get(FormulaFragment.listDataHeader1.get(FormulaFragment.currentposition)).get(FormulaFragment.currentChild).getFormulas_title())+
                        "\n\n" + "Via - iVocabe Dictionary \nCheck out App at: https://play.google.com/store/apps/details?id="+context.getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        fab_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavourite=true;
                fab_favourite1.setVisibility(View.VISIBLE);
                fab_favourite.setVisibility(View.GONE);
                new FavouriteList().execute();
                FormulaFavouriteFragment.listDataChild.get(FormulaFavouriteFragment.listDataHeader1.get(FormulaFavouriteFragment.currentposition)).get(FormulaFavouriteFragment.currentChild).setIs_favourite("1");
//                Toast.makeText(FormulaDetailsActivity.this, "Add Favourite", Toast.LENGTH_SHORT).show();
            }
        });

        fab_favourite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavourite=false;
                fab_favourite1.setVisibility(View.GONE);
                fab_favourite.setVisibility(View.VISIBLE);
                new FavouriteList().execute();
                FormulaFavouriteFragment.listDataChild.get(FormulaFavouriteFragment.listDataHeader1.get(FormulaFavouriteFragment.currentposition)).get(FormulaFavouriteFragment.currentChild).setIs_favourite("0");
//                Toast.makeText(FormulaDetailsActivity.this, "UnFavourite", Toast.LENGTH_SHORT).show();
            }
        });

        webView = (WebView) findViewById(R.id.webview);
        back = (ImageView) findViewById(R.id.nav_back);
        title = (TextView) findViewById(R.id.details_formulas_title);
        viewPager = (ViewPager) findViewById(R.id.view_pager_detail);

        detailPagerAdapter = new DetailsPagerAdapter();
        viewPager.setAdapter(detailPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        try {
            viewPager.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getdesc = getIntent().getStringExtra("html string");
        getsub_id = getIntent().getStringExtra("subject id");
        getsubtype = getIntent().getStringExtra("sub type");
        getformulas = getIntent().getStringExtra("formula");
        getis_fav = getIntent().getStringExtra("is favourite");
        getformulasdesc = getIntent().getStringExtra("formula desc id");

        if(getis_fav.contains("1")) {
            fab_favourite1.setVisibility(View.VISIBLE);
            fab_favourite.setVisibility(View.GONE);
            //Toast.makeText(DetailsActivity.this, "Add Favourite", Toast.LENGTH_SHORT).show();
        } else if(getis_fav.contains("0")) {
            fab_favourite1.setVisibility(View.GONE);
            fab_favourite.setVisibility(View.VISIBLE);
            //Toast.makeText(DetailsActivity.this, "UnFavourite", Toast.LENGTH_SHORT).show();
        }

        viewPager.setCurrentItem(FormulaFavouriteFragment.currentChild);
        title.setText(FormulaFavouriteFragment.listDataChild.get(FormulaFavouriteFragment.listDataHeader1.get(FormulaFavouriteFragment.currentposition)).get(FormulaFavouriteFragment.currentChild).getSub_type_name());
    }

    private class FavouriteList extends AsyncTask<String, String, String> {

        private String url = config.MAIN_API;
        private int success;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FormulaFavouriteDetailsActivity.this, "iVocabe", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            parm.add(new BasicNameValuePair("json", "{\"subject_id\": \"" + getsub_id + "\",\"desc_id\":\"" +getformulasdesc+ "\",\"user_id\":\"" +shared.getString("user id","")+ "\",\"desc_type\":\"F\",\"action\":\"addFavourite\"}\n"));

            JSONObject jsonObject = new JSONParser().makeHttpRequest(url, "POST", parm);
            Log.e("url", url);
            Log.e("Response:", "" + jsonObject + "");

            try {
                success = jsonObject.getInt("status");
                Log.e("Response:", "" + success);
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
//                Toast.makeText(DetailsActivity.this, "Add Favourite", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class DetailsPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public DetailsPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.formula_detail_pager, container, false);
            webView = (WebView) view.findViewById(R.id.webview);
            webView.loadData(FormulaFavouriteFragment.listDataChild.get(FormulaFavouriteFragment.listDataHeader1.get(FormulaFavouriteFragment.currentposition)).get(position).getHtml_string(), "text/html", "UTF-8");
            webView.getSettings().setBuiltInZoomControls(true);

//            webView.setWebViewClient(new WebViewClient() {
//                ProgressDialog progressDialog;
//
//                //If you will not use this method url links are opeen in new brower not in webview
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                    return true;
//                }
//
//                //Show loader on url load
//                public void onLoadResource (WebView view, String url) {
//                    if (progressDialog == null) {
//                        // in standard case YourActivity.this
//                        progressDialog = new ProgressDialog(context);
//                        progressDialog.setMessage("Loading...");
//                        progressDialog.show();
//                    }
//                }
//                public void onPageFinished(WebView view, String url) {
//                    try{
//                        if (progressDialog.isShowing()) {
//                            progressDialog.dismiss();
//                            progressDialog = null;
//                        }
//                    }catch(Exception exception){
//                        exception.printStackTrace();
//                    }
//                }
//
//            });

            def_desc = FormulaFavouriteFragment.listDataChild.get(FormulaFavouriteFragment.listDataHeader1.get(FormulaFavouriteFragment.currentposition)).get(position).getFormulas_title();
//            getis_fav = FormulaFavouriteFragment.listDataChild.get(FormulaFavouriteFragment.listDataHeader1.get(FormulaFavouriteFragment.currentposition)).get(position).getIs_favourite();

            shared.putInt("isAdLoad", shared.getInt("isAdLoad")+1);

            if (shared.getInt("isAdLoad") >= 10)
            {
                loadFullScreen();
            }

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return FormulaFavouriteFragment.listDataChild.get(FormulaFavouriteFragment.listDataHeader1.get(FormulaFavouriteFragment.currentposition)).size();
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

    private void loadFullScreen(){
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            shared.putInt("isAdLoad",0);
        }
    }

    private ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
//            webView.loadData(getdesc, "text/html", "UTF-8");
            FormulaFavouriteFragment.currentChild = position;
            getformulasdesc = FormulaFavouriteFragment.listDataChild.get(FormulaFavouriteFragment.listDataHeader1.get(FormulaFavouriteFragment.currentposition)).get(position).getFormula_desc_id();
            def = FormulaFavouriteFragment.listDataChild.get(FormulaFavouriteFragment.listDataHeader1.get(FormulaFavouriteFragment.currentposition)).get(position).getSub_type_name();
            getis_fav1 = FormulaFavouriteFragment.listDataChild.get(FormulaFavouriteFragment.listDataHeader1.get(FormulaFavouriteFragment.currentposition)).get(position).getIs_favourite();
            title.setText(def);

            if(getis_fav1.contains("1")) {
                fab_favourite1.setVisibility(View.VISIBLE);
                fab_favourite.setVisibility(View.GONE);
                // Toast.makeText(DetailsActivity.this, "Add Favourite", Toast.LENGTH_SHORT).show();
            } else if(getis_fav1.contains("0")) {
                fab_favourite1.setVisibility(View.GONE);
                fab_favourite.setVisibility(View.VISIBLE);
                // Toast.makeText(DetailsActivity.this, "UnFavourite", Toast.LENGTH_SHORT).show();
            }
            //viewPager.setCurrentItem(FormulaFavouriteFragment.currentChild);
            Log.e("call","onpageselected");

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            Log.e("call", "onpagescroll");
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            Log.e("call", "onpagescrollstatechnage");
//            int lastIdx = detailPagerAdapter.getCount() - 1;
//            int curItem = viewPager.getCurrentItem();
//            if (curItem == lastIdx && arg0 == 1) {
//                lastPageChange = true;
////                FormulaFavouriteFragment.currentChild = arg0;
////                title.setText(def);
//                // i put this here since onPageScroll gets called a couple of times.
//                lastPageChange = false;
//            } else {
//                lastPageChange = false;
//            }
        }
    };

    private static final class TransformerItem {

        final String title;
        final Class<? extends ViewPager.PageTransformer> clazz;

        public TransformerItem(Class<? extends ViewPager.PageTransformer> clazz) {
            this.clazz = clazz;
            title = clazz.getSimpleName();
        }

        @Override
        public String toString() {
            return title;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
