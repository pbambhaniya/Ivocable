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
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.github.florent37.tutoshowcase.TutoShowcase;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.multipz.dictionary.Fragment.WordFragment;
import com.multipz.dictionary.Json.JSONParser;
import com.multipz.dictionary.Json.config;
import com.multipz.dictionary.R;
import com.multipz.dictionary.Util.Constant_method;
import com.multipz.dictionary.Util.Shared;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class DetailsActivity extends AppCompatActivity {

    ImageView image, share, back;
    private ViewPager viewPager;
    private DetailsPagerAdapter detailPagerAdapter;
    TextView title, desc;
    String getname, getdesc, getsub_id,getdec_id, getdef_desc,getis_fav,getis_fav1;
    WebView webView;
    private ViewFlipper viewFlipper;
    private float lastX;
    String def, def_desc, word;
    SharedPreferences pref;
    String user_login_id;
    boolean lastPageChange = false;
    Shared shared;
    Context context;
    boolean isFavourite=false;
    public static final String Index = "index" ;
    ImageView fab_share,fab_favourite,fab_favourite1;
    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;
    private static final String SHOWCASE_ID = "simple example";
    TextView swipe;
    static {
        TRANSFORM_CLASSES = new ArrayList<>();
        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
    }
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        context=this;
        mAdView = (AdView)findViewById(R.id.adView);

        /*if(Constant_method.checkConn(context)){
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }else {
            mAdView.setVisibility(View.GONE);
        }*/

        init();
        shared = new Shared(this);
        getdesc = getIntent().getStringExtra("html string");
        getname = getIntent().getStringExtra("definition");
        getdef_desc = getIntent().getStringExtra("description");
        getsub_id = getIntent().getStringExtra("subject id");
        getdec_id = getIntent().getStringExtra("desc id");
        getis_fav = getIntent().getStringExtra("is favourite");

        if(getis_fav.contains("1")) {
            fab_favourite1.setVisibility(View.VISIBLE);
            fab_favourite.setVisibility(View.GONE);
            //Toast.makeText(DetailsActivity.this, "Add Favourite", Toast.LENGTH_SHORT).show();
        } else if(getis_fav.contains("0")) {
            fab_favourite1.setVisibility(View.GONE);
            fab_favourite.setVisibility(View.VISIBLE);
            //Toast.makeText(DetailsActivity.this, "UnFavourite", Toast.LENGTH_SHORT).show();
        }

        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.TEXT", "Word : " + WordFragment.mathswordlist1.get(WordFragment.currentposition).getDefinition() +
                        (WordFragment.mathswordlist1.get(WordFragment.currentposition).getDefinition_desc().contentEquals("")?"":"\n\nDescription : " +  WordFragment.mathswordlist1.get(WordFragment.currentposition).getDefinition_desc()) +
                        "\n\n" + "Via - iVocabe Dictionary \n Check out App at: https://play.google.com/store/apps/details?id="+context.getPackageName());
                sendIntent.setType("text/plain");
                DetailsActivity.this.startActivity(sendIntent);
            }
        });

        fab_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                fab_favourite1.setVisibility(View.VISIBLE);
//                fab_favourite.setVisibility(View.GONE);
                new FavouriteList().execute();
//                Toast.makeText(DetailsActivity.this, "Add Favourite", Toast.LENGTH_SHORT).show();
            }
        });

        fab_favourite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fab_favourite1.setVisibility(View.GONE);
//                fab_favourite.setVisibility(View.VISIBLE);
                new FavouriteList().execute();
            }
        });

        swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTuto();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title.setText(getname);
        detailPagerAdapter = new DetailsPagerAdapter();
        viewPager.setAdapter(detailPagerAdapter);
        viewPager.setCurrentItem(WordFragment.currentposition);
        title.setText(WordFragment.mathswordlist1.get(WordFragment.currentposition).getDefinition());
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        try {
            viewPager.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }



        if(shared.getBoolean("Dialogue",true)){
            swipe.performClick();
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

    public class DetailsPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public DetailsPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.detail_pager, container, false);
            webView = (WebView) view.findViewById(R.id.webview);
            webView.loadData(WordFragment.mathswordlist1.get(position).getHtml_string(), "text/html","UTF-8");
            getis_fav1 = WordFragment.mathswordlist1.get(position).getIs_favourite();
            //getdec_id = WordFragment.mathswordlist1.get(WordFragment.currentposition).getWord_desc_id();

            webView.getSettings().setBuiltInZoomControls(true);

            def_desc = WordFragment.mathswordlist1.get(position).getDefinition_desc();
//            getis_fav = WordFragment.mathswordlist1.get(position).getIs_favourite();

            shared.putInt("isAdLoad", shared.getInt("isAdLoad")+1);

            if (shared.getInt("isAdLoad") >= 10){
                loadFullScreen();
            }

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return WordFragment.mathswordlist1.size();
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

    private class FavouriteList extends AsyncTask<String, String, String> {

        private String url = config.MAIN_API;
        private int success;
        private ProgressDialog progressDialog;

        String isFav = getis_fav1;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(DetailsActivity.this, "iVocabe", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            parm.add(new BasicNameValuePair("json", "{\"subject_id\": \"" + getsub_id + "\",\"desc_id\":\"" +getdec_id+ "\",\"user_id\":\"" +shared.getString("user id","")+ "\",\"desc_type\":\"W\",\"action\":\"addFavourite\"}\n"));

            JSONObject jsonObject = new JSONParser().makeHttpRequest(url,"POST", parm);
            Log.e("url", url);
            Log.e("Response:", "" + jsonObject + "");

            try {
                success = jsonObject.getInt("status");
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                isFav = jsonObject1.getString("is_favourite");
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
                if(isFav.contains("1")) {
                    fab_favourite1.setVisibility(View.VISIBLE);
                    fab_favourite.setVisibility(View.GONE);
                }
                else if(isFav.contains("0")) {
                    fab_favourite1.setVisibility(View.GONE);
                    fab_favourite.setVisibility(View.VISIBLE);
                }
                WordFragment.mathswordlist1.get(WordFragment.currentposition).setIs_favourite(isFav);
            }
        }
    }

    private void init() {
        webView = (WebView) findViewById(R.id.webview);
        back = (ImageView) findViewById(R.id.nav_back);
        title = (TextView) findViewById(R.id.detailstitle);
        viewPager = (ViewPager) findViewById(R.id.view_pager_detail);
        fab_share=(ImageView)findViewById(R.id.fab_share);
        fab_favourite=(ImageView)findViewById(R.id.fab_favourite);
        fab_favourite1=(ImageView)findViewById(R.id.fab_favourite1);
        swipe=(TextView)findViewById(R.id.swipe);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    TutoShowcase ddi;
    protected void displayTuto() {
        ddi = TutoShowcase.from(this)
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
//                        Toast.makeText(DetailsActivity.this, "Tutorial dismissed", Toast.LENGTH_SHORT).show();
                    }
                })

                .setContentView(R.layout.tuto_sample)
                .setFitsSystemWindows(true)
                .on(R.id.swipe)
                .addCircle()
                .withBorder()
                .onClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .onClickContentView(R.id.got_it, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shared.putBoolean("Dialogue",false);
                        clossDialog();
                    }
                })
                .on(R.id.view_pager_detail)
                .displaySwipableRight()
                .delayed(500)
                .animated(true)
                .show();
    }

    void clossDialog(){
        ddi.dismiss();
    }

    private ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(final int position) {
//            webView.loadData(getdesc, "text/html", "UTF-8");
            WordFragment.currentposition = position;
            getdec_id = WordFragment.mathswordlist1.get(WordFragment.currentposition).getWord_desc_id();
            def = WordFragment.mathswordlist1.get(position).getDefinition();
            getis_fav1 = WordFragment.mathswordlist1.get(position).getIs_favourite();
            title.setText(def);

            if(getis_fav1.contains("1")) {
                fab_favourite1.setVisibility(View.VISIBLE);
                fab_favourite.setVisibility(View.GONE);
//                WordFragment.mathswordlist1.get(position).getWord_desc_id();
                //Toast.makeText(DetailsActivity.this, "Add Favourite", Toast.LENGTH_SHORT).show();
            }
            else if(getis_fav1.contains("0")) {
                fab_favourite1.setVisibility(View.GONE);
                fab_favourite.setVisibility(View.VISIBLE);
                //Toast.makeText(DetailsActivity.this, "UnFavourite", Toast.LENGTH_SHORT).show();
            }
            Log.e("call", "onpageselected");
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            Log.e("call", "onpagescroll");
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            Log.e("call", "onpagescrollstatechnage");

            int lastIdx = detailPagerAdapter.getCount() - 1;
            int curItem = viewPager.getCurrentItem();

            if (curItem == lastIdx && arg0 == 1) {
                lastPageChange = true;
                // i put this here since onPageScroll gets called a couple of times.
                lastPageChange = false;
            } else {
                lastPageChange = false;
            }
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
